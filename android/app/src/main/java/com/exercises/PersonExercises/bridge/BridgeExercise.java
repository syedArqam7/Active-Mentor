package com.exercises.PersonExercises.bridge;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class BridgeExercise extends AbstractPersonExercise {

    private static final float MINIMAL_HIP_ANGLE = 50;
    private static final float MINIMAL_KNEE_ANGLE = 85;
    private static final int MAXIMUM_ANGLE = 360;
    private static final int MINIMUM_ANGLE = 20;
    private static final int FRAMES_LOOK_BACK = 5;
    Pose leftPose, rightPose;
    LAYDOWN laydown;
    private boolean timerStart = false;
    private int poseUnmatchedCounts = 0;


    public BridgeExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.leg_raise_calibration_outlines, R.raw.leg_raise_calibration_correct, R.raw.leg_raise_calibration_calibrate), exerciseSettings);
        calibration.setCalibrationSize(0.1f, 1, 0.1f, 0.9f);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        //draw something
    }

    @Override
    protected void initExercise() {

        leftPose = new PersonPose(person)
                .below(person.leftLeg.ankle, person.leftLeg.hip)
                .below(person.leftArm.wrist, person.leftLeg.hip)
                .below(person.face.nose, person.leftLeg.hip)
                .above(person.leftLeg.knee, person.face.nose)
                .above(person.leftLeg.knee, person.leftLeg.hip)
                .above(person.leftLeg.knee, person.leftLeg.ankle)
                .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, (int) MINIMAL_HIP_ANGLE, MAXIMUM_ANGLE)
                .angle(person.leftLeg.ankle, person.leftLeg.knee, person.leftLeg.hip, MINIMUM_ANGLE, (int) MINIMAL_KNEE_ANGLE)
                .rightOf(person.leftLeg.knee, person.leftLeg.hip)
                .rightOf(person.leftLeg.hip, person.leftArm.shoulder);

        rightPose = new PersonPose(person)
                .below(person.rightLeg.ankle, person.rightLeg.hip)
                .below(person.rightArm.wrist, person.rightLeg.hip)
                .below(person.face.nose, person.rightLeg.hip)
                .above(person.rightLeg.knee, person.face.nose)
                .above(person.rightLeg.knee, person.rightLeg.hip)
                .above(person.rightLeg.knee, person.rightLeg.ankle)
                .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, (int) MINIMAL_HIP_ANGLE, MAXIMUM_ANGLE)
                .angle(person.rightLeg.ankle, person.rightLeg.knee, person.rightLeg.hip, MINIMUM_ANGLE, (int) MINIMAL_KNEE_ANGLE)
                .leftOf(person.rightLeg.knee, person.rightLeg.hip)
                .leftOf(person.rightLeg.hip, person.rightArm.shoulder);

    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        drawTextDebug(canvas, "continueTime: " + timerStart);
//        drawTextDebug(canvas, "poseUnmatchedCounts: " + poseUnmatchedCounts);
    }

    private void laydownSide() {
        if (person.leftLeg.hip.getX() > person.leftArm.shoulder.getX()) {
            laydown = LAYDOWN.Left;
        } else {
            laydown = LAYDOWN.Right;
        }

    }

    private void processRight() {
        if (rightPose.match()) {
            score.continueTime();
            poseUnmatchedCounts = 0;
            timerStart = true;
        } else {
            if (poseUnmatchedCounts >= FRAMES_LOOK_BACK) {
                score.stopTime();
                timerStart = false;
            } else {
                poseUnmatchedCounts++;
            }
        }
    }

    private void processLeft() {
        if (leftPose.match()) {
            score.continueTime();
            poseUnmatchedCounts = 0;
            timerStart = true;
        } else {
            if (poseUnmatchedCounts >= FRAMES_LOOK_BACK) {
                score.stopTime();
                timerStart = false;
            } else {
                poseUnmatchedCounts++;
            }
        }
    }

    protected void processExercise(InfoBlob infoBlob) {


        laydownSide();
        switch (laydown) {
            case Right:
                processRight();
                break;
            case Left:
                processLeft();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + laydown);
        }

    }

    @Override
    public String toString() {
        return "BridgeExercise{" +
                "laydown=" + laydown +
                ",  MINIMAL_HIP_ANGLE=" + MINIMAL_HIP_ANGLE +
                ", MINIMAL_KNEE_ANGLE=" + MINIMAL_KNEE_ANGLE +
                '}';
    }

    @Override
    public String getName() {
        return "bridge";
    }

    enum LAYDOWN {
        Right,
        Left
    }


}
