package com.exercises.PersonExercises.lunges;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class LungesExercise extends AbstractPersonExercise {

    Motion motion = Motion.DOWN;
    MODE mode;
    LEG leg = LEG.ANY;

    private Pose upPose;
    private Pose leftDownPose;
    private Pose rightDownPose;

    public LungesExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
    }

    @Override
    protected void initExercise() {

        upPose = new PersonPose(person)
                .isBodyUpright()
                .isStandingStraight();

        switch (exerciseSettings.getExerciseVariation()) {
            case 0:
                mode = MODE.NORMAL;
                leftDownPose = new PersonPose(person)
                        .minSingleLegAngle(17)
                        .maxSingleLegAngle(40)
                        .angleXaxis(person.rightLeg.hip, person.rightLeg.knee, 70, 110)
                        .minSingleKneeAngle(50)
                        .maxSingleKneeAngle(110)
                        .below(person.leftLeg.knee, person.leftLeg.hip)
                        .below(person.leftLeg.ankle, person.getGroundLine() - 0.03)
                        .below(person.rightLeg.ankle, person.getGroundLine() - 0.03);
                rightDownPose = new PersonPose(person)
                        .minSingleLegAngle(17)
                        .maxSingleLegAngle(40)
                        .angleXaxis(person.leftLeg.hip, person.leftLeg.knee, 70, 110)
                        .minSingleKneeAngle(50)
                        .maxSingleKneeAngle(110)
                        .below(person.rightLeg.knee, person.rightLeg.hip)
                        .below(person.leftLeg.ankle, person.getGroundLine() - 0.03)
                        .below(person.rightLeg.ankle, person.getGroundLine() - 0.03);
                break;
            case 1:
                mode = MODE.LATERAL;
                rightDownPose = new PersonPose(person)
                        .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.knee, 100, 155)
                        .angleXaxis(person.leftLeg.knee, person.leftLeg.ankle, 50, 73)
                        .angle(person.rightLeg.hip, person.rightLeg.knee, person.rightLeg.ankle, 111, 155)
                        .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.knee, 100, 156)
                        .rightLegCloserToHips()
                        .below(person.rightLeg.knee, person.rightLeg.hip);
                leftDownPose = new PersonPose(person)
                        .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.knee, 100, 155)
                        .angleXaxis(person.rightLeg.knee, person.rightLeg.ankle, 50, 73)
                        .angle(person.leftLeg.hip, person.leftLeg.knee, person.leftLeg.ankle, 111, 155)
                        .angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.knee, 100, 156)
                        .leftLegCloserToHips()
                        .below(person.leftLeg.knee, person.leftLeg.hip);
                break;
            case 2:
                mode = MODE.CURTSY;
                rightDownPose = new PersonPose(person)
                        .rightOf(person.leftLeg.ankle, person.rightLeg.ankle)
                        .rightOf(person.leftLeg.knee, person.rightLeg.knee)
                        .angle(person.rightLeg.hip, person.rightLeg.knee, person.rightLeg.ankle, 90, 165)
                        .angle(person.leftLeg.hip, person.leftLeg.knee, person.leftLeg.ankle, 120, 180);

                leftDownPose = new PersonPose(person)
                        .leftOf(person.rightLeg.ankle, person.leftLeg.ankle)
                        .leftOf(person.rightLeg.knee, person.leftLeg.knee)
                        .angle(person.leftLeg.hip, person.leftLeg.knee, person.leftLeg.ankle, 90, 165)
                        .angle(person.rightLeg.hip, person.rightLeg.knee, person.rightLeg.ankle, 120, 180);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + exerciseSettings.getExerciseVariation());
        }
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        // class abstract method implementation
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        person.drawDebug(canvas);
        drawTextDebug(canvas, exerciseSettings.getExerciseVariation() + "->" + motion + "->" + leg);
        switch (motion) {
            case UP:
                upPose.drawDebug(canvas);
                break;
            case DOWN:
                switch (leg) {
                    case LEFT:
                        leftDownPose.drawDebug(canvas);
                        break;
                    case RIGHT:
                        rightDownPose.drawDebug(canvas);
                        break;
                    case ANY:
                        rightDownPose.drawDebug(canvas);
                        leftDownPose.drawDebug(canvas);
                        break;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }

    }

    protected void processExercise(InfoBlob infoBlob) {
        switch (motion) {
            case UP:
                if (upPose.match()) {
                    motion = Motion.DOWN;
                    incrementScore();
                }
                break;
            case DOWN:
                switch (leg) {
                    case ANY:
                        if (leftDownPose.match()) {
                            leg = LEG.RIGHT;
                            motion = Motion.UP;
                        }
                        if (rightDownPose.match()) {
                            leg = LEG.LEFT;
                            motion = Motion.UP;
                        }
                        break;
                    case LEFT:
                        if (leftDownPose.match()) {
                            leg = LEG.RIGHT;
                            motion = Motion.UP;
                        }
                        break;
                    case RIGHT:
                        if (rightDownPose.match()) {
                            leg = LEG.LEFT;
                            motion = Motion.UP;
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + leg);
                }
            default:
                break;
        }

    }

    @Override
    public String toString() {
        return "Lunges{}";
    }

    @Override
    public String getName() {
        return "lunges";
    }


    enum MODE {
        NORMAL,
        LATERAL,
        CURTSY
    }


    enum Motion {
        UP,
        DOWN
    }

    enum LEG {
        LEFT,
        RIGHT,
        ANY
    }


}

