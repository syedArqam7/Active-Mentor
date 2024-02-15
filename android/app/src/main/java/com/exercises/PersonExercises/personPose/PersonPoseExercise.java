package com.exercises.PersonExercises.personPose;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.logger.SLOG;
import com.pose.PersonPose;
import com.pose.Pose;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.UtilArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;

public class PersonPoseExercise extends AbstractPersonExercise {

    private final UtilArrayList<Pose> personPosesPerform = new UtilArrayList<>();
    private final Random rd = new Random();
    private int poseIndex;
    private int lastPoseIndex;
    private static final int POSE_SIZE = 4;
    private Drawable poseImage;
    private static final long PERFORM_POSE_TIME_FRAME = 3000;
    private long posePerformTime = 0;


    public PersonPoseExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);

    }

    @Override
    protected void drawExercise(Canvas canvas) {
        if (poseImage != null) {
            drawImage(canvas, poseImage);
        }

    }



    @Override
    protected void initExercise() {
        personPosesPerform.add(new PersonPose(person).minShoulderAngle(95).minLegAngle(18));
        personPosesPerform.add(new PersonPose(person).maxArmAngle(95).maxLegAngle(18));
        personPosesPerform.add(new PersonPose(person).minShoulderAngle(75).maxLegAngle(18));
        personPosesPerform.add(new PersonPose(person).angle(person.leftArm.shoulder, person.leftLeg.hip, person.leftLeg.knee, 60, 95));

        initPose();
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.

//        canvas.drawText("00:0" + ((int) (posePerformTime - SystemClock.elapsedRealtime()) / 1000), 650,
//                JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().activeOrange().large());

//        drawTextLargeDebug(canvas, "00:0" + ((int) (posePerformTime - SystemClock.elapsedRealtime()) / 1000));
//
//        if (personPosesPerform.get(poseIndex) != null)
//            personPosesPerform.get(poseIndex).drawDebug(canvas);
    }

    private void initPose() {
        // init with random pose
        do {
            poseIndex = rd.nextInt(POSE_SIZE);
        }
        while (poseIndex == lastPoseIndex);
        lastPoseIndex = poseIndex;

        poseImage = preloadPicture(poseIndex + ".png");
        posePerformTime = SystemClock.elapsedRealtime() + PERFORM_POSE_TIME_FRAME;
    }

    public Drawable preloadPicture(String path) {

        // get asset manager
        AssetManager assetManager = eActivity.getAssets();
        try ( InputStream inputStream = assetManager.open("images/" + path)){
            // create drawable of answer image from input stream
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            SLOG.d("Error in loading pictures: " + e);
            return null;
        }
    }


    public void drawImage(Canvas canvas, Drawable drawable) {
        drawable.setBounds((int) (canvas.getWidth() * 0.1),
                (int) (canvas.getHeight() * 0.1),
                (int) (canvas.getWidth() * 0.9),
                (int) (canvas.getHeight() * 0.7));
        drawable.draw(canvas);
    }


    protected void processExercise(InfoBlob infoBlob) {
        // wait for time out then check the pose
        if (posePerformTime < SystemClock.elapsedRealtime()) {
            // check the pose, if correct then increment score
            if (personPosesPerform.get(poseIndex).match())
                incrementScore();

            initPose();
        }

    }

    @Override
    public String getName() {
        return "person-pose";
    }

    @Override
    public String toString() {
        return "PersonPoseGame{" +
                "Count=" + score.getCount() +
                '}';
    }


}
