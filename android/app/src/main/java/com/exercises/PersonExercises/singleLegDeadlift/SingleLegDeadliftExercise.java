package com.exercises.PersonExercises.singleLegDeadlift;

import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.calibration.Calibration;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class SingleLegDeadliftExercise extends AbstractPersonExercise {
    Pose leftPose, rightPose, standPose;
    private ObjectDetection leftHeel;
    private ObjectDetection rightHeel;
    private ObjectDetection hip;
    private static final int BACK_ANGLE_UPPER_LIMIT = 190;
    private static final int BACK_ANGLE_LOWER_LIMIT = 140;
    private static final int BACK_VERTICAL_ANGLE_UPPER_LIMIT = 90;
    private static final int BACK_VERTICAL_ANGLE_LOWER_LIMIT = 50;
    private MOTION motion = MOTION.ANY;
    private MOTION nextMotion = MOTION.ANY;
    public SingleLegDeadliftExercise(ExerciseSettings settings) {
        super(new LottieCalibration(R.raw.deadlift_calibration_outlines, R.raw.deadlift_calibration_correct, R.raw.deadlift_calibration_calibrate), settings);
        calibration.setCalibrationSize(0.1f, Calibration.BOTTOM_SCREEN, Calibration.LEFT_SCREEN, Calibration.RIGHT_SCREEN);
    }

    @Override
    protected void initExercise() {

        // get highest body elements for the calculation of angles
        hip = person.leftLeg.hip.getY() < person.rightLeg.hip.getY() ? person.leftLeg.hip : person.rightLeg.hip;
        ObjectDetection shoulder = person.leftArm.shoulder.getY() < person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;

        leftHeel = person.leftLeg.heel;
        rightHeel = person.rightLeg.heel;

        leftPose = new PersonPose(person).angle(leftHeel, hip, shoulder, BACK_ANGLE_LOWER_LIMIT, BACK_ANGLE_UPPER_LIMIT).
                angleYaxis(hip, shoulder, BACK_VERTICAL_ANGLE_LOWER_LIMIT, BACK_VERTICAL_ANGLE_UPPER_LIMIT);
        rightPose = new PersonPose(person).angle(rightHeel, hip, shoulder, BACK_ANGLE_LOWER_LIMIT, BACK_ANGLE_UPPER_LIMIT).
                angleYaxis(hip, shoulder, BACK_VERTICAL_ANGLE_LOWER_LIMIT, BACK_VERTICAL_ANGLE_UPPER_LIMIT);
        standPose = new PersonPose(person).isBodyUpright();

    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        if (hip == null || leftHeel == null || rightHeel == null || leftPose == null || rightPose == null)
//            return;
//
//        drawTextDebug(canvas, "Score: " + score.getCount());
//        drawTextLargeDebug(canvas, "Do " + motion);
//
//        leftPose.drawDebug(canvas);
//        rightPose.drawDebug(canvas);
//        standPose.drawDebug(canvas);

    }

    protected void processExercise(InfoBlob infoBlob) {
        switch (motion) {
            case ANY:
                if (leftPose.match()) {
                    motion = MOTION.STAND;
                    nextMotion = MOTION.RIGHT;
                    break;
                }

                if (rightPose.match()) {
                    motion = MOTION.STAND;
                    nextMotion = MOTION.LEFT;
                }

                break;
            case LEFT:
                if (leftPose.match()) {
                    motion = MOTION.STAND;
                    nextMotion = MOTION.RIGHT;

                    incrementScore();
                }
                break;

            case STAND:
                if (standPose.match()) {
                    motion = nextMotion;
                }
                break;
            case RIGHT:
                if (rightPose.match()) {
                    motion = MOTION.STAND;
                    nextMotion = MOTION.LEFT;

                    incrementScore();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "single-leg-deadlift";
    }


    enum MOTION {
        LEFT,
        RIGHT,
        STAND,
        ANY
    }
}

