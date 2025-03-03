package com.exercises.PersonExercises.growingGrass;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;

import com.utils.JOGOPaint;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;
import static com.exercises.base.exercise.ExerciseActivity.render;

public class ExerciseOutro {

    private final JOGOPaint outroScreenColor, survivedTimeTextPaint, gameOverTextPaint;
    private double colorValue = 1; // for black transparency

    private final long exerciseStartTime;
    private static final long CALIBRATION_TIME = 3500;
    private String survivedTimeText = "You Survived for ";
    private static final String GAME_OVER = "Game Over!";
    private boolean initiated = false;

    public ExerciseOutro(long exerciseStartTime) {
        this.exerciseStartTime = exerciseStartTime;
        outroScreenColor = new JOGOPaint().setColorWithRGB(0, 0, 0);
        survivedTimeTextPaint = new JOGOPaint().white().textSize(100).align(Paint.Align.CENTER).bioSansBold(eActivity.getAssets());
        gameOverTextPaint = new JOGOPaint().red().textSize(150).align(Paint.Align.CENTER).monoSpecMedium(eActivity.getAssets());
    }


    public void draw(Canvas canvas) {
        double TRANSPARENCY_THRESHOLD = 0.4;

        if (initiated) {
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), outroScreenColor.transparancy(colorValue).fill());
            colorValue -= 0.01;
            if (colorValue < TRANSPARENCY_THRESHOLD) {
                canvas.drawText(survivedTimeText, 0.5f * canvas.getWidth(), 0.6f * canvas.getHeight(), survivedTimeTextPaint);
                canvas.drawText(GAME_OVER, 0.5f * canvas.getWidth(), 0.4f * canvas.getHeight(), gameOverTextPaint);
            }
        }
    }

    public void survivalTimeCalculation() {
        long gameOverTimestamp = SystemClock.elapsedRealtime() - exerciseStartTime - CALIBRATION_TIME;
        @SuppressLint("DefaultLocale") String format = String.format("%%0%dd", 2);
        long elapsedTime = gameOverTimestamp / 1000;
        long seconds = elapsedTime % 60;
        long minutes = (elapsedTime % 3600) / 60;

        if (minutes == 0)
            survivedTimeText = "You Survived for " + String.format(format, seconds) + " seconds";
        else {
            survivedTimeText = "You Survived for " + String.format(format, minutes) + ":" + String.format(format, seconds);
        }
    }

    public void initiate() {
        survivalTimeCalculation();
        initiated = true;
        render.hideScore();
        render.hideTimer();

    }

}
