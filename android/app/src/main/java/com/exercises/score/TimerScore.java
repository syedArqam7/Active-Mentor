package com.exercises.score;


public class TimerScore extends Score {
    protected boolean running = false;

    public TimerScore(long time) {
        super();
        this.time = time;
    }

    @Override
    protected void initView() {
        render.showTimer();
    }

    @Override
    public void start() {
        super.start();
        sClock.scheduleOnTime(time, this::stopExercise);
        startCountdownTime(time);
        render.initiateCountDownSound();

    }
}
