package com.exercises.score;

public class NRepetitions extends Score {
    final int n;

    public NRepetitions(int n) {
        super();
        this.n = n;
        render.initiateNRepsSound(n);
    }

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
    public void setCount(int count, boolean blink) {
        super.setCount(count, blink);
        if (count == n) stopExercise();
    }

}
