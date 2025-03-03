package com.exercises.score;

public class TillFailureScore extends Score {

    @Override
    protected void initView() {
        render.showScore();
        render.showTimer();
    }

    @Override
    public void start() {
        super.start();
        startNormalTime();
    }

    @Override
    public void resetCount() {
        stopExercise();
    }
}
