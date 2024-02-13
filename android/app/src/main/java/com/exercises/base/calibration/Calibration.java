package com.exercises.base.calibration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.SystemClock;

import com.detection.ObjectDetection;
import com.exercises.base.exercise.AbstractExercise;
import com.jogo.R;
import com.location.DetectionLocation;
import com.logger.SLOG;
import com.render.lottie.LottieCalibration;
import com.render.lottie.LottieRender;
import com.utils.JOGOPaint;
import com.utils.sensors.callbackManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;


public class Calibration {
    public static final float TOP_SCREEN = 0.1f;
    public static final float BOTTOM_SCREEN = 0.98f;
    public static final float LEFT_SCREEN = 0.4f;
    public static final float RIGHT_SCREEN = 0.6f;
    protected final List<ObjectDetection> detections = new ArrayList<>();
    protected final List<Predicate<DetectionLocation>> extraChecks = new ArrayList<>();
    public boolean keepBitmapAspectRation = true;
    public float rotationSensorY = 0;
    public float rotationSensorZ = 0;
    protected int CALIBRATIONTIME = 1000;
    protected float topScreen = TOP_SCREEN;
    protected float bottomScreen = BOTTOM_SCREEN;
    protected float leftScreen = LEFT_SCREEN;
    protected float rightScreen = RIGHT_SCREEN;

    protected float calibrationImageHeight;
    protected float calibrationImageWidth;
    protected Bitmap calibrationBitmap;
    protected LottieCalibration animation;
    protected String screenMessage = "";
    protected float xScreenMessage = 0.5f;
    protected float yScreenMessage = 0.5f;
    protected int textSize = 100;
    protected AbstractExercise abstractExercise;
    protected Context context;
    protected boolean secondAnimationPlay = false;
    protected long time = 0;
    protected LottieRender tiltUp;
    protected LottieRender tiltDown;
    protected LottieRender rotateLeft;
    protected LottieRender rotateRight;
    protected Long calibratedSince = null;
    protected Long rotatedSince = null;
    protected JOGOPaint textPaint;
    boolean isbitmap;
    double rotationZHigh;
    double rotationZLow;
    double rotationYHigh = 25;
    double rotationYLow = 17;
    boolean scaleY;
    boolean alignTop = true;
    callbackManager sensorListener = null;
    JOGOPaint redPaint = new JOGOPaint();
    JOGOPaint yellowPaint = new JOGOPaint();
    private boolean isRotationCalibrated = false;

    {
        redPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));
        yellowPaint.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#DBFF00"), PorterDuff.Mode.SRC_ATOP));

    }

    public Calibration(Bitmap calibrationBitmap) {
        this(calibrationBitmap, TOP_SCREEN, BOTTOM_SCREEN, LEFT_SCREEN, RIGHT_SCREEN, true);
    }

    public Calibration(Bitmap calibrationBitmap, float topScreen, float bottomScreen, float leftScreen, float rightScreen) {
        this(calibrationBitmap, topScreen, bottomScreen, leftScreen, rightScreen, true);
    }

    public Calibration(LottieCalibration animation, float topScreen, float bottomScreen, float leftScreen, float rightScreen) {
        this(animation, topScreen, bottomScreen, leftScreen, rightScreen, true);
    }

    public Calibration(Bitmap calibrationBitmap, float topScreen, float bottomScreen, float leftScreen, float rightScreen, boolean scaleY) {
        this(topScreen, bottomScreen, leftScreen, rightScreen, scaleY);
        isbitmap = true;
        this.calibrationBitmap = calibrationBitmap;
    }

    public Calibration(LottieCalibration animation) {
        this(animation, TOP_SCREEN, BOTTOM_SCREEN, LEFT_SCREEN, RIGHT_SCREEN);
    }

    public Calibration(LottieCalibration animation, float topScreen, float bottomScreen, float leftScreen, float rightScreen, boolean scaleY) {
        this(topScreen, bottomScreen, leftScreen, rightScreen, scaleY);
        // animation calibration logic
        this.animation = animation;

        calibrationImageHeight = bottomScreen - topScreen;
        calibrationImageWidth = rightScreen - leftScreen;

        this.animation.setAnimationSize(0.8f, 0.88f);
    }


    public Calibration(float topScreen, float bottomScreen, float leftScreen, float rightScreen, boolean scaleY) {
        this.topScreen = topScreen;
        this.bottomScreen = bottomScreen;
        this.leftScreen = leftScreen;
        this.rightScreen = rightScreen;
        this.scaleY = scaleY;
        boolean isLandscape = eActivity.isOrientationLandscape();
        eActivity.getCameraFacing();
        initDirectionLottie();

        // landscape default sensor values
        if (isLandscape) {
            rotationZHigh = -80;
            rotationZLow = -95;

            rotationYHigh = 25;
            rotationYLow = 5;
        } else {
            rotationZHigh = 8;
            rotationZLow = -8;

            rotationYHigh = 30;
            rotationYLow = 9;
        }
    }

    public void setYCalibration(double low, double high) {
        rotationYHigh = high;
        rotationYLow = low;
    }

    public void setCorrectLottiePosition(double x, double y) {
        animation.getSecondAnimation().setNormalizedPosition(x, y);
    }

    public void setZCalibration(double low, double high) {
        rotationZHigh = high;
        rotationZLow = low;
    }

    public void setCalibrationSize(float top, float bottom, float left, float right) {
        topScreen = top;
        bottomScreen = bottom;
        leftScreen = left;
        rightScreen = right;

    }

    protected void initDirectionLottie() {
        tiltUp = new LottieRender(R.raw.tilt_up).ephemeral(false).setScale(0.5f, 0.5f).setNormalizedPosition(0.5, 0.25).loop();
        tiltDown = new LottieRender(R.raw.tilt_down).ephemeral(false).setScale(0.5f, 0.5f).setNormalizedPosition(0.5, 0.75).loop();
        rotateLeft = new LottieRender(R.raw.rotate_left).ephemeral(false).setScale(0.5f, 0.5f).setNormalizedPosition(0.1, 0.5).loop();
        rotateRight = new LottieRender(R.raw.rotate_right).ephemeral(false).setScale(0.5f, 0.5f).setNormalizedPosition(0.9, 0.5).loop();

    }

    protected void hideAllDirectionLottie() {
        tiltUp.hide();
        tiltDown.hide();

        rotateLeft.hide();
        rotateRight.hide();
    }

    protected void deleteAllDirectionLottie() {
        if (tiltUp == null) return;

        tiltUp.delete();
        tiltDown.delete();

        rotateLeft.delete();
        rotateRight.delete();
    }

    public void addExtraCheck(Predicate<DetectionLocation> p) {
        //add an extra check, e.g. maxBallSize
        extraChecks.add(p);
    }

    public void setKeepBitmapAspectRation(boolean keepBitmapAspectRation) {
        this.keepBitmapAspectRation = keepBitmapAspectRation;
    }

    public void setAlignTop(boolean alignTop) {
        this.alignTop = alignTop;
    }

    public void setAbstractExercise(AbstractExercise abstractExercise) {
        this.abstractExercise = abstractExercise;
    }

    protected boolean isInsideBox(DetectionLocation detection) {
        boolean isInside = bottomScreen > detection.getY() && topScreen < detection.getY() && leftScreen < detection.getX() && rightScreen > detection.getX();
        SLOG.d("isInside:" + isInside + " " + detection);
        return isInside;
    }

    protected boolean detectionsCalibrated() {
        //check if all detections are calibrated
        detections.forEach(d -> {
            if (d.getDetectedLocation() == null) SLOG.d("MISSING: " + d.getLabel());
        });

        return detections.stream().map(d -> {
            SLOG.d("stream " + d.getLabel());
            return d.getDetectedLocation();
        }).allMatch(
                (d -> (d != null) &&
                        extraChecks.stream().allMatch((p) -> p.test(d))
                        && isInsideBox(d)));
    }

    private void sensorCallback(float[] orientations) {

        rotationSensorY = orientations[1];
        rotationSensorZ = orientations[2];


        SLOG.d("calibration", "sensorY: " + rotationSensorY + " sensorZ: " + rotationSensorZ);

        if (rotationSensorY < rotationYHigh && rotationSensorY > rotationYLow &&
                rotationSensorZ < rotationZHigh && rotationSensorZ > rotationZLow) {
            if (!isRotationCalibrated && !isbitmap) {
                animation.getFirstAnimation().play();
                secondAnimationPlay = false;
            }
            isRotationCalibrated = true;
            screenMessage = "Move into\nthe lines";

            hideAllDirectionLottie();

        } else {
            if (!isbitmap) {
                animation.hide();
            }
            isRotationCalibrated = false;
            rotatedSince = null;
            screenMessage = "";
            calibratedSince = null;

            int camera = eActivity.getCameraFacing();
            SLOG.d("camera: " + camera);

            switch (camera) {
                case 1:
                    if (rotationSensorY > rotationYHigh) {
                        // tilt up
                        tiltUp.showAnimationContainer();
                        tiltDown.hide();
                    } else if (rotationSensorY < rotationYLow) {
                        // tilt down
                        tiltDown.showAnimationContainer();
                        tiltUp.hide();
                    } else {
                        tiltUp.hide();
                        tiltDown.hide();
                    }
                    break;
                case 0:
                    if (rotationSensorY < rotationYLow) {
                        // tilt up
                        tiltUp.showAnimationContainer();
                        tiltDown.hide();
                    } else if (rotationSensorY > rotationYHigh) {
                        // tilt down
                        tiltDown.showAnimationContainer();
                        tiltUp.hide();
                    } else {
                        tiltUp.hide();
                        tiltDown.hide();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + camera);
            }


            if (rotationSensorZ > rotationZHigh) {
                // rotate left
                rotateLeft.showAnimationContainer();
                rotateRight.hide();

            } else if (rotationSensorZ < rotationZLow) {
                // rotate right
                rotateRight.showAnimationContainer();
                rotateLeft.hide();
            } else {
                rotateLeft.hide();
                rotateRight.hide();
            }

        }

    }

    private void setupSensor() {
        sensorListener = eActivity.sensorManager.getXYZOrientation(this::sensorCallback);
    }

    public void processCalibration() {

        if (sensorListener == null) {
            setupSensor();
        }
        if (!detectionsCalibrated() || !isRotationCalibrated) {
            calibratedSince = null;
        } else {
            if (calibratedSince == null) {
                if (!isbitmap && !secondAnimationPlay) {
                    animation.getSecondAnimation().play();
                    secondAnimationPlay = true;
                }
                calibratedSince = SystemClock.elapsedRealtime();
                cleanup();
            }
        }
        if (isCalibrated() && !isbitmap) {
            animation.delete();
        }


    }

    public void cleanup() {
        // unregister listener
        if (sensorListener != null) {
            sensorListener.cancel();
            deleteAllDirectionLottie();
        }
    }

    public boolean isCalibrated() {
        return calibratedSince != null && (calibratedSince + CALIBRATIONTIME) < SystemClock.elapsedRealtime() && isRotationCalibrated;
    }

    protected void drawCalibrationImage(Canvas canvas) {
        JOGOPaint paint = calibratedSince == null ? redPaint : yellowPaint;
        Bitmap scaledBitmap;
        if (keepBitmapAspectRation) {
            if (scaleY) {
                float scaleByY = (topScreen - bottomScreen) * canvas.getHeight() / calibrationBitmap.getHeight();
                scaledBitmap = Bitmap.createScaledBitmap(calibrationBitmap, (int) (scaleByY * calibrationBitmap.getWidth()), (int) ((bottomScreen - topScreen) * canvas.getHeight()), false);
            } else {
                float scaleByX = (rightScreen - leftScreen) * canvas.getWidth() / calibrationBitmap.getWidth();
                scaledBitmap = Bitmap.createScaledBitmap(calibrationBitmap, (int) ((rightScreen - leftScreen) * canvas.getWidth()), (int) (scaleByX * calibrationBitmap.getHeight()), false);
            }
        } else {
            scaledBitmap = Bitmap.createScaledBitmap(calibrationBitmap, (int) ((rightScreen - leftScreen) * canvas.getWidth()), (int) ((bottomScreen - topScreen) * canvas.getHeight()), false);
        }

        if (alignTop) {
            canvas.drawBitmap(scaledBitmap, (rightScreen + leftScreen) / 2f * canvas.getWidth() - scaledBitmap.getWidth() / 2f, topScreen * canvas.getHeight(), paint);
        } else {
            canvas.drawBitmap(scaledBitmap, (rightScreen + leftScreen) / 2f * canvas.getWidth() - scaledBitmap.getWidth() / 2f, bottomScreen * canvas.getHeight() - scaledBitmap.getHeight(), paint);
        }
    }

    protected void drawCalibrationText(Canvas canvas) {
        float y = yScreenMessage;
        if (textPaint == null)
            textPaint = new JOGOPaint().white().textSize(textSize).align(Paint.Align.CENTER).bioSansBold(eActivity.getAssets());

        for (String line : screenMessage.split("\n")) {
            canvas.drawText(line, xScreenMessage * canvas.getWidth(), yScreenMessage * canvas.getHeight() + y, textPaint);
            y += textPaint.descent() - textPaint.ascent();
        }
    }

    public void draw(Canvas canvas) {
        if (isRotationCalibrated) {
            if (isbitmap) {
                drawCalibrationImage(canvas);
            }
        } else
            detections.forEach(detection -> detection.draw(canvas));
        drawCalibrationText(canvas);

    }

    public void drawDebug(Canvas canvas) {
        detections.forEach(detection -> detection.drawDebug(canvas));
        canvas.drawText("calibrated: " + isCalibrated(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint);
        canvas.drawText("rotation y: " + rotationSensorY, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().small().monospace());
        canvas.drawText("rotation z: " + rotationSensorZ, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().small().monospace());
        canvas.drawText("screen msg: " + screenMessage, JOGOPaint.xValue, JOGOPaint.getNewDrawDebugHeight(), new JOGOPaint().red().small().monospace());
    }

    public void addObjectDetection(ObjectDetection detection) {
        detections.add(detection);
    }


}

