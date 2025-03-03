package com.exercises.PersonExercises.pushUp;


import android.graphics.Canvas;
import android.os.SystemClock;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.location.DetectionLocation;
import com.render.lottie.LottieCalibration;
import com.render.sounds.SoundRender;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class PushUpExercise extends AbstractPersonExercise {

    static final int RESET_LIMIT = 4;
    private static final double RADIUS = 0.1;
    DetectionLocation kneeR, hipR, ankleR, kneeL, hipL, ankleL;
    int resetChance = 0;
    JOGOPaint timerPaint = new JOGOPaint().activeOrange().stroke().strokeWidth(30);
    private MOTION Motion = MOTION.DOWN;
    private MODE Mode;
    private double shoulderY, shoulderX, ankleX, ankleY;
    private double slopeShoulderAnkle;
    private static final double slopeDownUpperLimit = 0.25;
    private static final double slopeDownLowerLimit = 0.05;
    private static final double slopeUp = 0.40;
    private static final double MAX_BENT_ALLOWED = 140;
    private static final long PAUSED_TIME = 2000;
    private long pausedTime = 0;


    public PushUpExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.push_up_calibration_outlines, R.raw.push_up_calibration_correct, R.raw.push_up_calibration_calibrate), exerciseSettings);
        calibration.setCalibrationSize(0.1f, 1, 0.1f, 0.9f);
        calibration.setCorrectLottiePosition(0.5, 0.47);
    }

    @Override
    protected void initExercise() {
        switch (exerciseSettings.getExerciseVariation()) {
            case 0:
                Mode = MODE.NORMAL;
                break;
            case 1:
                Mode = MODE.PAUSED;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + exerciseSettings.getExerciseVariation());
        }
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        if (Mode == MODE.PAUSED) {
            // timer circle
            if (pausedTime == 0)
                canvas.drawCircle((float) (canvas.getWidth() * 0.5),
                        (float) (canvas.getHeight() * 0.2),
                        (float) (canvas.getHeight() * RADIUS),
                        timerPaint);
            else
                canvas.drawArc((float) (canvas.getWidth() * 0.5 - canvas.getHeight() * RADIUS),
                        (float) (canvas.getHeight() * 0.2 - canvas.getHeight() * RADIUS),
                        (float) (canvas.getWidth() * 0.5 + canvas.getHeight() * RADIUS),
                        (float) (canvas.getHeight() * 0.2 + canvas.getHeight() * RADIUS),
                        270,
                        360 * (float) (Math.max(percentageOfFinalDraw(), 0)),
                        false,
                        timerPaint);

        }
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {

        if (pausedTime > 0 && Mode == MODE.PAUSED) {
            drawTextLargeDebug(canvas, "PausedTimer: " + (pausedTime - SystemClock.elapsedRealtime()) / 1000 + "s");
        }

        drawTextDebug(canvas, "slopeShoulderAnkle: " + slopeShoulderAnkle);
        drawTextLargeDebug(canvas, "GO " + Motion);

//        canvas.drawText("Mode " + Mode, 50, JOGOPaint.getNewDrawDebugHeight() + 200, new JOGOPaint().blue().large());
//        canvas.drawText("R " + kneeBentR, 150, JOGOPaint.getNewDrawDebugHeight() + 200, new JOGOPaint().red().large());
//        canvas.drawText("L " + kneeBentL, 150, JOGOPaint.getNewDrawDebugHeight() + 400, new JOGOPaint().red().large());

        //Person's slope line
        canvas.drawLine(
                (float) shoulderX * canvas.getWidth(),
                (float) shoulderY * canvas.getHeight(),
                (float) ankleX * canvas.getWidth(),
                (float) ankleY * canvas.getHeight(),
                new JOGOPaint().blue().largeStroke()
        );
        //Horizontal line
        canvas.drawLine(
                (float) ankleX * canvas.getWidth(),
                (float) ankleY * canvas.getHeight(),
                (float) shoulderX * canvas.getWidth(),
                (float) ankleY * canvas.getHeight(),
                new JOGOPaint().blue().largeStroke()
        );
        //Up line
        double diffX = (shoulderX > ankleX) ? (ankleX - shoulderX) : (shoulderX - ankleX);
        canvas.drawLine(
                (float) ankleX * canvas.getWidth(),
                (float) ankleY * canvas.getHeight(),
                (float) shoulderX * canvas.getWidth(),
                (float) (ankleY + slopeUp * (diffX)) * canvas.getHeight(),
                new JOGOPaint().red().largeStroke()
        );
        //Down line
        canvas.drawLine(
                (float) ankleX * canvas.getWidth(),
                (float) ankleY * canvas.getHeight(),
                (float) shoulderX * canvas.getWidth(),
                (float) (ankleY + slopeDownUpperLimit * (diffX)) * canvas.getHeight(),
                new JOGOPaint().red().largeStroke()
        );
        drawTextLargeDebug(canvas, "" + (Math.round(Math.toDegrees(Math.atan(slopeShoulderAnkle)) * 100.0) / 100.0));

    }

    public double percentageOfFinalDraw() {
        /*
         * This method only concern with draw logic, not timer logic.
         * */

        return (pausedTime - SystemClock.elapsedRealtime()) / (double) PAUSED_TIME;
    }

    private boolean isKneeAngleFine() {
        hipR = person.rightLeg.hip.getDetectedLocation();
        kneeR = person.rightLeg.knee.getDetectedLocation();
        ankleR = person.rightLeg.ankle.getDetectedLocation();

        hipL = person.leftLeg.hip.getDetectedLocation();
        kneeL = person.leftLeg.knee.getDetectedLocation();
        ankleL = person.leftLeg.ankle.getDetectedLocation();

        double kneeBentR = SMath.calculateAngle3Points(hipR, kneeR, ankleR, false);
        double kneeBentL = SMath.calculateAngle3Points(hipL, kneeL, ankleL, false);

        return (kneeBentR >= MAX_BENT_ALLOWED && kneeBentL >= MAX_BENT_ALLOWED);
    }

    protected void processExercise(InfoBlob infoBlob) {

        shoulderY = SMath.getMeanY(person.rightArm.shoulder.getDetectedLocation(), person.leftArm.shoulder.getDetectedLocation());
        shoulderY = SMath.getMeanY(person.rightArm.shoulder.getDetectedLocation(), person.leftArm.shoulder.getDetectedLocation());
        shoulderX = SMath.getMeanX(person.rightArm.shoulder.getDetectedLocation(), person.leftArm.shoulder.getDetectedLocation());

        ankleY = SMath.getMeanY(person.rightLeg.ankle.getDetectedLocation(), person.leftLeg.ankle.getDetectedLocation());

        ankleX = SMath.getMeanX(person.rightLeg.ankle.getDetectedLocation(), person.leftLeg.ankle.getDetectedLocation());

        slopeShoulderAnkle = SMath.calculateSlopeWithXAxis(shoulderX, ankleX, shoulderY, ankleY);

        switch (Mode) {
            case NORMAL:
                processNormal();
                break;
            case PAUSED:
                processPaused();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Mode);
        }

    }

    private void processNormal() {

        switch (Motion) {
            //switch for sequence of Motions (Down then Up)
            case DOWN:
                if (slopeShoulderAnkle <= slopeDownUpperLimit) {
                    Motion = MOTION.UP;
                    render.up();
                }
                break;
            case UP:
                if (!isKneeAngleFine()) {
                    Motion = MOTION.DOWN;
                } else if (slopeShoulderAnkle >= slopeUp) {
                    Motion = MOTION.DOWN;
                    incrementScore();
                    render.down();
                }

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Motion);
        }
    }

    private void processPaused() {

        switch (Motion) {
            //switch for sequence of Motions (Down then Up)
            case DOWN:
                if (slopeShoulderAnkle <= slopeDownUpperLimit && slopeShoulderAnkle > slopeDownLowerLimit) {
                    // start the timer if 0.
                    if (pausedTime == 0) {
                        pausedTime = SystemClock.elapsedRealtime() + PAUSED_TIME;
                        break;
                    }

                    // if PAUSED Time limit is passed then change the motion.
                    if ((pausedTime - SystemClock.elapsedRealtime()) <= 0) {
                        Motion = MOTION.UP;
                        if (score.isRunning()) incrementScore();
                        render.up();
                        new SoundRender(R.raw.go_up).play();
                    }

                } else {
                    //a mistake is made
                    resetChance++;
                    if (resetChance >= RESET_LIMIT) { // if down angle is disturbed RESET_LIMIT times then reset the timer.
                        pausedTime = 0;
                    }
                }
                // reset paused timer
                break;
            case UP:
                if (slopeShoulderAnkle >= slopeUp) {
                    Motion = MOTION.DOWN;
                    new SoundRender(R.raw.go_down).play();
                    render.down();
                    pausedTime = 0;
                    resetChance = 0;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Motion);
        }
    }

    @Override
    public String toString() {
        return "PushUpExercise{" +
                "Motion=" + Motion +
                ", slopeShoulderAnkle=" + slopeShoulderAnkle +
                '}';
    }

    @Override
    public String getName() {
        return "pushups";
    }


    enum MOTION {
        UP,
        DOWN
    }

    enum MODE {
        NORMAL,
        PAUSED
    }


}

