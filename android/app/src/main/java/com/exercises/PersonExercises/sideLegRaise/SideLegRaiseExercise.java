package com.exercises.PersonExercises.sideLegRaise;

import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class SideLegRaiseExercise extends AbstractPersonExercise {
    Pose upPose, downPose;
    MODE mode;
    private ObjectDetection hip;
    private ObjectDetection leftAnkle;
    private ObjectDetection rightAnkle;
    private static final int LEGS_ANGLE_UPPER_LIMIT = 65;
    private static final int LEGS_ANGLE_LOWER_LIMIT = 15;
    private MOTION motion = MOTION.UP;

    public SideLegRaiseExercise(ExerciseSettings settings) {
        super(
                new LottieCalibration(R.raw.side_leg_raise_calibration_outlines, R.raw.side_leg_raise_calibration_correct, R.raw.side_leg_raise_calibration_calibrate,
                        settings.getExerciseVariation() == 0), settings);
        calibration.setCalibrationSize(0.15f, 1, 0.1f, 0.9f);
        calibration.setKeepBitmapAspectRation(false);
    }

    @Override
    protected void initExercise() {

        // get highest hip for the calculation of angles
        hip = person.leftLeg.hip.getY() < person.rightLeg.hip.getY() ? person.leftLeg.hip : person.rightLeg.hip;

        // get ankles location
        leftAnkle = person.leftLeg.ankle;
        rightAnkle = person.rightLeg.ankle;

        if (exerciseSettings.getExerciseVariation() == 0) {
            mode = MODE.LEFT;
            upPose = new PersonPose(person).angle(leftAnkle, hip, rightAnkle, LEGS_ANGLE_UPPER_LIMIT, 170).isBackFlat().isPersonLayingFlat();
            downPose = new PersonPose(person).angle(leftAnkle, hip, rightAnkle, 0, LEGS_ANGLE_LOWER_LIMIT).isBackFlat().isPersonLayingFlat();
        } else {
            mode = MODE.RIGHT;
            upPose = new PersonPose(person).angle(rightAnkle, hip, leftAnkle, LEGS_ANGLE_UPPER_LIMIT, 170).isBackFlat().isPersonLayingFlat();
            downPose = new PersonPose(person).angle(rightAnkle, hip, leftAnkle, 0, LEGS_ANGLE_LOWER_LIMIT).isBackFlat().isPersonLayingFlat();
        }


    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        if (hip == null || leftAnkle == null || rightAnkle == null) return;
//        drawTextDebug(canvas, "Score: " + score.getCount());
//        drawTextDebug(canvas, "Score: " + score.getCount());
//
//        upPose.drawDebug(canvas);
//        downPose.drawDebug(canvas);
    }

    protected void processExercise(InfoBlob infoBlob) {
        processSideLegRaise();
    }

    private void processSideLegRaise() {
        switch (motion) {
            case UP:
                if (upPose.match())
                    motion = MOTION.DOWN;

                break;
            case DOWN:
                if (downPose.match()) {
                    motion = MOTION.UP;
                    incrementScore();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "side-leg-raises";
    }

    enum MOTION {
        UP, DOWN
    }


    enum MODE {
        LEFT, RIGHT
    }
}

