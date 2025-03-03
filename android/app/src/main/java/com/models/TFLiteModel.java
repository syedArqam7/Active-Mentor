package com.models;

import android.content.Context;
import android.util.Pair;

import com.google.android.odml.image.BitmapExtractor;
import com.location.DetectionLocation;
import com.logger.SLOG;
import com.utils.ExtendedMLImage;
import com.utils.InfoBlob;

// import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
// import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.nnapi.NnApiDelegate;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;


public abstract class TFLiteModel extends Model {

    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;

    final String modelFileName;
    /*
     *
     *
     * Initializing the model
     *
     *
     */
    protected int imageSizeY;
    protected int imageSizeX;
    // protected DataType imageDataType;
    protected int afterPreProcessFrameId;
    protected int supplyFramePreProcessId;
    Device device = Device.GPU;
    Interpreter detectionModel;
    ImageProcessor imageProcessor;
    private FlowableEmitter<Pair<ExtendedMLImage, InfoBlob>> sourceEmitter;
    private Flowable<Pair<ExtendedMLImage, InfoBlob>> pipeline;

    public TFLiteModel(float confidenceScore, String modelFileName) {
        super(confidenceScore);
        this.modelFileName = modelFileName;
        buildPipeline();

    }

    @Override
    public void loadModel(Context context) throws IOException {
        MappedByteBuffer tfliteModel
                = FileUtil.loadMappedFile(context,
                modelFileName);

        Interpreter.Options options = (new Interpreter.Options());
        switch (device) {
            case CPU:
                break;
            // case GPU:
            //     GpuDelegate delegate = new GpuDelegate();
            //     options.addDelegate(delegate);
            //     break;
            case NNAPI:
                NnApiDelegate nnApiDelegate = new NnApiDelegate();
                options.addDelegate(nnApiDelegate);
                break;
            default:
                SLOG.e("Modelfile cannot be loaded");
                throw new IllegalStateException("Unexpected Device Value " + device);
        }

        detectionModel = new Interpreter(tfliteModel, options);

        int imageTensorIndex = 0;
        int[] imageShape = detectionModel.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}

        // imageDataType = detectionModel.getInputTensor(imageTensorIndex).dataType();
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];

        imageProcessor = buildImageProcessor();
    }

    protected ImageProcessor buildImageProcessor() {
        return new ImageProcessor.Builder()
                .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD))
                .build();
    }

    /*
     *
     *
     * Building the Pipeline
     *
     *
     */
    protected Flowable<Pair<TensorImage, InfoBlob>> preProcessImage(Pair<ExtendedMLImage, InfoBlob> extendedMlImageInfoBlobPair) {
        TensorImage tensorImage = TensorImage.fromBitmap(BitmapExtractor.extract((extendedMlImageInfoBlobPair.first.getMlImage())));
        tensorImage = imageProcessor.process(tensorImage);

        return Flowable.just(new Pair<>(tensorImage, extendedMlImageInfoBlobPair.second));
    }

    protected abstract Flowable<Pair<List<DetectionLocation>, InfoBlob>> RunModel(Pair<TensorImage, InfoBlob> extendedMlImageInfoBlobPair);

    private void buildPipeline() {
        pipeline = Flowable.create(emitter -> sourceEmitter = emitter, BackpressureStrategy.LATEST);
        pipeline.observeOn(Schedulers.computation())
                .filter((blob) -> supplyFramePreProcessId == blob.second.getFrameID())
                .concatMapEager(this::preProcessImage)
                .doOnNext((blob) -> afterPreProcessFrameId = blob.second.getFrameID())

                .observeOn(Schedulers.computation())
                .filter((blob) -> afterPreProcessFrameId == blob.second.getFrameID())
                .concatMap(this::RunModel)
                .subscribe(this::distributeLocations);
    }

    @Override
    public void supplyFrameInternal(ExtendedMLImage mlImage) {
        if (!running) return;
        InfoBlob infoBlob = new InfoBlob(mlImage);
        supplyFramePreProcessId = infoBlob.getFrameID();
        sourceEmitter.onNext(new Pair<>(mlImage, infoBlob));
    }

    @Override
    public void stop() {
        super.stop();
        sourceEmitter.onComplete();
        pipeline = null;
        sourceEmitter = null;
        if (detectionModel != null) detectionModel.close();
    }


    enum Device {
        NNAPI,
        GPU,
        CPU
    }


}
