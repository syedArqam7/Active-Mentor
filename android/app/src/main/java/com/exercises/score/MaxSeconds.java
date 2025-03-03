package com.exercises.score;


public class MaxSeconds extends Score {
    protected boolean running = false;

    @Override
    protected void initView() {
        //can be overridden if required
    }

    @Override
    public void start() {
        super.start();
        startNormalTime();
        render.showTimer();
    }


}
