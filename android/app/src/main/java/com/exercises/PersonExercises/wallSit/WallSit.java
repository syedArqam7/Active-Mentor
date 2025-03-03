package com.exercises.PersonExercises.wallSit;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;

public class WallSit extends AbstractPersonExercise {
    private static final int FRAMES_LOOK_BACK = 5;
    private int poseUnmatchedCounts = 0;
    private Pose pose;
    public WallSit(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.wall_sit_calibration_outlines, R.raw.wall_sit_calibrations_correct, R.raw.wall_sit_calibration_calibrate), exerciseSettings);
    }

    @Override
    protected void initExercise() {
        pose = new PersonPose(person)
                .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.knee, 75, 135)
                .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.knee, 75, 135)
                .angle(person.leftLeg.hip, person.leftLeg.knee, person.leftLeg.ankle, 90, 135)
                .angle(person.rightLeg.hip, person.rightLeg.knee, person.rightLeg.ankle, 90, 135);
    }

    @Override
    protected void processExercise(InfoBlob infoBlob) {
        if (pose.match()) {
            score.continueTime();
            poseUnmatchedCounts = 0;
        } else if (poseUnmatchedCounts >= FRAMES_LOOK_BACK) {
            score.stopTime();
        } else {
            poseUnmatchedCounts++;
        }
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {

        pose.drawDebug(canvas);
    }


    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    public String getName() {
        return "wall-sit";
    }
}
