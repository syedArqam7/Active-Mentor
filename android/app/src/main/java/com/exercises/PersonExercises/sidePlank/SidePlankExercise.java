package com.exercises.PersonExercises.sidePlank;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.SMath;

public class SidePlankExercise extends AbstractPersonExercise {

    Pose pose;
    LAYDOWN laydown;
    private double slopeShoulderKnee;
    private boolean aboveSlopeValue;
    private static final double MINIMAL_SLOPE_VALUE = 0.65;
    private static final double MINIMAL_SLOPE_BACK_CAMERA_VALUE = 0.85;
    private static final int MINIMAL_HIP_ANGLE = 168;
    private static final int MAXIMUM_HIP_ANGLE = 360;
    private static final int FRAMES_LOOK_BACK = 5;
    private int poseUnmatchedCounts = 0;


    private static final int SHOULDERHIGH = 85;
    private static final int SHOULDERLOW = 35;


    public SidePlankExercise(ExerciseSettings exerciseSettings) {
        super(
                new LottieCalibration(R.raw.side_plank_calibration_outlines, R.raw.side_plank_calibration_correct, R.raw.side_plank_calibration_calibrate,
                        exerciseSettings.getExerciseVariation() == 0),
                exerciseSettings);
        calibration.setCalibrationSize(0.1f, 1, 0.1f, 0.9f);
        calibration.setCorrectLottiePosition(0.47, 0.5);
    }

    @Override
    protected void initExercise() {
        switch (exerciseSettings.getExerciseVariation()) {
            case 1:
                laydown = LAYDOWN.Right;
                if (exerciseSettings.isBackCameraUsed()) {
                    pose = new PersonPose(person)
                            .below(person.leftArm.wrist, person.leftArm.shoulder)
                            .below(person.leftLeg.heel, person.leftArm.shoulder)
                            .below(person.leftArm.shoulder, person.rightArm.shoulder)
                            .below(person.leftLeg.hip, person.rightLeg.hip)
                            .below(person.leftArm.elbow, person.leftArm.shoulder)
                            .below(person.leftLeg.heel, person.leftLeg.hip)
                            .below(person.leftLeg.hip, person.rightArm.wrist)
                            .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, (int) MINIMAL_HIP_ANGLE, 360)
                            .leftOf(person.leftLeg.heel, person.leftLeg.ankle)
                            .leftOf(person.leftLeg.heel, person.leftLeg.knee)
                            .leftOf(person.leftLeg.knee, person.leftLeg.hip)
                            .leftOf(person.leftLeg.hip, person.leftArm.shoulder);
                } else {
                    pose = new PersonPose(person)
                            .below(person.rightArm.wrist, person.rightArm.shoulder)
                            .below(person.rightLeg.heel, person.rightArm.shoulder)
                            .below(person.rightArm.shoulder, person.leftArm.shoulder)
                            .below(person.rightLeg.hip, person.leftLeg.hip)
                            .below(person.rightLeg.hip, person.leftArm.wrist)
                            .below(person.rightArm.elbow, person.rightArm.shoulder)
                            .below(person.rightLeg.heel, person.rightLeg.hip)
                            .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, (int) MINIMAL_HIP_ANGLE, MAXIMUM_HIP_ANGLE)
                            .leftOf(person.rightLeg.heel, person.rightLeg.ankle)
                            .leftOf(person.rightLeg.heel, person.rightLeg.knee)
                            .leftOf(person.rightLeg.knee, person.rightLeg.hip)
                            .leftOf(person.rightLeg.hip, person.rightArm.shoulder);
                }
                break;
            case 0:
                laydown = LAYDOWN.Left;
                if (exerciseSettings.isBackCameraUsed()) {
                    pose = new PersonPose(person)
                            .below(person.rightArm.wrist, person.rightArm.shoulder)
                            .below(person.rightLeg.heel, person.rightArm.shoulder)
                            .below(person.rightArm.shoulder, person.leftArm.shoulder)
                            .below(person.rightLeg.hip, person.leftLeg.hip)
                            .below(person.rightLeg.hip, person.leftArm.wrist)
                            .below(person.rightArm.elbow, person.rightArm.shoulder)
                            .below(person.rightLeg.heel, person.rightLeg.hip)
                            .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, MINIMAL_HIP_ANGLE, MAXIMUM_HIP_ANGLE)
                            .rightOf(person.rightLeg.heel, person.rightLeg.ankle)
                            .rightOf(person.rightLeg.heel, person.rightLeg.knee)
                            .rightOf(person.rightLeg.knee, person.rightLeg.hip)
                            .rightOf(person.rightLeg.hip, person.rightArm.shoulder);
                } else {
                    pose = new PersonPose(person)
                            .below(person.leftArm.wrist, person.leftArm.shoulder)
                            .below(person.leftLeg.heel, person.leftArm.shoulder)
                            .below(person.leftArm.shoulder, person.rightArm.shoulder)
                            .below(person.leftLeg.hip, person.rightLeg.hip)
                            .below(person.leftLeg.hip, person.rightArm.wrist)
                            .below(person.leftArm.elbow, person.leftArm.shoulder)
                            .below(person.leftLeg.heel, person.leftLeg.hip)
                            .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, MINIMAL_HIP_ANGLE, MAXIMUM_HIP_ANGLE)
                            .rightOf(person.leftLeg.heel, person.leftLeg.ankle)
                            .rightOf(person.leftLeg.heel, person.leftLeg.knee)
                            .rightOf(person.leftLeg.knee, person.leftLeg.hip)
                            .rightOf(person.leftLeg.hip, person.leftArm.shoulder);
                }
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
        if (pose != null)
            pose.drawDebug(canvas);

    }

    protected void processExercise(InfoBlob infoBlob) {

        double shoulderY = SMath.getMeanY(person.rightArm.shoulder, person.leftArm.shoulder);
        double shoulderX = SMath.getMeanX(person.rightArm.shoulder, person.leftArm.shoulder);
        double heelY = SMath.getMeanY(person.rightLeg.heel, person.leftLeg.heel);
        double heelX = SMath.getMeanX(person.rightLeg.heel, person.leftLeg.heel);

        slopeShoulderKnee = SMath.calculateSlopeWithXAxis(shoulderX, heelX, shoulderY, heelY);

        aboveSlopeValue = slopeShoulderKnee < MINIMAL_SLOPE_VALUE;
        if (aboveSlopeValue && pose.match()) {
            score.continueTime();
            poseUnmatchedCounts = 0;
        } else if (poseUnmatchedCounts >= FRAMES_LOOK_BACK) {
            score.stopTime();
        } else {
            poseUnmatchedCounts++;
        }

    }

    @Override
    public String toString() {
        return "SidePlankExercise{" +
                "pose=" + pose +
                ", laydown=" + laydown +
                ", slopeShoulderKnee=" + slopeShoulderKnee +
                ", slopeValue=" + aboveSlopeValue +
                ", MINIMAL_SLOPE_VALUE=" + MINIMAL_SLOPE_VALUE +
                ", MINIMAL_SLOPE_BACK_CAMERA_VALUE=" + MINIMAL_SLOPE_BACK_CAMERA_VALUE +
                ", MINIMAL_HIP_ANGLE=" + MINIMAL_HIP_ANGLE +
                ", MAXIMUM_HIP_ANGLE=" + MAXIMUM_HIP_ANGLE +
                ", FRAMES_LOOK_BACK=" + FRAMES_LOOK_BACK +
                ", SHOULDERHIGH=" + SHOULDERHIGH +
                ", SHOULDERLOW=" + SHOULDERLOW +
                '}';
    }

    @Override
    public String getName() {
        return "side-plank";
    }

    enum LAYDOWN {
        Right,
        Left
    }


}
