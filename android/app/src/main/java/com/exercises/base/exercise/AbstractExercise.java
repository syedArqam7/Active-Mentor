package com.exercises.base.exercise;

import android.app.job.JobInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Pair;

// import com.detection.BallDetection;
import com.detection.ObjectDetection;
import com.detection.person.PersonDetection;
import com.exercises.base.calibration.Calibration;
import com.exercises.base.statistics.AppStatistics;
import com.exercises.score.Score;
import com.logger.SLOG;
import com.render.lottie.LottieCountDown;
import com.utils.Gear;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.UtilArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;


public abstract class AbstractExercise {
    //todo this class would also benefit from some nice rewriting

    protected final UtilArrayList<InfoBlob> infoBlobArrayList = new UtilArrayList<>();
    protected final List<ObjectDetection> objectDetections = new ArrayList<>();

    public Score score;
    protected Calibration calibration;
    protected ExerciseSettings exerciseSettings;
    protected AppStatistics appStatistics = new AppStatistics();
    protected boolean running = true;
    protected int score_count = 0;
    protected int timeScore = 0;

    protected int BOUNDING_BOX_TIMEOUT = 6000;

    protected String outText = "";
    protected JOGOPaint textPaint;
    protected STATUS status = STATUS.CALIBRATION;
    protected boolean objectInScreenCheckEnabled = true;
    //todo this is not extremely clean
    boolean detectionsPaused = false;
    long inFrameLastUpdatedTime = SystemClock.elapsedRealtime();
    long OBJECTS_UNDETECTED_TIME_WINDOW = 2000;

    public AbstractExercise(Calibration calibration, ExerciseSettings exerciseSettings) {
        this.calibration = calibration;
        this.exerciseSettings = exerciseSettings;

        score = ExerciseClient.getClient().score;

        if (calibration != null) {
            calibration.setAbstractExercise(this);
            status = STATUS.CALIBRATION;
        }

    }

    /********** main exercise functions **********/


    protected void initExercise() {
        //todo this is a temporary fix, and will be solved when we have a better exerciseSettings approach
        //can be overridden if required
    }

    public void onCalibrated() {
        score.scoreFrameIDs.add(new Pair<>(infoBlobArrayList.getLast().getFrameID(), -1)); // exerciseStartFrameID
        //countdown
        new LottieCountDown().onEnd(this::startMainExercise).play();
    }

    public boolean isInCountDownState() {
        return status == STATUS.COUNTDOWN;
    }

    protected void startMainExercise() {
        if (!running) return;
        initExercise();
        score.start();
        //starts the main logic of the exercise
        appStatistics.startExercise();
        status = STATUS.EXERCISE;
        eActivity.startRecording();
    }

    public boolean start() {
        appStatistics.start();
        //if we don't have calibration, we assume we are calibrated
        if (calibration == null) startMainExercise();
        running = true;
        return true;
    }

    //allows for clean stop of all the other modules
    protected void stopDescendants() {
        if (!running) return;
        running = false;

        eActivity.stopRecording();
        score.stop();
        objectDetections.forEach(ObjectDetection::unSubscribe);
        if (calibration != null) calibration.cleanup();

    }

    public void stop() {
        stopDescendants();
    }

    public void drawDebug(Canvas canvas) {
        //this should not be overridden, we have drawExerciseDebug for that
        appStatistics.drawModelStatistics(canvas, objectDetections);
        switch (status) {
            case CALIBRATION:
                calibration.drawDebug(canvas);
                break;
            case COUNTDOWN:
                //handled by the view
                break;
            case EXERCISE:
                if (Gear.iff("DrawExerciseDebug", true)) drawExerciseDebug(canvas);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        canvas.drawText(getName(), canvas.getWidth() - 30, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().fillStroke().red());

    }

    public void draw(Canvas canvas) {
        //todo this should not be overridden, we have drawExercise for that


        switch (status) {
            case CALIBRATION:
                calibration.draw(canvas);
                drawBoundingBox(canvas);
                break;
            case COUNTDOWN:
                //handled by the view
                break;
            case EXERCISE:
                timeScore++;
                if (score.timeSinceLastScore() > BOUNDING_BOX_TIMEOUT) drawBoundingBox(canvas);

                drawOutText(canvas);
                drawExercise(canvas);
                canvas.drawText("Score: " + score.getCount(), JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().small().monospace());
                canvas.drawText("Score_count: " + score_count, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().small().monospace());
                canvas.drawText("TimeScore: " + timeScore, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().small().monospace());
                objectDetections.forEach(objectDetection -> objectDetection.draw(canvas));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    protected void drawOutText(Canvas canvas) {
        if (textPaint == null)
            textPaint = new JOGOPaint().yellow().textSize(130).align(Paint.Align.CENTER).bioSansBold(eActivity.getAssets());

        float y = 0.5f;
        for (String line : outText.split("\n")) {
            canvas.drawText(line, 0.50f * canvas.getWidth(), 0.5f * canvas.getHeight() + y, textPaint);
            y += textPaint.descent() - textPaint.ascent();
        }

    }

    protected void drawTextDebug(Canvas canvas, String text) {
        canvas.drawText(text, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint);
    }

    protected void drawTextLargeDebug(Canvas canvas, String text) {
        canvas.drawText(text, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugLargeHeight(), JOGOPaint.debugLargePaint);
    }

    protected void incrementScore() {
        score.incrementCount();
    }

    /********** process functions **********/
    protected void processCalibration() {
        calibration.processCalibration();
        if (calibration.isCalibrated()) {
            status = STATUS.COUNTDOWN;
            onCalibrated();
        }
    }

    public void process(InfoBlob infoBlob) {

        if (!running) return;
        //todo should not be overridden, we have processExercise for that
        infoBlobArrayList.add(infoBlob);

        switch (status) {
            case CALIBRATION:
                processCalibration();
                break;
            case COUNTDOWN:
                break;
            case EXERCISE:
                if (objectInScreenCheckEnabled && !isObjectInScreen()) {
                    outText = "Move inside\nthe screen";
                    score.stopTime();
                    detectionsPaused = true;
                    return;
                } else {
                    if (detectionsPaused) {
                        detectionsPaused = false;
                        outText = "";
                        score.continueTime();
                    }
                }

                score.setCurrentFrameID(infoBlob.getFrameID());

                processExercise(infoBlob); // runs subclass' processExercise()

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }

    }

    protected boolean isObjectInScreen() {

        if (objectDetections.stream().allMatch(ObjectDetection::inFrame)) {
            inFrameLastUpdatedTime = SystemClock.elapsedRealtime();
        }

        return (SystemClock.elapsedRealtime() - inFrameLastUpdatedTime) <= OBJECTS_UNDETECTED_TIME_WINDOW;
    }

    /********** JSON **********/

    public void writeToJSON(JSONObject exerciseData) throws JSONException {
        exerciseData.put("exercise_name", getName());
        exerciseData.put("high_score", exerciseSettings.getHighScore());
        exerciseData.put("completion_time", TimeUnit.MILLISECONDS.toSeconds(SystemClock.elapsedRealtime() - appStatistics.exerciseTime));
        for (ObjectDetection obj : objectDetections) {
            if (obj instanceof PersonDetection) obj.writeToJSON(exerciseData);
            // if (obj instanceof BallDetection) obj.writeToJSON(exerciseData);
        }

    }

    /********** abstract functions **********/
    protected abstract void drawBoundingBox(Canvas canvas);

    protected abstract void drawExerciseDebug(Canvas canvas);

    public abstract String getName();

    protected abstract void drawExercise(Canvas canvas);

    protected abstract void processExercise(InfoBlob infoBlob);

    enum STATUS {
        CALIBRATION,
        COUNTDOWN,
        EXERCISE
    }


}
