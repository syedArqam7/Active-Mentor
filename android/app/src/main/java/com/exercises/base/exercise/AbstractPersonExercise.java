package com.exercises.base.exercise;

import android.graphics.Canvas;

import com.detection.person.PersonDetection;
import com.exercises.base.calibration.Calibration;
import com.models.ModelManager;
import com.render.lottie.LottieCalibration;


public abstract class AbstractPersonExercise extends AbstractExercise {
    protected final boolean upperBodyOnly;
    protected final PersonDetection person;


    public AbstractPersonExercise(Calibration abstractCalibration, ExerciseSettings exerciseSettings, ModelManager.MODELTYPE modelType, boolean upperBodyOnly) {
        super(abstractCalibration, exerciseSettings);
        this.upperBodyOnly = upperBodyOnly;
        person = new PersonDetection(true, modelType, upperBodyOnly);
        person.setExerciseLead(true);
        objectDetections.add(person);

        if (abstractCalibration != null)
            person.getDetectionSubClasses().forEach(calibration::addObjectDetection);
    }

    public AbstractPersonExercise(Calibration abstractCalibration, ExerciseSettings exerciseSettings, ModelManager.MODELTYPE modelType) {
        this(abstractCalibration, exerciseSettings, modelType, false);
    }

    public AbstractPersonExercise(LottieCalibration animation, ExerciseSettings exerciseSettings, ModelManager.MODELTYPE modeltype, boolean upperBodyOnly) {
        this(new Calibration(animation), exerciseSettings, modeltype, upperBodyOnly);
    }

    public AbstractPersonExercise(LottieCalibration animation, ExerciseSettings exerciseSettings, ModelManager.MODELTYPE modelType) {
        this(new Calibration(animation), exerciseSettings, modelType, false);

    }

    public AbstractPersonExercise(LottieCalibration animation, ExerciseSettings exerciseSettings, boolean UpperBodyOnly) {
        this(new Calibration(animation), exerciseSettings, ModelManager.MODELTYPE.POSENET, UpperBodyOnly);

    }

    public AbstractPersonExercise(LottieCalibration animation, ExerciseSettings exerciseSettings) {
        this(new Calibration(animation), exerciseSettings, ModelManager.MODELTYPE.POSENET, false);
    }

    protected void drawBoundingBox(Canvas canvas) {

        person.drawBBox(canvas);
    }
}
