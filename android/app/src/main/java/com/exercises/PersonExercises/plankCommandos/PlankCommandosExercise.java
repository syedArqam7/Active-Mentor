package com.exercises.PersonExercises.plankCommandos;

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

public class PlankCommandosExercise extends AbstractPersonExercise {

    protected int ELBOW_ANGLE_UP_MAX = 170;
    protected int ELBOW_ANGLE_UP_MIN = 140;
    protected int ELBOW_ANGLE_DOWN_MAX = 140;
    protected int ELBOW_ANGLE_DOWN_MIN = 80;
    protected int PLANK_ANGLE_MAX = 100;
    protected int PLANK_ANGLE_MIN = 60;
    protected int LEGS_ANGLE_MAX = 190;
    protected int LEGS_ANGLE_MIN = 150;
    MOTION motion = MOTION.ANY;
    MOTION lastMotion = MOTION.ANY;
    Pose leftUp, leftDown;
    Pose rightUp, rightDown;
    Pose plankPose;
    public PlankCommandosExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.push_up_calibration_outlines, R.raw.push_up_calibration_correct, R.raw.push_up_calibration_calibrate), exerciseSettings);
        calibration.setCalibrationSize(0.35f, 1, 0.1f, 0.9f);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {

        // uncomment when required or during debugging.

//        drawTextLargeDebug(canvas, "Do " + motion);
//        drawTextDebug(canvas, "score: " + score.getCount());
//        drawTextDebug(canvas,"last motion: " + lastMotion);

//        leftUp.drawDebug(canvas);
//        leftDown.drawDebug(canvas);
//
//        rightUp.drawDebug(canvas);
//        rightDown.drawDebug(canvas);
//
//        plankPose.drawDebug(canvas);

    }

    @Override
    protected void initExercise() {
        ObjectDetection elbow = person.leftArm.elbow.getY() > person.rightArm.elbow.getY() ? person.leftArm.elbow : person.rightArm.elbow;
        ObjectDetection shoulder = person.leftArm.shoulder.getY() > person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;
        ObjectDetection hip = person.leftLeg.hip.getY() > person.rightLeg.hip.getY() ? person.leftLeg.hip : person.rightLeg.hip;
        ObjectDetection knee = person.leftLeg.knee.getY() > person.rightLeg.knee.getY() ? person.leftLeg.knee : person.rightLeg.knee;
        ObjectDetection ankle = person.leftLeg.ankle.getY() > person.rightLeg.ankle.getY() ? person.leftLeg.ankle : person.rightLeg.ankle;

        leftUp = new PersonPose(person)
                .angle(person.leftArm.shoulder, person.leftArm.elbow, person.leftArm.wrist, ELBOW_ANGLE_UP_MIN, ELBOW_ANGLE_UP_MAX);
        leftDown = new PersonPose(person)
                .angle(person.leftArm.shoulder, person.leftArm.elbow, person.leftArm.wrist, ELBOW_ANGLE_DOWN_MIN, ELBOW_ANGLE_DOWN_MAX);

        rightUp = new PersonPose(person)
                .angle(person.rightArm.shoulder, person.rightArm.elbow, person.rightArm.wrist, ELBOW_ANGLE_UP_MIN, ELBOW_ANGLE_UP_MAX);
        rightDown = new PersonPose(person)
                .angle(person.rightArm.shoulder, person.rightArm.elbow, person.rightArm.wrist, ELBOW_ANGLE_DOWN_MIN, ELBOW_ANGLE_DOWN_MAX);

        plankPose = new PersonPose(person).isBackFlat()
                .angle(elbow, shoulder, hip, PLANK_ANGLE_MIN, PLANK_ANGLE_MAX)
                .angle(hip, knee, ankle, LEGS_ANGLE_MIN, LEGS_ANGLE_MAX)
                .above(shoulder, ankle);


    }

    protected void processExercise(InfoBlob infoBlob) {
        switch (motion) {
            case ANY:
                if (leftDown.match() && plankPose.match()) {
                    motion = MOTION.RIGHT_DOWN;
                    lastMotion = MOTION.LEFT_DOWN;
                    break;
                }
                if (rightDown.match() && plankPose.match()) {
                    motion = MOTION.LEFT_DOWN;
                    lastMotion = MOTION.RIGHT_DOWN;
                    break;
                }
                break;
            case LEFT_DOWN:
                if (leftDown.match() && plankPose.match()) {

                    if (lastMotion == MOTION.RIGHT_DOWN) {
                        lastMotion = motion;
                        motion = MOTION.RIGHT_UP;
                        //incrementScore();
                    }
                    if (lastMotion == MOTION.RIGHT_UP) {
                        lastMotion = motion;
                        motion = MOTION.RIGHT_DOWN;
                    }

                }

                break;
            case RIGHT_DOWN:
                if (rightDown.match() && plankPose.match()) {

                    if (lastMotion == MOTION.LEFT_DOWN) {
                        lastMotion = motion;
                        motion = MOTION.LEFT_UP;
                        // incrementScore();
                    }
                    if (lastMotion == MOTION.LEFT_UP) {
                        lastMotion = motion;
                        motion = MOTION.LEFT_DOWN;
                    }

                }
                break;
            case LEFT_UP:

                if (leftUp.match() && plankPose.match()) {

                    if (lastMotion == MOTION.RIGHT_UP) {
                        lastMotion = motion;
                        motion = MOTION.RIGHT_DOWN;
                        incrementScore();
                    }
                    if (lastMotion == MOTION.RIGHT_DOWN) {
                        lastMotion = motion;
                        motion = MOTION.RIGHT_UP;
                    }

                }
                break;
            case RIGHT_UP:

                if (rightUp.match() && plankPose.match()) {

                    if (lastMotion == MOTION.LEFT_UP) {
                        lastMotion = motion;
                        motion = MOTION.LEFT_DOWN;
                        incrementScore();

                    }
                    if (lastMotion == MOTION.LEFT_DOWN) {
                        lastMotion = motion;
                        motion = MOTION.LEFT_UP;
                    }

                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }

    }

    @Override
    public String toString() {
        return "PlankCommandosExercise{" +
                ", score=" + score.getCount() +
                '}';
    }

    @Override
    public String getName() {
        return "plank-commandos";
    }

    enum MOTION {
        ANY,
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN
    }


}
