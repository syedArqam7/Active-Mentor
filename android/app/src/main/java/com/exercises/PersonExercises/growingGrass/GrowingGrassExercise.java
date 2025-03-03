package com.exercises.PersonExercises.growingGrass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.widget.ImageView;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseClient;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.render.lottie.LottieCalibration;
import com.render.lottie.LottieRender;
import com.render.sounds.SoundRender;
import com.utils.InfoBlob;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.exercises.base.exercise.ExerciseActivity.timer;

public class GrowingGrassExercise extends AbstractPersonExercise {

    private static final int NUM_OF_ASSETS = 7;
    private final List<GrowingGrassAssets> growingGrassAssetsList = new ArrayList<>();
    private static final double START_X = 0.1; // Initial X position of the growingGrass Assets.
    private static final double START_Y = 0.5; // Initial Y position of the growingGrass Assets.
    private static final double WIDTH = 0.12; // To help separate the assets and check for condition in GrowingGrassAssets->isPersonIn().
    // draw side banners at the ends of the screen.
    LottieRender leftSideBanner, rightSideBanner;
    private ExerciseOutro exerciseOutro;
    private boolean isGameOver = false;


    public GrowingGrassExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
    }


    @Override
    protected void drawExercise(Canvas canvas) {
        //IntStream.range(0, NUM_OF_ASSETS).forEach(x -> growingGrassAssetsList.get(x).draw(canvas));
        exerciseOutro.draw(canvas);
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        //IntStream.range(0, NUM_OF_ASSETS).forEach(x -> growingGrassAssetsList.get(x).draw(canvas));
        exerciseOutro.draw(canvas);  // outro animation will be displayed when game is over.
    }





    @Override
    protected void initExercise() {
        initGrowingGrassAssets();
        long startTime = SystemClock.elapsedRealtime();
        exerciseOutro = new ExerciseOutro(startTime);
    }


    private void initGrowingGrassAssets() {
        IntStream.range(0, NUM_OF_ASSETS).forEach(x -> growingGrassAssetsList.add(new GrowingGrassAssets(START_X, START_Y, WIDTH, x)));
        leftSideBanner = new LottieRender(R.raw.side_jogo_banner).ephemeral(false).background(Color.BLACK).setNormalizedLayout(0.9, 0, 1, 1).setNormalizedPosition(0.05, 0.5).setScaleType(ImageView.ScaleType.FIT_END).loop().play();
        rightSideBanner = new LottieRender(R.raw.side_jogo_banner).ephemeral(false).background(Color.BLACK).setNormalizedLayout(0, 0, 0.1, 1).setNormalizedPosition(0.95, 0.5).setScaleType(ImageView.ScaleType.FIT_START).loop().play();
    }


    /**
     * Hides all the lottie animations when game is over. This is basically to have a cleaner outro.
     */
    private void tidyUpLottieAssets() {
        IntStream.range(0, NUM_OF_ASSETS).forEach(x -> {
            growingGrassAssetsList.get(x).hideLottieJumpArrow();
            growingGrassAssetsList.get(x).hideLottieGrass();
        });
        leftSideBanner.hide();
        rightSideBanner.hide();
    }

    private void stopExercise() {
        ExerciseClient.getClient().stopExercise();
    }


    protected void processExercise(InfoBlob infoBlob) {

        if (isGameOver) return;

        IntStream.range(0, NUM_OF_ASSETS).forEach(x ->
        {

            growingGrassAssetsList.get(x).process(person);

            if (growingGrassAssetsList.get(x).isGrassFullyGrown()) {
                isGameOver = true;
                new SoundRender(R.raw.game_over).play();
                tidyUpLottieAssets();
                exerciseOutro.initiate();
                timer.schedule(this::stopExercise, 3000);
                return; // don't bother processing other assets
            }

        });
    }


    @Override
    public String getName() {
        return "growing-grass";
    }

    @Override
    public String toString() {
        return "growing-grass";
    }

}






