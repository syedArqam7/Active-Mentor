package com.exercises.PersonExercises.highknee;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.location.DetectionLocation;
import com.render.lottie.LottieCalibration;
import com.utils.Gear;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;

public class HighKneeExercise extends AbstractPersonExercise {
    private static final long COOLDOWNTIME = 150;//slowdown posenet
    private double hipLine;
    private float HIPLINEPARAMETER = 0.45f;//scales hipline from knee (0) to hip (1)
    private long coolDownTime = 0;
    private double leftKneeAngle, rightKneeAngle;
    private KNEE knee = KNEE.ANY;

    // Gear Initializers
    {
//        Gear.LONG("cooldowntime",(l)->COOLDOWNTIME = l,COOLDOWNTIME);
        Gear.FLOAT("HIPLINEPARAMETER_INC", (l) -> HIPLINEPARAMETER += l, (HIPLINEPARAMETER));
        Gear.FLOAT("HIPLINEPARAMETER_DEC", (l) -> HIPLINEPARAMETER -= l, (HIPLINEPARAMETER));
        Gear.setIff("DrawExerciseDebug", true);
        Gear.setIff("drawExtra", true);
    }

    public HighKneeExercise(ExerciseSettings settings) {
        //we set the topscreen to 0.3, to deal with the questions probably need a better way to deal with it later...
        super(new LottieCalibration(R.raw.high_knees_calibration_outlines, R.raw.high_knees_calibration_correct, R.raw.high_knees_calibration_calibrate), settings);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        // class abstract method implementation
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
        drawTextDebug(canvas, "Score: " + score.getCount());
        drawTextDebug(canvas, "Knee:  " + knee);
       // drawTextLargeDebug(canvas, "Left: " + leftKneeAngle);
        //drawTextLargeDebug(canvas, "Right: " + rightKneeAngle);
//        drawTextDebug(canvas, "lextended:  " + person.leftArm.sidewaysExtended() + " lcosine:" + String.format("%.2f", person.leftArm.getSideWaysExtendedCosine()) +
//                "rextended:  " + person.rightArm.sidewaysExtended() + " rcosine:" + String.format("%.2f", person.rightArm.getSideWaysExtendedCosine()));
//
        canvas.drawLine(0, (float) hipLine * canvas.getHeight(), canvas.getWidth(), (float) hipLine * canvas.getHeight(), new JOGOPaint().blue().mediumStroke());
//
////         exercise gears
        if (Gear.iff("drawExtra")) drawExtraDebug(canvas);

    }

    public void drawExtraDebug(Canvas canvas) {
        drawTextDebug(canvas, "ExtraDebug:  ");
        drawTextDebug(canvas, "Hipline parameter:  " + HIPLINEPARAMETER);
        drawTextLargeDebug(canvas, "Left: " + leftKneeAngle);
        drawTextLargeDebug(canvas, "Right: " + rightKneeAngle);
        drawTextLargeDebug(canvas, " Left Ankle: " + aboveLine(person.leftLeg.ankle.getDetectedLocation()));
        drawTextLargeDebug(canvas, " Right Ankle: " + aboveLine(person.rightLeg.ankle.getDetectedLocation()));
    }

    private boolean aboveLine(DetectionLocation detectionLocation) {
        //return True if detected location of Knee is above the hipline
        return detectionLocation.getY() < hipLine;
    }

    private void processHighKnee() {

        if (coolDownTime > SystemClock.elapsedRealtime()) return;

        final int minAllowedAngle = -10;
        final int maxAllowedAngle = 90;

        leftKneeAngle = SMath.calculateAngle3Points(person.leftLeg.hip.getLocation(), person.leftLeg.knee.getLocation(), person.leftLeg.ankle.getLocation(), false);
        rightKneeAngle = SMath.calculateAngle3Points(person.rightLeg.hip.getLocation(), person.rightLeg.knee.getLocation(), person.rightLeg.ankle.getLocation(), false);

        switch (knee) {
            //check for if the knee is Right,left or Any(at start of the exercise, where left or right knee could used)
            case RIGHT:
                //check if the right knee above hipline and the leg is bent, so it changes KNEE enum to left and increment count
                if (aboveLine(person.rightLeg.knee.getLocation()) && (rightKneeAngle > minAllowedAngle && rightKneeAngle < maxAllowedAngle) && !aboveLine(person.rightLeg.ankle.getLocation())) {
                    knee = KNEE.LEFT;
                    incrementScore();
                    coolDownTime = SystemClock.elapsedRealtime() + COOLDOWNTIME;
                }
                break;
            case LEFT:
                //check if the left knee above hipline and the leg is bent, so it changes KNEE enum to Right and increment count
                if (aboveLine(person.leftLeg.knee.getLocation()) && (leftKneeAngle > minAllowedAngle && leftKneeAngle < maxAllowedAngle) && !aboveLine(person.leftLeg.ankle.getLocation())) {
                    knee = KNEE.RIGHT;
                    incrementScore();
                    coolDownTime = SystemClock.elapsedRealtime() + COOLDOWNTIME;
                }
                break;
            case ANY:
                //used at the start of the exercise, Whenever any(left or Right) KNEE is higher than the hipline and the leg is bent, it checks if the Right/left knee is higher it changes enum to Left/Right and increment count.
                if (aboveLine(person.rightLeg.knee.getLocation()) && (rightKneeAngle > minAllowedAngle && rightKneeAngle < maxAllowedAngle)) {
                    knee = KNEE.LEFT;
                    incrementScore();
                    coolDownTime = SystemClock.elapsedRealtime() + COOLDOWNTIME;
                } else if (aboveLine(person.leftLeg.knee.getLocation()) && (leftKneeAngle > minAllowedAngle && leftKneeAngle < maxAllowedAngle)) {
                    knee = KNEE.RIGHT;
                    incrementScore();
                    coolDownTime = SystemClock.elapsedRealtime() + COOLDOWNTIME;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + knee);
        }
    }

    @Override
    protected void processExercise(InfoBlob infoBlob) {
//        if knee location of left or right leg is not detected then return
        if (!person.leftLeg.knee.getLocation().locationKnown() || !person.rightLeg.knee.getLocation().locationKnown())
            return;

        //line which is higher than knee and lower than hip.
        hipLine = Math.max(
                (person.leftLeg.hip.getDetectedLocation().getY() * HIPLINEPARAMETER + person.leftLeg.knee.getDetectedLocation().getY() * (1 - HIPLINEPARAMETER)),
                (person.rightLeg.hip.getDetectedLocation().getY() * HIPLINEPARAMETER + person.rightLeg.knee.getDetectedLocation().getY() * (1 - HIPLINEPARAMETER)
                ));
        processHighKnee();
    }

    @Override
    public String toString() {
        return "HighKneeExercise{" +
                "hipLine=" + hipLine +
                ", COOLDOWNTIME=" + COOLDOWNTIME +
                ", HIPLINEPARAMETER=" + HIPLINEPARAMETER +
                ", coolDownTime=" + coolDownTime +
                ", knee=" + knee +
                '}';
    }

    @Override
    public String getName() {
        return "high-knees";
    }

    enum KNEE {
        LEFT, RIGHT, ANY
    }
}

