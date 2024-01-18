package com.exercises.PersonExercises.touchPoint;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Pair;

import com.detection.person.PersonDetection;
import com.exercises.score.Score;
import com.location.Location;
import com.render.Render;
import com.utils.JOGOPaint;

public class TouchPoint extends Location {

    final long initializationTime = SystemClock.elapsedRealtime();
    private static final float radius = 0.095f;
    PersonDetection person;
    Score score;
    Render render;
    int WAIT_TIME = 2500; // skip touchPoint after WAIT_TIME seconds
    // TOUCHPOINT GENERATION RANGES
    double X_MAX, Y_MAX, X_MIN, Y_MIN;
    double hiddenHandRadius = radius + 0.01;
    double hiddenFootRadius = radius + 0.012;
    JOGOPaint timerPaint = new JOGOPaint().jogoYellow().stroke().strokeWidth(20);
    private long resolutionTime;
    private STATUS status = STATUS.ACTIVE;

    public TouchPoint(PersonDetection person, Score score, Render render, RectF quadrantRectF) {
        super(0, 0);
        this.score = score;
        this.person = person;
        this.render = render;
        this.X_MIN = quadrantRectF.left;
        this.Y_MIN = quadrantRectF.top;
        this.X_MAX = quadrantRectF.right;
        this.Y_MAX = quadrantRectF.bottom;
        Pair<Double, Double> touchPoint = getRandomTouchPoint();
        this.x = touchPoint.first;
        this.y = touchPoint.second;
    }

    private Pair<Double, Double> getRandomTouchPoint() {
        double randX = X_MIN + ( Math.random() * ((X_MAX - X_MIN)));
        double randY = Y_MIN + (Math.random() * ((Y_MAX - Y_MIN)));
        return new Pair<>(randX, randY);
    }

    private long elapsedTimeSinceStart() {
        return SystemClock.elapsedRealtime() - initializationTime;
    }

    public long getResolvedTime() {
        return resolutionTime - initializationTime;
    }

    private void resolve() {

        this.status = STATUS.RESOLVED;
        resolutionTime = SystemClock.elapsedRealtime();
        incrementScore();
    }

    // increment score depending on how fast the person resolves the touchPoint
    private void incrementScore() {
        double POINTS_AWARD_THRESHOLD = 0.5;
        double resolvedTime = ((double) getResolvedTime() / (double) WAIT_TIME);
        if (resolvedTime >= POINTS_AWARD_THRESHOLD)
            incrementScore();
        else
            score.incrementCount(2);
    }

    private void skip() {
        this.status = STATUS.MISSED; //SKIPPED can also work
    }

    public boolean isActive() {
        return status == STATUS.ACTIVE;
    }

    public boolean isResolved() {
        return status == STATUS.RESOLVED;
    }

    public boolean isMissed() {
        return status == STATUS.MISSED;
    }

    private boolean didHandTouch() {

        return (this.checkIntersection(person.leftArm.index.getDetectedLocation(), hiddenHandRadius)
                || (this.checkIntersection(person.leftArm.wrist.getDetectedLocation(), hiddenHandRadius)) ||
                this.checkIntersection(person.rightArm.index.getDetectedLocation(), hiddenHandRadius) ||
                (this.checkIntersection(person.rightArm.wrist.getDetectedLocation(), hiddenHandRadius)
                ));
    }

    private boolean didFootTouch() {

        return this.checkIntersection(person.leftLeg.index.getDetectedLocation(), hiddenFootRadius) ||
                this.checkIntersection(person.leftLeg.ankle.getDetectedLocation(), hiddenFootRadius) ||
                this.checkIntersection(person.rightLeg.index.getDetectedLocation(), hiddenFootRadius) ||
                this.checkIntersection(person.rightLeg.ankle.getDetectedLocation(), hiddenFootRadius);
    }

    private boolean didFootOrHandTouch() {

        return (didFootTouch() || didHandTouch());
    }

    @Override
    public double getRadius() {
        return radius;
    }

    protected void processHandsMode() {

        if (elapsedTimeSinceStart() > WAIT_TIME) {
            this.skip();
            return;
        }

        if (didHandTouch()) {
            this.resolve();
        }

    }

    protected void processFeetMode() {
        if (elapsedTimeSinceStart() > WAIT_TIME) {
            this.skip();
            return;
        }

        if (didFootTouch()) {
            this.resolve();
        }

    }

    protected void processAnyMode() {
        if (elapsedTimeSinceStart() > WAIT_TIME) {
            this.skip();
            return;
        }

        if (didFootOrHandTouch()) {
            this.resolve();
        }

    }

    public double percentageOfFinalDraw() {
        return (SystemClock.elapsedRealtime() - initializationTime) / (double) WAIT_TIME;
    }

    protected void drawArc(Canvas canvas) {
        if (isActive())
            canvas.drawArc(canvX(canvas) - canvRad(canvas),
                    canvY(canvas) - canvRad(canvas),
                    canvX(canvas) + canvRad(canvas),
                    canvY(canvas) + canvRad(canvas),
                    270,
                    360 * (float) (1 - percentageOfFinalDraw()),
                    false, timerPaint);
    }


    @Override
    public void draw(Canvas canvas) {
        // inner red circle
        canvas.drawCircle(canvX(canvas), canvY(canvas), (float) (Math.min(canvas.getWidth(), canvas.getHeight()) * 0.09f), new JOGOPaint().black().fillStroke());
        drawArc(canvas);
    }

    @Override
    public void drawDebug(Canvas canvas) {
//can be overridden if required
    }

    enum STATUS {ACTIVE, RESOLVED, MISSED}

}
