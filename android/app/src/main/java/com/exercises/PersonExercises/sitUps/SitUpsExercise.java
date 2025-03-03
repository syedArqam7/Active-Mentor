package com.exercises.PersonExercises.sitUps;

import android.graphics.Canvas;

import com.detection.ObjectDetection;
import com.exercises.base.calibration.Calibration;
import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.Gear;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;
import com.utils.UtilArrayList;

import static com.exercises.base.exercise.ExerciseActivity.render;

public class SitUpsExercise extends AbstractPersonExercise {
    private ObjectDetection hip;
    private ObjectDetection shoulder;
    private ObjectDetection knee;
    private ObjectDetection ankle;
    private final UtilArrayList<Double> HKA_Angles = new UtilArrayList<>();
    private static final int SHK_ANGLE_UPPER_LIMIT = 160;
    private static final int SHK_ANGLE_LOWER_LIMIT = 120;
    private static final int HIP_ANKLE_LIMIT = 60;

    private Pose upPose;
    private Pose downPose;
    private MOTION motion = MOTION.DOWN;

    // Gear Initializers
    {
        Gear.setIff("DrawExerciseDebug", true);
    }

    public SitUpsExercise(ExerciseSettings settings) {
        //we set the topscreen to 0.3, to deal with the questions probably need a better way to deal with it later...
        super(new LottieCalibration(R.raw.sit_up_calibration_outlines, R.raw.sit_up_calibration_correct, R.raw.sit_up_calibration_calibrate), settings);
        calibration.setCalibrationSize(0.3f, Calibration.BOTTOM_SCREEN - 0.1f, Calibration.LEFT_SCREEN - 0.1f, Calibration.RIGHT_SCREEN);
    }

    @Override
    protected void drawExercise(Canvas canvas) {
//can be overridden if required
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        if (hip == null || shoulder == null || knee == null || ankle == null) return;
//        drawTextDebug(canvas,"Score: " + score.getCount());
//        upPose.drawDebug(canvas);
//        downPose.drawDebug(canvas);
//
//        drawTextLargeDebug(canvas, "Perform " + motion);

    }

    @Override
    protected void initExercise() {
        // get highest body parts for the calculation of angles
        shoulder = person.leftArm.shoulder.getY() < person.rightArm.shoulder.getY() ? person.leftArm.shoulder : person.rightArm.shoulder;
        hip = person.leftLeg.hip.getY() < person.rightLeg.hip.getY() ? person.leftLeg.hip : person.rightLeg.hip;
        knee = person.leftLeg.knee.getY() < person.rightLeg.knee.getY() ? person.leftLeg.knee : person.rightLeg.knee;
        ankle = person.leftLeg.ankle.getY() < person.rightLeg.ankle.getY() ? person.leftLeg.ankle : person.rightLeg.ankle;

        upPose = new PersonPose(person).angle(shoulder, hip, ankle, 0, SHK_ANGLE_LOWER_LIMIT).angleYaxis(hip, ankle, HIP_ANKLE_LIMIT, 120).isBodyUpright();
        downPose = new PersonPose(person).angle(shoulder, hip, ankle, SHK_ANGLE_UPPER_LIMIT, 250).angleYaxis(hip, ankle, HIP_ANKLE_LIMIT, 120).isBackFlat();
    }

    protected void processExercise(InfoBlob infoBlob) {

        // calculate angle between hip, knee and ankle
        double HKA_Angle = SMath.calculateAngle3Points(hip, knee, ankle, false);

        processSitUps();
        // for ratings
        HKA_Angles.add(HKA_Angle);
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
                render.up();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + motion);
        }
    }

    @Override
    public String getName() {
        return "situps";
    }


    enum MOTION {
        UP, DOWN
    }
}

