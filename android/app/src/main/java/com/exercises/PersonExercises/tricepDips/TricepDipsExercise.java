package com.exercises.PersonExercises.tricepDips;

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

public class TricepDipsExercise extends AbstractPersonExercise {
    Pose upPose, downPose;
    private ObjectDetection hip;
    private ObjectDetection shoulder;
    private ObjectDetection elbow;
    private static final int ARMS_ANGLE_UP_HIGH_LIMIT = 185;
    private static final int ARMS_ANGLE_UP_LOW_LIMIT = 140;
    private static final int ARMS_ANGLE_DOWN_HIGH_LIMIT = 120;
    private static final int ARMS_ANGLE_DOWN_LOW_LIMIT = 80;
    private static final int SHK_ANGLE_UP_HIGH_LIMIT = 130;
    private static final int SHK_ANGLE_UP_LOW_LIMIT = 90;
    private static final int SHK_ANGLE_DOWN_HIGH_LIMIT = 75;
    private static final int SHK_ANGLE_DOWN_LOW_LIMIT = 50;
    private MOTION motion = MOTION.DOWN;
    private MODE mode;


    public TricepDipsExercise(ExerciseSettings settings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), settings);
        calibration.setCalibrationSize(0.15f, 1, 0.1f, 0.9f);
    }

    @Override
    protected void initExercise() {

        initSide();

        ObjectDetection wrist;
        ObjectDetection knee;
        switch (mode) {
            case LEFT:
                shoulder = person.leftArm.shoulder;
                elbow = person.leftArm.elbow;
                wrist = person.leftArm.wrist;
                hip = person.leftLeg.hip;
                knee = person.leftLeg.knee;
                break;
            case RIGHT:
                shoulder = person.rightArm.shoulder;
                elbow = person.rightArm.elbow;
                wrist = person.rightArm.wrist;
                hip = person.rightLeg.hip;
                knee = person.rightLeg.knee;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }


        upPose = new PersonPose(person).angle(shoulder, elbow, wrist, ARMS_ANGLE_UP_LOW_LIMIT, ARMS_ANGLE_UP_HIGH_LIMIT)
                .angle(shoulder, hip, knee, SHK_ANGLE_UP_LOW_LIMIT, SHK_ANGLE_UP_HIGH_LIMIT);

        downPose = new PersonPose(person).angle(shoulder, elbow, wrist, ARMS_ANGLE_DOWN_LOW_LIMIT, ARMS_ANGLE_DOWN_HIGH_LIMIT)
                .angle(shoulder, hip, knee, SHK_ANGLE_DOWN_LOW_LIMIT, SHK_ANGLE_DOWN_HIGH_LIMIT);

    }

    void initSide() {
        if (person.face.nose.getX() > person.leftLeg.ankle.getX()
                && person.face.nose.getX() > person.rightLeg.ankle.getX())
            mode = MODE.RIGHT;
        else
            mode = MODE.LEFT;
    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        if (hip == null || shoulder == null || elbow == null) return;
        drawTextDebug(canvas, "Score: " + score.getCount());
        drawTextLargeDebug(canvas, "GO " + motion);

        upPose.drawDebug(canvas);
        downPose.drawDebug(canvas);

    }

    protected void processExercise(InfoBlob infoBlob) {
        processSideLegRaise();
    }

    private void processSideLegRaise() {
        switch (motion) {
            case UP:
                if (upPose.match()) {
                    motion = MOTION.DOWN;
                    incrementScore();
                }
                break;
            case DOWN:
                if (downPose.match()) {
                    motion = MOTION.UP;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "tricep-dips";
    }

    enum MOTION {
        UP, DOWN
    }


    enum MODE {
        LEFT, RIGHT
    }
}

