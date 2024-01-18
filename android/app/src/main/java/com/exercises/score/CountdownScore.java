package com.exercises.score;

public class CountdownScore extends Score {


    // time should be in long and always in millisecond
    public CountdownScore(long time) {
        super();
        this.time = time;
        countdown = true;
    }

    @Override
    protected void initView() {
        render.showScore();
        render.showTimer();
        render.initiateCountDownSound();
    }

    @Override
    public void start() {
        super.start();
        startCountdownTime(time);
        sClock.scheduleOnTime(time, this::stopExercise);

    }

}
