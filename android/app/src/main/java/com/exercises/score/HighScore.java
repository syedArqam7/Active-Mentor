package com.exercises.score;

public class HighScore extends Score {
    protected int highScore;

    public HighScore(int highScore) {
        super();
        this.highScore = highScore;
    }

    @Override
    protected void initView() {
        render.showScore();
        render.showTimer();
    }

    @Override
    public void setCount(int count, boolean blink) {
        super.setCount(count, blink);
        if (count > highScore) {
            highScore = count;
            render.setHighScore(highScore);
        }
    }

    @Override
    public int getFinalScore() {
        super.getFinalScore();
        return highScore;
    }

    @Override
    public void start() {
        super.start();
        startNormalTime();
    }
}
