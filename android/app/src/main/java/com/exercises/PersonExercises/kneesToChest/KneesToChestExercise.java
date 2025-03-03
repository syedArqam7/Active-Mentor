package com.exercises.PersonExercises.kneesToChest;

import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.calibration.Calibration;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;

public class KneesToChestExercise extends AbstractPersonExercise {

    private Pose upPose;
    private Pose downPose;
    private MOTION motion = MOTION.DOWN;

    public KneesToChestExercise(ExerciseSettings settings) {
        super(new LottieCalibration(R.raw.sit_up_calibration_outlines, R.raw.sit_up_calibration_correct, R.raw.sit_up_calibration_calibrate,
                settings.getExerciseVariation() == 0), settings);
        calibration.setCalibrationSize(0.3f, Calibration.BOTTOM_SCREEN - 0.1f, Calibration.LEFT_SCREEN - 0.1f, Calibration.RIGHT_SCREEN);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
       //can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.

       // drawTextDebug(canvas, "Score: " + score.getCount());
        upPose.drawDebug(canvas);
        downPose.drawDebug(canvas);

       // drawTextLargeDebug(canvas, "Perform " + motion);

    }

    @Override
    protected void initExercise() {
        // get highest body parts for the calculation of angles
        ObjectDetection shoulder = person.leftArm.shoulder.getY() < person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;
        ObjectDetection hip = person.leftLeg.hip.getY() < person.rightLeg.hip.getY() ? person.leftLeg.hip : person.rightLeg.hip;
        ObjectDetection knee = person.leftLeg.knee.getY() < person.rightLeg.knee.getY() ? person.leftLeg.knee : person.rightLeg.knee;
        ObjectDetection ankle = person.leftLeg.ankle.getY() < person.rightLeg.ankle.getY() ? person.leftLeg.ankle : person.rightLeg.ankle;
        ObjectDetection nose = person.face.nose;

        upPose = new PersonPose(person).angle(hip, knee, ankle, 30, 100)
                .angle(shoulder, hip, knee, 10, 65).isBodyUpright();
        downPose = new PersonPose(person).angle(hip, knee, ankle, 120, 180)
                .angle(shoulder, hip, knee, 90, 160)
                .above(nose, shoulder)
                .above(nose, knee)
                .isBackFlat();
    }

    protected void processExercise(InfoBlob infoBlob) {
        processSitUps();
    }

    private void processSitUps() {
        switch (motion) {
            case UP:
                if (!upPose.match()) return;
                incrementScore();
                motion = MOTION.DOWN;

                break;
            case DOWN:
                if (!downPose.match()) return;
                motion = MOTION.UP;

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "knees-to-chest";
    }


    enum MOTION {
        UP, DOWN
    }
}

