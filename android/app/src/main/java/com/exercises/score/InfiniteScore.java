package com.exercises.score;

public class InfiniteScore extends Score {

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
}
