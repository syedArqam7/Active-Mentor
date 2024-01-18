package com.exercises.PersonExercises.sideToSideHop;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.location.DetectionLocation;
import com.pose.PersonPose;
import com.render.lottie.LottieCalibration;
import com.utils.ConeLocation;
import com.utils.ImageUtils;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.SMath;

import java.util.ArrayList;
import java.util.List;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;


public class SideToSideHopExercise extends AbstractPersonExercise {

    private final List<PersonPose> poses = new ArrayList<>();
    boolean conesUsed = true;
    ConeLocation cone;
    private POSITION nextPosition = POSITION.LEFT;
    private double hurdleY;
    private static final double hurdleX = 0.5;
    private PersonPose poseLeft, poseRight, poseAbove;
    private static final int LOOKBACK = 10;
    private static final double ANKLE_SCALE = 0.8;
    private static final double RADIUS = 0.13;


    public SideToSideHopExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
        // calibration.setBitmapSize(0.2f,Calibration.BOTTOM_SCREEN,Calibration.LEFT_SCREEN,Calibration.RIGHT_SCREEN);
    }


    protected void initExercise() {
        poseLeft = new PersonPose(person)
                .leftOf(person.leftLeg.heel, hurdleX)
                .leftOf(person.rightLeg.heel, hurdleX);
        poses.add(poseLeft);

        poseRight = new PersonPose(person)
                .rightOf(person.leftLeg.heel, hurdleX)
                .rightOf(person.rightLeg.heel, hurdleX);
        poses.add(poseRight);

        poseAbove = new PersonPose(person, "jumped")
                .hasBeenAbove(person.leftLeg.heel, () -> hurdleY, LOOKBACK)
                .hasBeenAbove(person.rightLeg.heel, () -> hurdleY, LOOKBACK);
        poses.add(poseAbove);

        initCones();

    }

    private void initCones() {
        if (!conesUsed) return;
        cone = new ConeLocation(RADIUS, hurdleX, hurdleY, false).activate(); //first cone position
    }


    @Override
    protected void drawExercise(Canvas canvas) {
        cone.draw(canvas);
    }


    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.

//        drawTextLargeDebug(canvas, "Move to " + nextPosition + " and Jump");
//        canvas.drawLine((float) hurdleX * canvas.getWidth(), (float) hurdleY * canvas.getHeight(), (float) hurdleX * canvas.getWidth(), canvas.getHeight(), new JOGOPaint().red().largeStroke());
//        poses.forEach(p -> p.drawDebug(canvas));
    }

    private boolean isHurdleTouched() {
        return !poseAbove.match();
    }

    private void updateHurdleY() {
        Double kneeYl = person.leftLeg.knee.getNDetectedLocations(LOOKBACK).getMax(DetectionLocation::compareByY).getY() * (1 - ANKLE_SCALE);
        Double kneeYr = person.rightLeg.knee.getNDetectedLocations(LOOKBACK).getMax(DetectionLocation::compareByY).getY() * (1 - ANKLE_SCALE);

        Double ankleYl = person.leftLeg.ankle.getNDetectedLocations(LOOKBACK).getMax(DetectionLocation::compareByY).getY() * ANKLE_SCALE;
        Double ankleYr = person.rightLeg.ankle.getNDetectedLocations(LOOKBACK).getMax(DetectionLocation::compareByY).getY() * ANKLE_SCALE;

        hurdleY = SMath.getMean(kneeYl + ankleYl, kneeYr + ankleYr);

        cone.setY(hurdleY);

    }


    protected void processExercise(InfoBlob infoBlob) {
        updateHurdleY();
        switch (nextPosition) {
            case LEFT:
                if (poseLeft.match()) {
                    nextPosition = POSITION.RIGHT;
                    if (isHurdleTouched()) {
                        cone.setFallOverDirection(true);
                    } else {
                        incrementScore();
                        if (cone.isFallOver()) {
                            cone.reset();
                        }
                    }
                }
                break;
            case RIGHT:
                if (poseRight.match()) {
                    nextPosition = POSITION.LEFT;
                    if (isHurdleTouched()) {
                        cone.setFallOverDirection(false);
                    } else {
                        incrementScore();
                        if (cone.isFallOver()) {
                            cone.reset();
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + nextPosition);
        }
    }

    @Override
    public String getName() {
        return "side-to-side-hop";
    }

    @Override
    public String toString() {
        return "HopHop{" +
                "Count=" + score.getCount() +
                '}';
    }

    enum POSITION {
        LEFT,
        RIGHT
    }

}
