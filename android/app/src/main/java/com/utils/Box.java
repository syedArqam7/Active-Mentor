package com.utils;

import android.graphics.Canvas;

import com.detection.person.PersonDetection;
import com.location.Location;

import java.util.Random;


public class Box extends Location {

    private final Random rand = new Random();
    private double startX;
    private double startY;
    private final double height;
    private double currentWidth;
    private double currentHeight;
    private int index;
    private int lastJumpFrame;
    private static final int MIN_JUMP_GAP = 4;
    private STimer timer;

    public Box() {
        super(0, 0);
        this.startX = 0;
        this.startY = 0;
        this.height = 0;
        this.currentHeight = 0;
        this.currentWidth = 0;
        this.lastJumpFrame = 0;
        this.index = -1;  // the index of temp boxes will be -1
    }

    public Box(double startX, double startY, double width, double height, int index, STimer timer) {

        super(startX, startY);
        this.startX = startX;
        this.startY = startY;
        this.height = height;
        this.currentWidth = width;
        this.currentHeight = height;
        this.index = index;
        this.lastJumpFrame = 0;
        this.timer = timer;
    }

    public void startGrowGrass(int delay) {
        this.timer.scheduleInfinite(this::randomIncrease, delay);
    }

    public void randomIncrease() {
        double RANGEMIN = 0.0, RANGEMAX = 0.0015;
        this.setHeight((this.getHeight()) - (RANGEMIN + (RANGEMAX - RANGEMIN) * rand.nextDouble()));
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getWidth() {
        return currentWidth;
    }

    public void setWidth(double width) {
        this.currentWidth = width;
    }

    public double getHeight() {
        return currentHeight;
    }

    public void setHeight(double height) {
        this.currentHeight = height;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isPersonIn(PersonDetection person) {
        //double ankleX = SMath.getMeanX(person.leftLeg.ankle.getDetectedLocation(), person.rightLeg.ankle.getDetectedLocation());
        double rightAnkleX = person.rightLeg.ankle.getDetectedLocation().getX();
        double leftAnkleX = person.leftLeg.ankle.getDetectedLocation().getX();
        return ((rightAnkleX >= this.startX && rightAnkleX <= this.startX + this.currentWidth) || (leftAnkleX >= this.startX && leftAnkleX <= this.startX + this.currentWidth));
    }

    public boolean haveJumped(PersonDetection person) {
        int testJump = person.didJump(lastJumpFrame);
        if (testJump != lastJumpFrame && (testJump - lastJumpFrame) >= MIN_JUMP_GAP) {
            this.lastJumpFrame = testJump;
            return true;
        } else {
            return false;
        }
    }

    public boolean isInAndJumped(PersonDetection person) {

        boolean isSolved = isPersonIn(person) && haveJumped(person);
        if (isSolved && !(this.isCurrentHeightLower())) {
            this.currentHeight = this.currentHeight + 0.05;
        }
        return (isSolved);
    }

    public boolean isCurrentHeightLower() {
        return (this.currentHeight > this.height);
    }

    public boolean isLimitReached(double limit) {
        return (this.getHeight() <= limit);
    }


    public void drawBox(Canvas canvas, JOGOPaint boxPaint, JOGOPaint labelPaint) {  // label = -1 means no label
        canvas.drawRect((float) (this.startX) * canvas.getWidth(), (float) (this.startY) * canvas.getHeight(), (float) (this.startX + this.currentWidth) * canvas.getWidth(), (float) (this.startY + this.currentHeight) * canvas.getHeight(), boxPaint);
        canvas.drawText((this.index + 1) + "", (float) (this.startX + (this.currentWidth / 2)) * canvas.getWidth(), (float) (((this.startY + this.currentHeight) + this.startY) / 2) * canvas.getHeight(), labelPaint);
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public void draw(Canvas canvas) {
//can be overridden if required
    }

    @Override
    public void drawDebug(Canvas canvas) {
//can be overridden if required
    }
}
