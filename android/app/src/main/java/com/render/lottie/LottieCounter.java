package com.render.lottie;

import com.airbnb.lottie.LottieAnimationView;
import com.exercises.base.exercise.ExerciseActivity;

public class LottieCounter extends LottieBase<LottieCounter> {
    LottieAnimationView counter;

    public LottieCounter() {
        super(0, ExerciseActivity.render.getViewBinding().lavScore);
        this.minFrame(10).maxFrame(30).ephemeral(false);

    }
}
