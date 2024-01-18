package com.utils;

import android.os.SystemClock;

public class InfoBlob {
    private final int frameID;
    private final long startTime = SystemClock.elapsedRealtime();
    private final int screenOrientation;
    private long doneProcessingTimestamp;
    String infostring = "";
    private final int width;
    private final int height;

    public InfoBlob(ExtendedMLImage extendedMLImage) {
        this.frameID = extendedMLImage.getFrameID();
        width = extendedMLImage.getMlImage().getHeight();
        height = extendedMLImage.getMlImage().getWidth();
        screenOrientation = extendedMLImage.screenOrientation;
    }

    public int getOrientedWidth() {
        return (getScreenOrientation() % 90 == 0) ? getHeight() : getWidth();
    }

    public int getOrientedHeight() {
        return (getScreenOrientation() % 90 == 0) ? getWidth() : getHeight();

    }

    public long sinceStart() {
        return SystemClock.elapsedRealtime() - getStartTime();
    }

    public void doneProcessing() {
        setDoneProcessingTimestamp(SystemClock.elapsedRealtime());
    }

    public void addInfo(String info) {
        infostring += info + ",";
    }

    @Override
    public String toString() {
        return "InfoBlob{" +
                "frameID=" + getFrameID() +
                ", startTime=" + getStartTime() +
                ", infostring='" + infostring + '\'' +
                '}';
    }

    public int getFrameID() {
        return frameID;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public long getDoneProcessingTimestamp() {
        return doneProcessingTimestamp;
    }

    public void setDoneProcessingTimestamp(long doneProcessingTimestamp) {
        this.doneProcessingTimestamp = doneProcessingTimestamp;
    }
}
