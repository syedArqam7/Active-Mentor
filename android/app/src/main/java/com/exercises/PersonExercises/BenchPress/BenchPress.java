package com.exercises.PersonExercises.BenchPress;

import static com.exercises.base.exercise.ExerciseActivity.render;

import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.calibration.Calibration;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.Gear;
import com.utils.InfoBlob;

public class BenchPress extends AbstractPersonExercise {
    private static final int ELBOW_ANGLE_LIMIT_START = 165; // Angle for almost fully extended arms
    private static final int ELBOW_ANGLE_LIMIT_END = 45; // Angle for the bottom position of the press
    private Pose startPose, endPose;
    private MOTION motion = MOTION.DOWN; // Consider renaming this to represent the phases of the bench press more accurately


    // Gear Initializers
    {
        Gear.setIff("DrawExerciseDebug", true);
    }

    public BenchPress(ExerciseSettings settings) {
        //we set the topscreen to 0.3, to deal with the questions probably need a better way to deal with it later...
        super(new LottieCalibration(R.raw.deadlift_calibration_outlines, R.raw.deadlift_calibration_correct, R.raw.deadlift_calibration_calibrate), settings);
        calibration.setCalibrationSize(0.2f, Calibration.BOTTOM_SCREEN, Calibration.LEFT_SCREEN, Calibration.RIGHT_SCREEN);
    }

    @Override
    protected void initExercise() {
        // Detect upper body parts for angle calculations
        ObjectDetection shoulder = person.leftArm.shoulder.getY() < person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;
        ObjectDetection elbow = person.leftArm.elbow.getY() < person.rightArm.elbow.getY() ? person.leftArm.elbow : person.rightArm.elbow;
        ObjectDetection wrist = person.leftArm.wrist.getY() < person.rightArm.wrist.getY() ? person.leftArm.wrist : person.rightArm.wrist;

        startPose = new PersonPose(person).angle(shoulder, elbow, wrist, ELBOW_ANGLE_LIMIT_START - 10, ELBOW_ANGLE_LIMIT_START);
        endPose = new PersonPose(person).angle(shoulder, elbow, wrist, ELBOW_ANGLE_LIMIT_END, ELBOW_ANGLE_LIMIT_END + 10);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        //can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
        drawTextDebug(canvas, "Score: " + score.getCount());

/*        if (downPose != null && standingPose != null) {
            downPose.drawDebug(canvas);
            standingPose.drawDebug(canvas);
        }*/

       drawTextLargeDebug(canvas, "GO " + motion);
    }

    protected void processExercise(InfoBlob infoBlob) {
        switch (motion) {
            case DOWN:
                if (endPose.match() && !startPose.match()) {
                    motion = MOTION.UP;
                    render.up(); // Update this method to reflect bench press motion
                }
                break;
            case UP:
                if (startPose.match()) {
                    motion = MOTION.DOWN;
                    incrementScore();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "benchpress";
    }

    enum MOTION {
        UP, DOWN
    }
}

