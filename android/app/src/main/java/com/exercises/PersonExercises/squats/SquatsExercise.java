package com.exercises.PersonExercises.squats;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseActivity;
import com.exercises.base.exercise.ExerciseSettings;
import com.exercises.score.Score;
import com.jogo.R;
import com.logger.SLOG;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class SquatsExercise extends AbstractPersonExercise {

    int lastDownFrame, lastUpFrame;
    private MOTION motion = MOTION.DOWN;   //To go down first is default set
    private static final double DECISIONLINE_LOW = 0.5;
    private static final double DECISIONLINE_HIGH = 0.7;
    private Pose bodyUpright;
    private double scaleRatio = 1;
    private double hipLocationY;
    private double kneeLocationY;
    private double decisionLineHigh;
    private double decisionLineLow;
    private double calibratedDistanceNoseHipY;
    private double calibratedDistanceHipKneeY;

    public SquatsExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);

    }


    protected void initExercise() {
        setCalibratedDistanceNoseHipY();
        setCalibratedDistanceHipKneeY();
        bodyUpright = new PersonPose(person).isBodyUpright();

    }


    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }




    @Override
    protected void drawExerciseDebug(Canvas canvas) {
// uncomment when required or during debugging.
//        //canvas.drawText("Score: " + score.getCount(), 200, getNewDrawDebugHeight(), new JOGOPaint().jogoYellow().large());
//        canvas.drawLine(0, (float) decisionLineHigh * canvas.getHeight(), canvas.getWidth(), (float) decisionLineHigh * canvas.getHeight(), new JOGOPaint().blue().mediumStroke());
//        canvas.drawLine(0, (float) decisionLineLow * canvas.getHeight(), canvas.getWidth(), (float) decisionLineLow * canvas.getHeight(), new JOGOPaint().green().mediumStroke());
//
//        canvas.drawLine(0, (float) kneeLocationY * canvas.getHeight(), canvas.getWidth(), (float) kneeLocationY * canvas.getHeight(), new JOGOPaint().yellow().mediumStroke());
//        canvas.drawLine(0, (float) hipLocationY * canvas.getHeight(), canvas.getWidth(), (float) hipLocationY * canvas.getHeight(), new JOGOPaint().red().mediumStroke());
//        drawTextLargeDebug(canvas, "Perform " + motion);
//        drawTextLargeDebug(canvas, "Ratio " + scaleRatio);
    }

    // Calculate distance between Hip and Knee during calibration
    public void setCalibratedDistanceHipKneeY() {
        double kneeY = Math.max(person.leftLeg.knee.getY(), person.rightLeg.knee.getY());
        double hipY = Math.max(person.leftLeg.hip.getY(), person.rightLeg.hip.getY());
        calibratedDistanceHipKneeY = kneeY - hipY;
    }

    // Calculate distance between Hip and Nose during calibration
    public void setCalibratedDistanceNoseHipY() {
        double noseY = person.face.nose.getDetectedLocation().getY();
        double hipY = Math.max(person.leftLeg.hip.getY(), person.rightLeg.hip.getY());
        calibratedDistanceNoseHipY = hipY - noseY;

    }

    private void updateScaleRatio() {
        double noseY = person.face.nose.getDetectedLocation().getY();
        double hipY = Math.max(person.leftLeg.hip.getY(), person.rightLeg.hip.getY());
        double currentDistanceNoseHipY = hipY - noseY;
        scaleRatio = currentDistanceNoseHipY / calibratedDistanceNoseHipY;
    }

    public double getDecisionLineHigh() {
        return kneeLocationY - calibratedDistanceHipKneeY * DECISIONLINE_HIGH * scaleRatio;
    }

    public double getDecisionLineLow() {
        return kneeLocationY - calibratedDistanceHipKneeY * DECISIONLINE_LOW * scaleRatio;
    }


    protected void processExercise(InfoBlob infoBlob) {
        updateScaleRatio();
        hipLocationY = Math.max(person.leftLeg.hip.getY(), person.rightLeg.hip.getY());
        kneeLocationY = Math.max(person.leftLeg.knee.getY(), person.rightLeg.knee.getY());
        decisionLineLow = getDecisionLineLow();
        decisionLineHigh = getDecisionLineHigh();

        switch (motion) {
            //switch for sequence of Motions (Down then Up)
            case DOWN:
                if (hipLocationY >= (decisionLineLow) && bodyUpright.match()) {
                    motion = MOTION.UP;
                    lastDownFrame = infoBlob.getFrameID();

                    render.up();
                }
                break;
            case UP:
                if (hipLocationY <= (decisionLineHigh) && bodyUpright.match()) {
                    motion = MOTION.DOWN;
                    lastUpFrame = infoBlob.getFrameID();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "squats";
    }

    @Override
    public String toString() {
        return "Squat{" +
                "Count=" + score.getCount() +
                "latestEstimatedHipLocationY=" + decisionLineHigh +
                ", hipLocationY=" + hipLocationY +
                ", kneeLocationY=" + kneeLocationY +
                '}';
    }

    enum MOTION {
        UP,
        DOWN,
    }
}
