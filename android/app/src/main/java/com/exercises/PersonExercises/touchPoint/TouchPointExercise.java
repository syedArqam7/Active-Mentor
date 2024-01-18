package com.exercises.PersonExercises.touchPoint;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;

public class TouchPointExercise extends AbstractPersonExercise {

    MODE exerciseMode = MODE.HANDS;
    TouchPointManager tpManager;
    TouchPoint currentTouchPoint;
    int SKIPPED_COUNTS = 0;
    public TouchPointExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
    }

    @Override
    protected void initExercise() {
        switch (exerciseSettings.getExerciseVariation()) {
            case 0:
                exerciseMode = MODE.HANDS;
                break;
            case 1:
                exerciseMode = MODE.FEET;
                break;
            case 3:
                exerciseMode = MODE.ANY;
            default:
                throw new IllegalStateException("Unexpected value: " + exerciseSettings.getExerciseVariation());
        }
        tpManager = new TouchPointManager(person, score, exerciseMode);
        currentTouchPoint = tpManager.spawnNewTouchPoint();

    }

    @Override
    protected void processExercise(InfoBlob infoBlob) {

        switch (exerciseMode) {
            case HANDS:
                currentTouchPoint.processHandsMode();
                break;

            case FEET:
                currentTouchPoint.processFeetMode();
                break;

            case ANY:
                currentTouchPoint.processAnyMode();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + exerciseMode);

        }

        if (!currentTouchPoint.isActive()) {

            if (currentTouchPoint.isMissed())
                SKIPPED_COUNTS++;

            currentTouchPoint = tpManager.spawnNewTouchPoint();
        }

    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        tpManager.draw(canvas);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        currentTouchPoint.draw(canvas);
    }

    @Override
    public String getName() {
        return "touch-points";
    }


    enum MODE {HANDS, FEET, ANY}

}


