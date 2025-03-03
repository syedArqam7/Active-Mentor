package com.location;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MissingLocation extends DetectionLocation {

    //todo how should we handle this class.
    public MissingLocation(int frameID, String classlabel) {
        super(classlabel, 0, 0, frameID, 0, STATUS.MISSING);
        process();
    }

    @Override
    public void draw(Canvas canvas) {
//can be overridden if required
    }

    @Override
    public void drawDebug(Canvas canvas) {
//can be overridden if required
    }


    public void updateLocation(double centerX, double centerY, STATUS status) {
        this.x = centerX;
        this.y = centerY;
        this.status = status;
    }

    @Override
    public void drawDebug(Canvas canvas, Paint paint) {
//can be overridden if required
    }

    @Override
    public double getRadius() {
        return 0;
    }
}
