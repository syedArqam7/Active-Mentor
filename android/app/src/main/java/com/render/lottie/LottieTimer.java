package com.render.lottie;

import com.airbnb.lottie.LottieAnimationView;
import com.exercises.base.exercise.ExerciseActivity;

public class LottieTimer extends LottieBase<LottieTimer> {
    LottieAnimationView counter;

    public LottieTimer() {
        super(0, ExerciseActivity.render.getViewBinding().lavTimer);
        ephemeral(false);
    }
}
