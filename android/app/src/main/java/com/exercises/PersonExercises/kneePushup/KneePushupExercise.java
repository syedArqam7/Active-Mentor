package com.exercises.PersonExercises.kneePushup;


import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class KneePushupExercise extends AbstractPersonExercise {

    private MOTION Motion = MOTION.DOWN;
    private double kneeY;

    private ObjectDetection shoulder;
    private ObjectDetection knee;
    private static final int DOWN_MIN_ANGLE = 1;
    private static final int DOWN_MAX_ANGLE = 20;
    private static final int UP_MAX_ANGLE = 80;
    private static final int UP_MIN_ANGLE = 40;

    private PersonPose poseUp, poseDown;

    public KneePushupExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.knee_push_up_calibration_outlines, R.raw.knee_push_up_calibration_correct, R.raw.knee_push_up_calibration_calibrate), exerciseSettings);
        calibration.setCalibrationSize(0.2f, 1, 0.1f, 0.9f);
    }

    @Override
    protected void initExercise() {

        // consider lowest body points
        shoulder = person.leftArm.shoulder.getY() > person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;
        knee = person.leftLeg.knee.getY() > person.rightLeg.knee.getY() ? person.leftLeg.knee : person.rightLeg.knee;

        poseDown = new PersonPose(person)
                .angleXaxis(shoulder, knee, DOWN_MIN_ANGLE, DOWN_MAX_ANGLE);
        poseUp = new PersonPose(person)
                .angleXaxis(shoulder, knee, UP_MIN_ANGLE, UP_MAX_ANGLE);

    }

    @Override
    protected void drawExercise(Canvas canvas) {
      //can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {

        // uncomment when required or during debugging.

       // drawTextDebug(canvas, "GO " + Motion);

//        canvas.drawLine(
//                shoulder.getDetectedLocation().getXf() * canvas.getWidth(),
//                shoulder.getDetectedLocation().getYf() * canvas.getHeight(),
//                knee.getDetectedLocation().getXf() * canvas.getWidth(),
//                knee.getDetectedLocation().getYf() * canvas.getHeight(),
//                new JOGOPaint().blue().largeStroke()
//        );


        poseDown.drawDebug(canvas);
        poseUp.drawDebug(canvas);

    }

    private boolean isKneeDown() {
        double elbowY = SMath.getMinY(person.rightArm.elbow, person.leftArm.elbow);
        return (kneeY > elbowY);
    }

    protected void processExercise(InfoBlob infoBlob) {

        kneeY = SMath.getMeanY(person.rightLeg.knee, person.leftLeg.knee);

        switch (Motion) {
            //switch for sequence of Motions (Down then Up)
            case DOWN:
                if (poseDown.match()) {
                    Motion = MOTION.UP;
                    render.up();
                }
                break;
            case UP:
                if (poseUp.match()) {
                    Motion = MOTION.DOWN;
                    if (isKneeDown()) {
                        incrementScore();
                    }
                }
                break;
        }
    }

    @Override
    public String toString() {
        return "KneePushUpExercise{" +
                "Motion=" + Motion +
                '}';
    }

    @Override
    public String getName() {
        return "knee-pushups";
    }

    enum MOTION {
        UP,
        DOWN
    }


}

