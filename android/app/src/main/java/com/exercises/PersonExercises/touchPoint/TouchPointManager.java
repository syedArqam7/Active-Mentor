package com.exercises.PersonExercises.touchPoint;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.detection.ObjectDetection;
import com.detection.person.PersonDetection;
import com.exercises.score.Score;
import com.render.Render;
import com.utils.JOGOPaint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class TouchPointManager {

    double X_MAX = 0.9, Y_MAX = 0.9, X_MIN = 0.1, Y_MIN = 0.15;
    double NUM_QUADRANTS = 10;
    String currentQuadrantNum;
    Random randNumGenerator = new Random();
    PersonDetection person;
    Score score;
    Render render;
    List<String> activeQuadrants = new ArrayList<>();
    Map<String, RectF> quadrantMap =  new ConcurrentHashMap<>();
    List<String> messyQuadrants = new ArrayList<>();


    TouchPointExercise.MODE exerciseMode; // todo there must be a better way to do this
    boolean drawQuadrant = true;   // set to true if you want to draw all the quadrants

    public TouchPointManager(PersonDetection person, Score score, TouchPointExercise.MODE exerciseMode) {
        this.score = score;
        this.person = person;
        this.exerciseMode = exerciseMode;
        initializeQuadrants();

    }

    private void initializeQuadrants() {
        double xVal = (X_MAX + X_MIN) / NUM_QUADRANTS;
        double yVal = (Y_MAX + Y_MIN) / NUM_QUADRANTS;
        double left, right, top = Y_MIN, bottom = top + yVal;

        int Y_NUM_QUADRANTS = 6, X_NUM_QUADRANTS = 8;
        int quadrantNum;
        for (int y = 0; y < Y_NUM_QUADRANTS; y++) {
            left = X_MIN;
            for (int x = 0; x < X_NUM_QUADRANTS; x++) {
                right = left + xVal;
                quadrantNum = x + (y * X_NUM_QUADRANTS);
                quadrantMap.put("" + quadrantNum, new RectF((float) left, (float) top, (float) right, (float) bottom));
                left = right;
            }
            top += yVal;
            bottom = top + yVal;
        }
        // disable the quadrants that overlap Score circle and timer box.
        messyQuadrants.add("" + 7);
        messyQuadrants.add("" + 15);
        messyQuadrants.add("" + 23);
        messyQuadrants.add("" + 31);
        messyQuadrants.add("" + 39);

    }

    private boolean isAnyBodyPartInside(RectF quadrantRect) {
        return person.getDetectionSubClasses().stream().map(ObjectDetection::getLocation).anyMatch(d -> quadrantRect.contains(d.getXf(), d.getYf()));
    }

    private boolean isQuadrantAfar(RectF quadrantRect) {

        double MIN_THRESHOLD = person.shoulderToShoulder.getFullLength();
        return person.getDetectionSubClasses().stream().map(ObjectDetection::getDetectedLocation).anyMatch(d -> (d.getEuclideanDistance(quadrantRect.centerX(), quadrantRect.centerY()) <= MIN_THRESHOLD));
    }

    // for FOOT mode; we want all the lower quadrants to be active.
    private boolean isQuadrantBelowShoulders(RectF quadrantRect) {
        return (Math.max(person.leftArm.shoulder.getDetectedLocation().getY(), person.rightArm.shoulder.getDetectedLocation().getY()) < quadrantRect.top);

    }

    private void updateQuadrants() {
        activeQuadrants.clear();
        IntStream.range(0, quadrantMap.size()).forEach(quadrantNum -> {
            RectF quadrantRectF = quadrantMap.get("" + quadrantNum);
            if (exerciseMode == TouchPointExercise.MODE.FEET) {
                if (!isAnyBodyPartInside(quadrantRectF) && isQuadrantBelowShoulders(quadrantRectF) && !isQuadrantAfar(quadrantRectF) && !messyQuadrants.contains("" + quadrantNum))
                    activeQuadrants.add("" + quadrantNum);
            } else {
                if (!isAnyBodyPartInside(quadrantRectF) && !isQuadrantAfar(quadrantRectF) && !messyQuadrants.contains("" + quadrantNum))
                    activeQuadrants.add("" + quadrantNum);
            }
        });

        // if all quadrants are inactive, then activate all of them except the messyOnes that overlap SCORE circle and TIMER box
        if (activeQuadrants.isEmpty()) {
            IntStream.range(0, quadrantMap.size()).forEach(quadrantNum -> {
                if (!messyQuadrants.contains("" + quadrantNum))
                    activeQuadrants.add("" + quadrantNum);
            });
        }
    }

    public TouchPoint spawnNewTouchPoint() {
        updateQuadrants();
        int randQuadrantNum = randNumGenerator.nextInt(activeQuadrants.size());

        RectF quadrantRectF = quadrantMap.get(activeQuadrants.get(randQuadrantNum));
        currentQuadrantNum = activeQuadrants.get(randQuadrantNum);
        return new TouchPoint(person, score, render, quadrantRectF);

    }

    private void drawQuadrants(Canvas canvas) {
        int bound = quadrantMap.size();
        for (int quadrantNum = 0; quadrantNum < bound; quadrantNum++) {
            JOGOPaint paint = new JOGOPaint().activeOrange().stroke();
            paint.setStrokeWidth(10);
            if (!activeQuadrants.contains("" + quadrantNum))
                paint.red();
            RectF rectF = quadrantMap.get("" + quadrantNum);
            canvas.drawRect(rectF.left * canvas.getWidth(), rectF.top * canvas.getHeight(), rectF.right * canvas.getWidth(), rectF.bottom * canvas.getHeight(), paint);
        }
    }

    public void draw(Canvas canvas) {
        if (quadrantMap != null && drawQuadrant) {
            drawQuadrants(canvas);
        }
        JOGOPaint.getNewDrawDebugHeight();
        canvas.drawText("quadrant " + currentQuadrantNum, 20, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().activeOrange().medium());
        JOGOPaint.getNewDrawDebugHeight();
        canvas.drawText("mode " + exerciseMode, 20, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().activeOrange().medium());

    }


}
