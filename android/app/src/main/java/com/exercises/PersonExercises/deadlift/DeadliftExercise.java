package com.exercises.PersonExercises.deadlift;

import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.calibration.Calibration;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.logger.SLOG;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.Gear;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class DeadliftExercise extends AbstractPersonExercise {
    private static final int SHK_ANGLE_LIMIT = 90;
    private static final int HKA_ANGLE_LIMIT = 155;
    private Pose standingPose, downPose;
    private MOTION motion = MOTION.DOWN;

    // Gear Initializers
    {
        Gear.setIff("DrawExerciseDebug", true);
    }

    public DeadliftExercise(ExerciseSettings settings) {
        //we set the topscreen to 0.3, to deal with the questions probably need a better way to deal with it later...
        super(new LottieCalibration(R.raw.deadlift_calibration_outlines, R.raw.deadlift_calibration_correct, R.raw.deadlift_calibration_calibrate), settings);
        calibration.setCalibrationSize(0.2f, Calibration.BOTTOM_SCREEN, Calibration.LEFT_SCREEN, Calibration.RIGHT_SCREEN);
    }

    @Override
    protected void initExercise() {
        // get highest body parts for the calculation of angles
        ObjectDetection shoulder = person.leftArm.shoulder.getY() < person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;
        ObjectDetection hip = person.leftLeg.hip.getY() < person.rightLeg.hip.getY() ? person.leftLeg.hip : person.rightLeg.hip;
        ObjectDetection knee = person.leftLeg.knee.getY() < person.rightLeg.knee.getY() ? person.leftLeg.knee : person.rightLeg.knee;
        ObjectDetection ankle = person.leftLeg.ankle.getY() < person.rightLeg.ankle.getY() ? person.leftLeg.ankle : person.rightLeg.ankle;

        downPose = new PersonPose(person).angle(shoulder, hip, knee, 0, SHK_ANGLE_LIMIT)
                .angle(hip, knee, ankle, 90, HKA_ANGLE_LIMIT);
        standingPose = new PersonPose(person).isBodyUpright();

    }

    @Override
    protected void drawExercise(Canvas canvas) {
        //can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        drawTextDebug(canvas, "Score: " + score.getCount());
//
//        if (downPose != null && standingPose != null) {
//            downPose.drawDebug(canvas);
//            standingPose.drawDebug(canvas);
//        }
//
//        drawTextLargeDebug(canvas, "GO " + motion);
    }

    protected void processExercise(InfoBlob infoBlob) {
        switch (motion) {
            case DOWN:
                if (downPose.match() && !standingPose.match()) {
                    motion = MOTION.UP;
                    render.up();
                }
                break;
            case UP:
                if (standingPose.match()) {
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
        return "deadlift";
    }


    enum MOTION {
        UP, DOWN
    }
}

