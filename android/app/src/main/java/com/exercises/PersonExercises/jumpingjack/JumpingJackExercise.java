package com.exercises.PersonExercises.jumpingjack;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class JumpingJackExercise extends AbstractPersonExercise {

    /*
    the following variables are initialized experimentally and also act as conditional variables
     */
    private static final int shoulderHigh = 95;
    private static final int shoulderLow = 50;
    private static final int legHigh = 4;
    private static final int legLow = 4;
    Motion motion = Motion.UP; // acts as a conditional variable
    MODE mode;
    private Pose upPose;
    private Pose downPose;

    public JumpingJackExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
    }

    @Override
    protected void initExercise() {
        switch (exerciseSettings.getExerciseVariation()) {
            case 0:
                mode = MODE.NORMAL;
                upPose = new PersonPose(person).minShoulderAngle(shoulderHigh).minLegAngle(legHigh);
                downPose = new PersonPose(person).maxArmAngle(shoulderHigh).maxLegAngle(legHigh);
                break;
            case 1:
                mode = MODE.INVERSE;
                upPose = new PersonPose(person).minShoulderAngle(shoulderHigh).maxLegAngle(legHigh);
                downPose = new PersonPose(person).maxArmAngle(shoulderHigh).minLegAngle(legHigh);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + exerciseSettings.getExerciseVariation());
        }
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        upPose.draw(canvas);
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
        upPose.drawDebug(canvas);
        downPose.drawDebug(canvas);

//        drawTextLargeDebug(canvas, "Motion: " + motion);
    }

    protected void processExercise(InfoBlob infoBlob) {
        switch (motion) {
            case UP:
                if (!upPose.match()) return;
                incrementScore();
                motion = Motion.DOWN;
                break;
            case DOWN:
                if (!downPose.match()) return;
                motion = Motion.UP;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String toString() {
        return "JumpingJackExercise{" +
                "motion=" + motion +
                ", shoulderHigh=" + shoulderHigh +
                ", shoulderLow=" + shoulderLow +
                ", legHigh=" + legHigh +
                ", legLow=" + legLow +
                '}';
    }

    @Override
    public String getName() {
        return "jumping-jacks";
    }


    enum Motion {
        UP,
        DOWN
    }

    enum MODE {
        NORMAL,
        INVERSE
    }


}
