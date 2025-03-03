package com.exercises.PersonExercises.legRaise;


import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class LegRaiseExercise extends AbstractPersonExercise {

    private MOTION Motion = MOTION.UP;
    private double shoulderY, shoulderX, ankleX, ankleY, hipY, hipX;
    private double angleShoulderHip, angleHipAnkle;
    private static final double angleDown = 15;
    private static final double angleUp = 60;
    private static final double FLATNESS_ANGLE_LIMIT = 3;

    public LegRaiseExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.leg_raise_calibration_outlines, R.raw.leg_raise_calibration_correct, R.raw.leg_raise_calibration_calibrate), exerciseSettings);
        calibration.setCalibrationSize(0.1f, 1, 0.1f, 0.9f);
        calibration.setAlignTop(false);

    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        drawTextLargeDebug(canvas,"GO " + Motion);

        canvas.drawLine(
                (float) hipX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                (float) ankleX * canvas.getWidth(),
                (float) ankleY * canvas.getHeight(),
                new JOGOPaint().blue().largeStroke()
        );
        //Horizontal line
        canvas.drawLine(
                (float) hipX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                (float) ankleX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                new JOGOPaint().blue().largeStroke()
        );
        //Up line
        double diffX = (hipX > ankleX) ? (ankleX - hipX) : (hipX - ankleX);
        //double diffX = Math.sqrt(Math.pow((hipX - ankleX), 2) + Math.pow((hipY - ankleY), 2)) * -1;
        canvas.drawLine(
                (float) hipX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                (float) ankleX * canvas.getWidth(),
                (float) (hipY + (Math.tan(Math.toRadians(angleUp))) * (diffX)) * canvas.getHeight(),
                new JOGOPaint().red().largeStroke()
        );
        //Down line
        canvas.drawLine(
                (float) hipX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                (float) ankleX * canvas.getWidth(),
                (float) (hipY + Math.tan(Math.toRadians(angleDown)) * (diffX)) * canvas.getHeight(),
                new JOGOPaint().red().largeStroke()
        );
        drawTextLargeDebug(canvas, "angleHipAnkle: " + angleHipAnkle);
        //Horizontal Line Hip-Shoulder
        canvas.drawLine(
                (float) hipX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                (float) shoulderX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                new JOGOPaint().red().largeStroke()
        );
        //Line Hip-Shoulder
        canvas.drawLine(
                (float) hipX * canvas.getWidth(),
                (float) hipY * canvas.getHeight(),
                (float) shoulderX * canvas.getWidth(),
                (float) shoulderY * canvas.getHeight(),
                new JOGOPaint().blue().largeStroke()
        );
//        drawTextLargeDebug(canvas, "angleShoulderHip: " + angleShoulderHip);

    }

    private boolean isShoulderHipFine() {
        shoulderY = SMath.getMeanY(person.rightArm.shoulder, person.leftArm.shoulder);
        shoulderX = SMath.getMeanX(person.rightArm.shoulder, person.leftArm.shoulder);

        angleShoulderHip = SMath.calculateSlopeWithXAxis(shoulderX, hipX, shoulderY, hipY);

        return (angleShoulderHip <= FLATNESS_ANGLE_LIMIT);
    }

    protected void processExercise(InfoBlob infoBlob) {

        ankleY = SMath.getMeanY(person.rightLeg.ankle, person.leftLeg.ankle);
        ankleX = SMath.getMeanX(person.rightLeg.ankle, person.leftLeg.ankle);

        hipY = SMath.getMeanY(person.rightLeg.hip, person.leftLeg.hip);
        hipX = SMath.getMeanX(person.rightLeg.hip, person.leftLeg.hip);

        angleHipAnkle = SMath.calculateAngleWithXAxis(hipX, ankleX, hipY, ankleY);

        if (!isShoulderHipFine()) {
            Motion = MOTION.UP;
            return;
        }

        switch (Motion) {
            //switch for sequence of Motions (Down then Up)
            case DOWN:
                if (angleHipAnkle <= angleDown) {
                    Motion = MOTION.UP;
                    incrementScore();
                    render.up();
                }
                break;
            case UP:
                if (angleHipAnkle > angleUp) {
                    Motion = MOTION.DOWN;
                    if (score.isRunning()) render.down();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Motion);
        }
    }

    @Override
    public String toString() {
        return "LegRaiseExercise{" +
                "Motion=" + Motion +
                ", angleShoulderHip=" + angleShoulderHip +
                ", angleHipAnkle=" + angleHipAnkle +
                '}';
    }

    @Override
    public String getName() {
        return "leg-raises";
    }

    enum MOTION {
        UP,
        DOWN
    }


}

