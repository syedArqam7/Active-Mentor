package com.exercises.PersonExercises.jumpingSquat;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class JumpingSquatExercise extends AbstractPersonExercise {

    private MOTION Motion = MOTION.DOWN;   //To go down first is default set
    private static final float HIPLINEPARAMETER = 0.6f;

    private double hipLocationY, kneeLocationY;

    private int lastJumpFrame = 0, lastDownFrame = 0;

    public JumpingSquatExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }



    @Override
    protected void drawExerciseDebug(Canvas canvas) {
// uncomment when required or during debugging.
//        canvas.drawText("Score: " + score.getCount(), 200, getNewDrawDebugHeight(), new JOGOPaint().jogoYellow().large());

//        canvas.drawLine(0, (float) hipLocationY * canvas.getHeight(), canvas.getWidth(), (float) hipLocationY * canvas.getHeight(), new JOGOPaint().blue().mediumStroke());
//        canvas.drawLine(0, (float) kneeLocationY * canvas.getHeight(), canvas.getWidth(), (float) kneeLocationY * canvas.getHeight(), new JOGOPaint().yellow().mediumStroke());
//        drawTextLargeDebug(canvas, "Perform " + Motion);

    }


    private void processWithJump(InfoBlob infoBlob) {
        //switch for sequence of Motions (Down/squat then jump)
        switch (Motion) {
            case DOWN:
                if (hipLocationY >= (kneeLocationY)) {                 // comparing if the hip is lower than knees
                    lastDownFrame = infoBlob.getFrameID();
                    Motion = MOTION.JUMP;
                    render.jump();
                }
                break;
            case JUMP:
                int jumpFrame = person.didJump(lastJumpFrame);
                if (jumpFrame != lastJumpFrame && jumpFrame > lastDownFrame) {      //comparing if any new jump is recorded (second condition to ensure that it's after last down movement)
                    Motion = MOTION.DOWN;
                    lastJumpFrame = jumpFrame;
                    incrementScore();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Motion);
        }
    }

    protected void processExercise(InfoBlob infoBlob) {

        // Hip Y Location in current Frame
        hipLocationY = SMath.getMeanY(person.leftLeg.hip, person.rightLeg.hip) * HIPLINEPARAMETER +
                SMath.getMeanY(person.leftLeg.ankle, person.rightLeg.ankle) * (1 - HIPLINEPARAMETER);

        // KNEE Y Location in current Frame
        kneeLocationY = SMath.getMeanY(person.leftLeg.knee, person.rightLeg.knee);

        processWithJump(infoBlob);

    }

    @Override
    public String getName() {
        return "jumping-squats";
    }

    @Override
    public String toString() {
        return "JumpingSquat{" +
                "Count=" + score.getCount() +
                ", hipLocationY=" + hipLocationY +
                ", kneeLocationY=" + kneeLocationY +
                ", LastJumpFrame=" + lastJumpFrame +
                ", LastDownFrame=" + lastDownFrame +
                ", HIPLINEPARAMETER=" + HIPLINEPARAMETER +
                '}';
    }

    enum MOTION {
        DOWN,
        JUMP
    }

}
