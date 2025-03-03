package com.exercises.PersonExercises.ladderRun;

import android.graphics.Canvas;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseActivity;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.render.lottie.LottieCalibration;
import com.utils.Box;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.UtilArrayList;

import java.util.Random;
import java.util.stream.IntStream;


public class LadderRunExercise extends AbstractPersonExercise {

    private static final Random rand = new Random();
    private static final int BOXES = 6;
    private static final UtilArrayList<Box> box = new UtilArrayList<>();
    private static final double BOXSTARTY = 0.72;
    private static final double BOXWIDTH = 0.1;
    private static final double BOXHEIGHT = 0.25;
    private int nextPosition = 1;

    public LadderRunExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings);
    }

    @Override
    protected void initExercise() {
        initBoxes();
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        drawTextLargeDebug(canvas, "Move to " + nextPosition + " and Jump");
        IntStream.range(0, BOXES).forEach(x ->
        {
            if (x == (nextPosition - 1)) {
                box.get(x).drawBox(canvas, new JOGOPaint().red().textSize(400).strokeWidth(20), new JOGOPaint().blue().textSize(200).strokeWidth(20));
            } else {
                box.get(x).drawBox(canvas, new JOGOPaint().red().stroke().strokeWidth(20), new JOGOPaint().blue().textSize(200).strokeWidth(20));
            }
        });
    }

    @Override
    protected void drawExerciseDebug(Canvas canvas) {
// uncomment when required or during debugging.
//        drawTextLargeDebug(canvas, "Move to " + nextPosition + " and Jump");
//        IntStream.range(0, BOXES).forEach(x ->
//        {
//            if (x == (nextPosition - 1)) {
//                box.get(x).drawBox(canvas, new JOGOPaint().red().textSize(400).strokeWidth(20), new JOGOPaint().blue().textSize(200).strokeWidth(20));
//            } else {
//                box.get(x).drawBox(canvas, new JOGOPaint().red().stroke().strokeWidth(20), new JOGOPaint().blue().textSize(200).strokeWidth(20));
//            }
//        });

    }

    private void initBoxes() {
        double BOXSTARTX = 0.2;
        final double[] nextX = {BOXSTARTX};
        IntStream.range(0, BOXES).forEach(x ->
        {
            box.add(new Box(nextX[0], BOXSTARTY, BOXWIDTH, BOXHEIGHT, x, ExerciseActivity.timer));
            nextX[0] = nextX[0] + BOXWIDTH;
        });
    }


    @Override
    protected void processExercise(InfoBlob infoBlob) {
        int previousPosition = nextPosition;
        if (box.get(nextPosition - 1).isInAndJumped(person)) {
            incrementScore();
            do {
                nextPosition = ((rand.nextInt(BOXES + 1) % BOXES) + 1);
            } while (nextPosition == previousPosition);

        }

    }

    @Override
    public String getName() {
        return "ladder-run";
    }

    @Override
    public String toString() {
        return "LadderRun{" +
                "Count=" + score.getCount() +
                '}';
    }

}
