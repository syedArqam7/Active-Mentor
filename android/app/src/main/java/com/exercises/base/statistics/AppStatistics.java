package com.exercises.base.statistics;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.detection.ObjectDetection;
import com.location.DetectionLocation;
import com.utils.JOGOPaint;

import java.util.List;
import java.util.stream.IntStream;

public class AppStatistics {
    public long exerciseTime;
    public long startTime;
    private Long[] averageCtS;
    private int frameCounter = 0;

    public void startExercise() {
        exerciseTime = SystemClock.elapsedRealtime();
    }

    public void start() {
        startTime = SystemClock.elapsedRealtime();
    }
    //initializing averageCtS array

    public void initAverageCts(List<ObjectDetection> objectDetections) {
        averageCtS = new Long[objectDetections.size()];
        IntStream.range(0, objectDetections.size()).forEach(idx -> averageCtS[idx] = 0L);
    }


    public void drawModelStatistics(Canvas canvas, List<ObjectDetection> objectDetections) {

        if (averageCtS == null) {
            initAverageCts(objectDetections);
        }

        frameCounter++;

        for (int i = 0; i < objectDetections.size(); i++) {
            ObjectDetection objectDetection = objectDetections.get(i);
            DetectionLocation loc = objectDetection.getLocation();

            averageCtS[i] += objectDetection.getInfoBlobArrayList().getLast() == null ? 0 : objectDetection.getInfoBlobArrayList().getLast().sinceStart();

            String s = objectDetection.getLabel() + " frame: " + (loc == null ? 0 : loc.getFrameID()) +
                    " ms/i: " + String.format("%.1f", (SystemClock.elapsedRealtime() - startTime) / (float) objectDetection.getFrameInferenceCount()) +
                    " skip: " + objectDetection.getSkippedFrameCount() +
                    " CtS: " + (int) (averageCtS[i] / frameCounter);

            canvas.drawText(s, 20, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().fillStroke().small().monospace());
            objectDetection.drawDebug(canvas);
        }
    }

}
