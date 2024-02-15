package com.exercises.PersonExercises.orderGames;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.location.Location;
import com.utils.JOGOPaint;

public class OrderSequence extends Location {
    long selectionTime = 0;
    boolean selected = false;
    private double radius;
    private final double visibleRadius;
    private boolean correct;
    private int seqNumber;
    private JOGOPaint colorPaint;
    private final JOGOPaint pText = new JOGOPaint().activeOrange().bioSansBold().center();

    public OrderSequence(double radius, double centerX, double centerY, int seqNumber, double visibleRadius) {
        super(centerX, centerY);
        this.radius = radius;
        this.seqNumber = seqNumber;
        this.visibleRadius = visibleRadius;

        colorPaint = new JOGOPaint().black().fill();

        pText.setTextSize(160);
        pText.setStrokeWidth(5);
    }

    private void reset() {
        selected = false;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int number) {
        this.seqNumber = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void select(boolean correct) {
        selectionTime = SystemClock.elapsedRealtime();
        selected = true;
        this.correct = correct;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        String seqNumberString = String.valueOf(seqNumber);
        if (selected) {
            if (correct)
                colorPaint = new JOGOPaint().green().transparancy(.30).fill();
            if (!correct)
                colorPaint = new JOGOPaint().red().transparancy(.30).fill();

        }
        // sequence number
        canvas.drawCircle(canvX(canvas), canvY(canvas),
                (float) (Math.min(canvas.getWidth(), canvas.getHeight()) * visibleRadius), colorPaint);
        canvas.drawText(seqNumberString, canvX(canvas), canvY(canvas) - pText.centerY(), pText);
    }


    @Override
    public void drawDebug(Canvas canvas) {
        canvas.drawCircle(canvX(canvas), canvY(canvas), canvRad(canvas), new JOGOPaint().black().fill().transparancy(.70));
        this.draw(canvas);
    }
}




