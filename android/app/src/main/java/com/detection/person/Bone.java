package com.detection.person;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.location.DetectionLocation;
import com.render.CanDraw;
import com.utils.JOGOPaint;

public class Bone implements CanDraw {
    private final BodyElement bodyPart1;
    private final BodyElement bodyPart2;
    private final String boneName;
    private double currentLenght;
    private double maxLength;


    public Bone(BodyElement bodyPart1, BodyElement bodyPart2) {
        this.bodyPart1 = bodyPart1;
        this.bodyPart2 = bodyPart2;
        this.boneName = bodyPart1.getLabel() + " " + bodyPart2.getLabel();
    }

    @Override
    public void draw(Canvas canvas) {
        //can be overridden if required
    }

    //new draw
    public void draw(Canvas canvas, Paint paint) {
        //can be overridden if required
    }

    @Override
    public void drawDebug(Canvas canvas) {
        drawDebug(canvas, new JOGOPaint().activeOrange().mediumStroke());
    }

    public void drawDebug(Canvas canvas, Paint paint) { //drawDebug replica
        DetectionLocation l1 = bodyPart1.getLocation();
        DetectionLocation l2 = bodyPart2.getLocation();
        if (l1 == null || !l1.locationKnown() || l2 == null || !l2.locationKnown()) return;

        canvas.drawLine((float) l1.getX() * canvas.getWidth(), (float) l1.getY() * canvas.getHeight(), (float) l2.getX() * canvas.getWidth(), (float) l2.getY() * canvas.getHeight(), paint);
//        canvas.drawText("" + String.format("%.2f", currentLenght) + "," + String.format("%.2f", maxLength), (float) (l1.getX() + l2.getX())/2f * canvas.getWidth(), (float) (l1.getY() + l2.getY())/2f * canvas.getHeight(), new JOGOPaint().red().fillStroke().small().monospace());
    }

    public void updateLength() {
        DetectionLocation l1 = bodyPart1.getLocation();
        DetectionLocation l2 = bodyPart2.getLocation();
        if (l1 == null || !l1.locationKnown() || l2 == null || !l2.locationKnown()) return;

        /*
         * lim y(t) = lim y(t-1) * 0.5 + x(t)
         *     (t -> inf) --> y(t) = x(t)
         */

        currentLenght = currentLenght * 0.5 + l1.getEuclideanDistance(l2) * 0.5;
        maxLength = Math.max(currentLenght, maxLength);
    }

    public double getFullLength() {
        return maxLength;
    }

    public String getBoneName() {
        return boneName;
    }

}

