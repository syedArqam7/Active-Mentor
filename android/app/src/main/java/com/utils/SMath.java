package com.utils;

import com.detection.ObjectDetection;
import com.location.DetectionLocation;

public class SMath {

    public static double getMeanX(DetectionLocation loc1, DetectionLocation loc2) {
        return (loc1.getX() + loc2.getX()) / 2;

    }

    public static double getMeanX(ObjectDetection obj1, ObjectDetection obj2) {
        return getMeanX(obj1.getDetectedLocation(), obj2.getDetectedLocation());
    }

    public static double getMaxX(ObjectDetection obj1, ObjectDetection obj2) {
        return Math.max(obj1.getDetectedLocation().getX(), obj2.getDetectedLocation().getX());
    }

    public static double getMinX(ObjectDetection obj1, ObjectDetection obj2) {
        return Math.min(obj1.getDetectedLocation().getX(), obj2.getDetectedLocation().getX());
    }


    public static double getMaxY(ObjectDetection obj1, ObjectDetection obj2) {
        return Math.max(obj1.getDetectedLocation().getY(), obj2.getDetectedLocation().getY());
    }

    public static double getMinY(ObjectDetection obj1, ObjectDetection obj2) {
        return Math.min(obj1.getDetectedLocation().getY(), obj2.getDetectedLocation().getY());
    }

    public static double getMeanY(DetectionLocation loc1, DetectionLocation loc2) {
        return (loc1.getY() + loc2.getY()) / 2;
    }

    public static double getMeanY(ObjectDetection obj1, ObjectDetection obj2) {
        return getMeanY(obj1.getDetectedLocation(), obj2.getDetectedLocation());
    }


    /***** SLOPE X *****/
    public static double calculateSlopeWithXAxis(double x1, double x2, double y1, double y2) {
        return Math.abs((y2 - y1) / (x2 - x1));
    }

    public static double calculateSlopeWithXAxis(DetectionLocation loc1, DetectionLocation loc2) {
        return calculateSlopeWithXAxis(loc1.getX(), loc2.getX(), loc1.getY(), loc2.getY());
    }

    public static double calculateSlopeWithXAxis(ObjectDetection obj1, ObjectDetection obj2) {
        return calculateSlopeWithXAxis(obj1.getDetectedLocation(), obj2.getDetectedLocation());
    }

    /***** SLOPE Y *****/
    public static double calculateSlopeWithYAxis(double x1, double x2, double y1, double y2) {
        return Math.abs((x2 - x1) / (y2 - y1));
    }

    public static double calculateSlopeWithYAxis(DetectionLocation loc1, DetectionLocation loc2) {
        return calculateSlopeWithYAxis(loc1.getX(), loc2.getX(), loc1.getY(), loc2.getY());
    }

    public static double calculateSlopeWithYAxis(ObjectDetection obj1, ObjectDetection obj2) {
        return calculateSlopeWithYAxis(obj1.getDetectedLocation(), obj2.getDetectedLocation());
    }


    /***** ANGLE X *****/
    public static double calculateAngleWithXAxis(double x1, double x2, double y1, double y2) {
        return Math.round(Math.toDegrees(Math.atan((calculateSlopeWithXAxis(x1, x2, y1, y2)))) * 100.0) / 100.0;
    }

    public static double calculateAngleWithXAxis(DetectionLocation loc1, DetectionLocation loc2) {
        return calculateAngleWithXAxis(loc1.getX(), loc2.getX(), loc1.getY(), loc2.getY());
    }

    public static double calculateAngleWithXAxis(ObjectDetection obj1, ObjectDetection obj2) {
        return calculateAngleWithXAxis(obj1.getDetectedLocation(), obj2.getDetectedLocation());
    }


    /***** ANGLE Y *****/
    public static double calculateAngleWithYAxis(double x1, double x2, double y1, double y2) {
        return (Math.abs(90 - Math.round(Math.toDegrees(Math.atan(calculateSlopeWithXAxis(x1, x2, y1, y2))) * 100.0) / 100.0));
    }

    public static double calculateAngleWithYAxis(DetectionLocation loc1, DetectionLocation loc2) {
        return calculateAngleWithYAxis(loc1.getX(), loc2.getX(), loc1.getY(), loc2.getY());
    }

    public static double calculateAngleWithYAxis(ObjectDetection obj1, ObjectDetection obj2) {
        return calculateAngleWithYAxis(obj1.getDetectedLocation(), obj2.getDetectedLocation());
    }


    public static double calculateAngle3Points(DetectionLocation dlA, DetectionLocation dlB, DetectionLocation dlC, boolean negativeAngle) {
        /*
        * Forming a triangle
        * dlA = elbow
        * dlB = shoulder
        * dlC = hip
        * OR
        * dlA = shoulder
        * dlB = hip
        * dlC = ankle
        *
        * Let in ABC triangle,
                a = distance(C, B)
                b = distance(C, A)
                c = distance(A, B)
        *
        * B= arccos(a^2+c^2−b^2)/2.c.a
        */
        if (dlA == null || dlB == null || dlC == null) return -1;
        double distanceA = dlC.getEuclideanDistance(dlB);
        double distanceB = dlC.getEuclideanDistance(dlA);
        double distanceC = dlA.getEuclideanDistance(dlB);

        double theta = Math.acos((Math.pow(distanceA, 2) + Math.pow(distanceC, 2) - Math.pow(distanceB, 2)) / (2 * distanceC * distanceA));

        if (negativeAngle) return Math.round((180.0 - Math.toDegrees(theta)) * 100.0) / 100;
        return Math.round(Math.toDegrees(theta) * 100.0) / 100.0;
    }


    public static double calculateAngle3Points(ObjectDetection dlA, ObjectDetection dlB, ObjectDetection dlC, boolean negativeAngle) {
        return calculateAngle3Points(dlA.getDetectedLocation(), dlB.getDetectedLocation(), dlC.getDetectedLocation(), negativeAngle);
    }


    public static double clip(double in) {
        return Math.min(Math.max(in, 0), 1);
    }

    public static double clip(double in, double rad) {
        return Math.min(Math.max(in, 0 + rad), 1 - rad);
    }

    public static double clip(double in, double max, double min) {
        return Math.min(Math.max(in, min), max);
    }

    public static double getMean(double val1, double val2) {
        return ((val1 + val2) / 2);
    }

    public static double getMean(double... numbers) {
        double v = 0;
        for (double number : numbers) {
            v += number;
        }
        return v / numbers.length;
    }

}
