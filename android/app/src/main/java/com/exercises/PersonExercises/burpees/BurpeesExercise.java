package com.exercises.PersonExercises.burpees;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.models.ModelManager;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class BurpeesExercise extends AbstractPersonExercise {

    double midLine;
    double shoulderLine;
    boolean isBelowMidLine = false;
    MOTION motion = MOTION.DOWN;

    int MAXIMUM_HIP_ANGLE = 175, MINIMUM_HIP_ANGLE = 125;

    private Pose upPose;
    private Pose downPose;

    public BurpeesExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings, ModelManager.MODELTYPE.POSENET_FASTMODE);
    }



    @Override
    protected void initExercise() {

        midLine = Math.max(person.leftLeg.hip.getDetectedLocation().getY(), person.rightLeg.hip.getDetectedLocation().getY());
        shoulderLine = Math.max(person.leftArm.shoulder.getDetectedLocation().getY(), person.rightArm.shoulder.getDetectedLocation().getY());

        upPose = new PersonPose(person).above(person.face.nose, shoulderLine);

        downPose = new PersonPose(person).angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.ankle, MINIMUM_HIP_ANGLE, MAXIMUM_HIP_ANGLE)
                .angle(person.rightArm.shoulder, person.rightLeg.hip, person.rightLeg.ankle, MINIMUM_HIP_ANGLE, MAXIMUM_HIP_ANGLE);

    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        drawTextLargeDebug(canvas, "Motion " + motion);
//        JOGOPaint.getNewDrawDebugHeight();
//        canvas.drawText("belowMidLine " + isBelowMidLine , JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().jogoYellow().medium());
//        JOGOPaint.getNewDrawDebugHeight();
//        canvas.drawText("rightAngle: " + person.rightArm.getShoulderAngle(), JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(),  new JOGOPaint().jogoYellow().medium());

        downPose.drawDebug(canvas);

        canvas.drawLine(
                0.1f * canvas.getWidth(),
                (float) midLine * canvas.getHeight(),
                0.9f * canvas.getWidth(),
                (float) midLine * canvas.getHeight(),
                new JOGOPaint().large().jogoYellow());

        canvas.drawLine(
                0.1f * canvas.getWidth(),
                (float) shoulderLine * canvas.getHeight(),
                0.9f * canvas.getWidth(),
                (float) shoulderLine * canvas.getHeight(),
                new JOGOPaint().large().red());
    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    private boolean isPersonLyingDown() {
        return (isBelowMidLine && downPose.match());
    }

    @Override
    protected void processExercise(InfoBlob infoBlob) {

        isBelowMidLine = person.face.nose.getDetectedLocation().getY() > midLine;

        switch (motion) {
            case DOWN:
                if (isPersonLyingDown()) {
                    motion = MOTION.UP;
                }
                break;

            case UP:
                if (upPose.match()) {
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
        return "burpees";
    }


    enum MOTION {
        UP, DOWN
    }
}