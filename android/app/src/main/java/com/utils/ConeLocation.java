package com.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;

import com.location.Location;
import com.logger.SLOG;
import com.render.lottie.LottieCone;

public class ConeLocation extends Location {
    long fallOverTime = 0;
    boolean fallOver = false;
    private double radius;
    private boolean active = false;
    private LottieCone lottieCone;
    private final boolean fallOverLeft;

    public ConeLocation(double radius, double centerX, double centerY, boolean fallOverLeft) {
        super(centerX, centerY);
        this.radius = radius;
        this.fallOverLeft = fallOverLeft;

        initCones();

    }

    private void initCones() {
        lottieCone = new LottieCone();
    }

    public boolean isActive() {
        return active;
    }

    public ConeLocation activate() {
        this.active = true;
        return this;
    }

    public void reset() {
        fallOver = false;
        lottieCone.reset();
    }

    public void setFallOverDirection(boolean fallOverLeft) {
        fallOver = true;
        fallOverTime = SystemClock.elapsedRealtime();
        lottieCone.fallOver(fallOverLeft);
    }

    public void fallOver() {
        setFallOverDirection(this.fallOverLeft);
    }

    public boolean isFallOver() {
        return fallOver;
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void setY(double y) {
        lottieCone.setNormalizedPosition(x, y);
        super.setY(y);
    }


    @Override
    public void draw(Canvas canvas) {
//can be overridden if required
    }

    @Override
    public void drawDebug(Canvas canvas) {
        Paint jogoPaint = new Paint();
        SLOG.d(String.valueOf(this));
        jogoPaint.setColor(Color.RED);
        jogoPaint.setStrokeWidth(10);
        jogoPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) getX() * canvas.getWidth(), (float) (getY() * canvas.getHeight()), (float) radius * canvas.getWidth(), jogoPaint);
    }

}




