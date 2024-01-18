package com.exercises.PersonExercises.plank;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class PlankExercise extends AbstractPersonExercise {


    private final static int FRAMES_LOOK_BACK = 5;
    LAYDOWN laydown; // acts as a conditional variable
    private boolean aboveSlopeValue;
    private PersonPose leftPose, rightPose;
    private int poseUnmatchedCounts = 0;


    public PlankExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.plank_calibration_outlines, R.raw.plank_calibration_correct, R.raw.plank_calibration_calibrate), exerciseSettings);

    }

    @Override
    protected void initExercise() {
        final int MINIMAL_HIP_ANGLE = 100;
        final int MAXIMUM_HIP_ANGLE = 360;
        final int MINIMAL_KNEE_ANGLE = 112;
        final int SHOULDER_HIGH = 55;

        leftPose = new PersonPose(person)
                .below(person.leftArm.wrist, person.leftArm.shoulder)
                .below(person.leftLeg.knee, person.leftArm.shoulder)
                .below(person.leftArm.elbow, person.leftArm.shoulder)
                .below(person.leftArm.elbow, person.leftLeg.hip)
                .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, MINIMAL_HIP_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.rightLeg.hip, person.rightLeg.knee, person.rightLeg.ankle, MINIMAL_KNEE_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.leftLeg.hip, person.leftLeg.knee, person.leftLeg.ankle, MINIMAL_KNEE_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.leftArm.elbow, person.leftArm.shoulder, person.leftLeg.hip, SHOULDER_HIGH, MAXIMUM_HIP_ANGLE);


        rightPose = new PersonPose(person)
                .below(person.rightArm.wrist, person.rightArm.shoulder)
                .below(person.rightLeg.knee, person.rightArm.shoulder)
                .below(person.rightArm.elbow, person.rightArm.shoulder)
                .below(person.rightArm.elbow, person.rightLeg.hip)
                .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, MINIMAL_HIP_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.rightLeg.hip, person.rightLeg.knee, person.rightLeg.ankle, MINIMAL_KNEE_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.leftLeg.hip, person.leftLeg.knee, person.leftLeg.ankle, MINIMAL_KNEE_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.rightArm.elbow, person.rightArm.shoulder, person.rightLeg.hip, SHOULDER_HIGH, MAXIMUM_HIP_ANGLE);

    }

    @Override
    protected void drawExercise(Canvas canvas) {
        // class abstract method implementation
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        leftPose.drawDebug(canvas);
//        rightPose.drawDebug(canvas);
//        drawTextDebug(canvas, String.valueOf(laydown));
//        drawTextDebug(canvas, String.valueOf(aboveSlopeValue));
    }

    private void layDownSide() {
        if (person.leftLeg.hip.getX() > person.leftArm.shoulder.getX()) {
            laydown = LAYDOWN.Left;
        } else {
            laydown = LAYDOWN.Right;
        }
    }

    private void processRight() {
        if (aboveSlopeValue && rightPose.match()) {
            score.continueTime();
            poseUnmatchedCounts = 0;
        } else if (poseUnmatchedCounts >= FRAMES_LOOK_BACK) {
            score.stopTime();
        } else {
            poseUnmatchedCounts++;
        }
    }

    private void processLeft() {
        if (aboveSlopeValue && leftPose.match()) {
            score.continueTime();
            poseUnmatchedCounts = 0;
        } else if (poseUnmatchedCounts >= FRAMES_LOOK_BACK) {
            score.stopTime();
        } else {
            poseUnmatchedCounts++;
        }
    }

    protected void processExercise(InfoBlob infoBlob) {

        final double MINIMAL_SLOPE_VALUE = 0.55;
        final double MINIMAL_SLOPE_BACK_CAMERA_VALUE = 0.75;

        double shoulderY = ((person.rightArm.shoulder.getDetectedLocation().getY()) + (person.leftArm.shoulder.getDetectedLocation().getY())) / 2;
        double shoulderX = ((person.rightArm.shoulder.getDetectedLocation().getX()) + (person.leftArm.shoulder.getDetectedLocation().getX())) / 2;
        double heelY = ((person.rightLeg.heel.getDetectedLocation().getY()) + (person.leftLeg.heel.getDetectedLocation().getY())) / 2;
        double heelX = ((person.rightLeg.heel.getDetectedLocation().getX()) + (person.leftLeg.heel.getDetectedLocation().getX())) / 2;

        double slopeShoulderKnee = Math.abs((shoulderY - heelY) / (shoulderX - heelX));

        layDownSide();

        if (exerciseSettings.isBackCameraUsed()) {
            aboveSlopeValue = slopeShoulderKnee < MINIMAL_SLOPE_BACK_CAMERA_VALUE;
        } else {
            aboveSlopeValue = slopeShoulderKnee < MINIMAL_SLOPE_VALUE;
        }

        switch (laydown) {
            case Left:
                processLeft();
                break;
            case Right:
                processRight();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + laydown);
        }


    }

    @Override
    public String toString() {
        return "PlankExercise{" +
                "score=" + score_count +
                '}';
    }

    @Override
    public String getName() {
        return "plank";
    }

    enum LAYDOWN {
        Right,
        Left
    }


}
