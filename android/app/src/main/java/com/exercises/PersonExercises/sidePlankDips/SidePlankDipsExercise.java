package com.exercises.PersonExercises.sidePlankDips;


import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;


public class SidePlankDipsExercise extends AbstractPersonExercise {

    private MOTION motion = MOTION.DOWN;
    private PersonPose upPose, downPose, sidePlankPose;
    private static final double MINIMAL_SLOPE_VALUE = 0.65;
    private static final double MINIMAL_SLOPE_BACK_CAMERA_VALUE = 0.85;
    private boolean slopeValue;
    private static final int MINIMAL_HIP_ANGLE = 165;
    private static final int MINIMAL_DOWN_HIP_ANGLE = 80;
    private static final int MAXIMAL_DOWN_HIP_ANGLE = 160;


    public SidePlankDipsExercise(ExerciseSettings exerciseSettings) {
        super(
                new LottieCalibration(R.raw.side_plank_calibration_outlines, R.raw.side_plank_calibration_correct, R.raw.side_plank_calibration_calibrate,
                        exerciseSettings.getExerciseVariation() != 0),
                exerciseSettings);
        calibration.setCalibrationSize(0.1f, 1, 0.1f, 0.9f);
    }

    @Override
    protected void initExercise() {
        switch (exerciseSettings.getExerciseVariation()) {
            case 0:
                initLeft();
                break;
            case 1:
                initRight();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + exerciseSettings.getExerciseVariation());
        }
    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.

//        drawTextLargeDebug(canvas, "GO " + motion);
//        drawTextDebug(canvas, "Slope: " + slopeValue);
//
//        if (downPose == null || sidePlankPose == null) return;
//
//        downPose.drawDebug(canvas);
//        sidePlankPose.drawDebug(canvas);

    }

    private void initLeft() {
        if (exerciseSettings.isBackCameraUsed()) {

            sidePlankPose = new PersonPose(person)
                    .below(person.rightArm.wrist, person.rightArm.shoulder)
                    .below(person.rightLeg.heel, person.rightArm.shoulder)
                    .below(person.rightArm.shoulder, person.leftArm.shoulder)
                    .below(person.rightLeg.hip, person.leftLeg.hip)
                    .below(person.rightLeg.hip, person.leftArm.wrist)
                    .below(person.rightArm.elbow, person.rightArm.shoulder)
                    .rightOf(person.rightLeg.heel, person.rightLeg.ankle)
                    .rightOf(person.rightLeg.heel, person.rightLeg.knee)
                    .rightOf(person.rightLeg.knee, person.rightLeg.hip)
                    .rightOf(person.rightLeg.hip, person.rightArm.shoulder);

            upPose = new PersonPose(person)
                    .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, (int) MINIMAL_HIP_ANGLE, 360);

            downPose = new PersonPose(person)
                    .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, (int) MINIMAL_DOWN_HIP_ANGLE, (int) MAXIMAL_DOWN_HIP_ANGLE);
        } else {
            sidePlankPose = new PersonPose(person)
                    .below(person.leftArm.wrist, person.leftArm.shoulder)
                    .below(person.leftLeg.heel, person.leftArm.shoulder)
                    .below(person.leftArm.shoulder, person.rightArm.shoulder)
                    .below(person.leftLeg.hip, person.rightLeg.hip)
                    .below(person.leftLeg.hip, person.rightArm.wrist)
                    .below(person.leftArm.elbow, person.leftArm.shoulder)
                    .rightOf(person.leftLeg.heel, person.leftLeg.ankle)
                    .rightOf(person.leftLeg.heel, person.leftLeg.knee)
                    .rightOf(person.leftLeg.knee, person.leftLeg.hip)
                    .rightOf(person.leftLeg.hip, person.leftArm.shoulder);

            upPose = new PersonPose(person)
                    .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, (int) MINIMAL_HIP_ANGLE, 360);


            downPose = new PersonPose(person)
                    .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, (int) MINIMAL_DOWN_HIP_ANGLE, (int) MAXIMAL_DOWN_HIP_ANGLE);
        }

    }

    private void initRight() {
        //head right side, so left side of the body is down.
        //testcases
        if (exerciseSettings.isBackCameraUsed()) {
            sidePlankPose = new PersonPose(person)
                    .below(person.leftArm.wrist, person.leftArm.shoulder)
                    .below(person.leftLeg.heel, person.leftArm.shoulder)
                    .below(person.leftArm.shoulder, person.rightArm.shoulder)
                    .below(person.leftLeg.hip, person.rightLeg.hip)
                    .below(person.leftArm.elbow, person.leftArm.shoulder)
                    .below(person.leftLeg.hip, person.rightArm.wrist)
                    .leftOf(person.leftLeg.heel, person.leftLeg.ankle)
                    .leftOf(person.leftLeg.heel, person.leftLeg.knee)
                    .leftOf(person.leftLeg.knee, person.leftLeg.hip)
                    .leftOf(person.leftLeg.hip, person.leftArm.shoulder);

            upPose = new PersonPose(person)
                    .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, (int) MINIMAL_HIP_ANGLE, 360);

            downPose = new PersonPose(person)
                    .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, (int) MINIMAL_DOWN_HIP_ANGLE, (int) MAXIMAL_DOWN_HIP_ANGLE);

        } else {
            sidePlankPose = new PersonPose(person)
                    .below(person.rightArm.wrist, person.rightArm.shoulder)
                    .below(person.rightLeg.heel, person.rightArm.shoulder)
                    .below(person.rightArm.shoulder, person.leftArm.shoulder)
                    .below(person.rightLeg.hip, person.leftLeg.hip)
                    .below(person.rightLeg.hip, person.leftArm.wrist)
                    .below(person.rightArm.elbow, person.rightArm.shoulder)
                    .leftOf(person.rightLeg.heel, person.rightLeg.ankle)
                    .leftOf(person.rightLeg.heel, person.rightLeg.knee)
                    .leftOf(person.rightLeg.knee, person.rightLeg.hip)
                    .leftOf(person.rightLeg.hip, person.rightArm.shoulder);

            upPose = new PersonPose(person)
                    .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, (int) MINIMAL_HIP_ANGLE, 360);

            downPose = new PersonPose(person)
                    .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, (int) MINIMAL_DOWN_HIP_ANGLE, (int) MAXIMAL_DOWN_HIP_ANGLE);
        }


    }

    protected void processExercise(InfoBlob infoBlob) {

        double shoulderY = SMath.getMeanY(person.rightArm.shoulder, person.leftArm.shoulder);
        double shoulderX = SMath.getMeanX(person.rightArm.shoulder, person.leftArm.shoulder);
        double heelY = SMath.getMeanY(person.rightLeg.heel, person.leftLeg.heel);
        double heelX = SMath.getMeanX(person.rightLeg.heel, person.leftLeg.heel);

        double slopeShoulderKnee = SMath.calculateSlopeWithXAxis(shoulderX, heelX, shoulderY, heelY);

        if (exerciseSettings.isBackCameraUsed())
            slopeValue = slopeShoulderKnee < MINIMAL_SLOPE_BACK_CAMERA_VALUE;
        else
            slopeValue = slopeShoulderKnee < MINIMAL_SLOPE_VALUE;


        switch (motion) {
            case UP:
                if (slopeValue && sidePlankPose.match() && upPose.match()) {
                    incrementScore();
                    motion = MOTION.DOWN;
                }
                break;
            case DOWN:
                if (slopeValue && sidePlankPose.match() && downPose.match()) {
                    motion = MOTION.UP;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }


    }

    @Override
    public String toString() {
        return "sidePlankDipsExercise{" +
                "Motion=" + motion +
                ", score=" + score.getCount() +
                '}';
    }

    @Override
    public String getName() {
        return "side-plank-dips";
    }

    enum MOTION {
        UP,
        DOWN
    }


}

