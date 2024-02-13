package com.render.lottie;

import android.view.View;
import android.widget.ImageView;
import android.graphics.Color;

import com.exercises.base.exercise.ExerciseActivity;
import com.jogo.R;
import com.render.sounds.SoundRender;

public class LottieCountDown extends LottieBase<LottieCountDown> {

    LottieRender preload;
    LottieRender postLoad;
    // TODO: Code cleanup required
    public LottieCountDown() {
        // super(0, ExerciseActivity.render.getViewBinding().lavCountDown);
        super(R.raw.active_countdown);
        ephemeral(true);
        background(Color.BLACK);
        
        onStart(this::startSound);

        preload = createPreload();
        preload.onEnd(super::play);
        //onstart is not called, because the view is hidden
        // preload.onEnd(this::showContainer);

        // onEnd(this::hideContainer);

        // postLoad = createPostLoad();
        // postLoad.onEnd(this::showUI);
        // onEnd(postLoad::play);

        // maxFrame(193);
        // setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public LottieCountDown play() {
        preload.play();
        return this;
    }

    private LottieRender createPreload() {
        return new LottieRender(R.raw.green_fade_in_out)
                .setScaleType(ImageView.ScaleType.FIT_XY)
                .minFrame(5).maxFrame(20)
                .speed(1.5f).ephemeral(true);
    }

    private LottieRender createPostLoad() {
        return new LottieRender(R.raw.green_fade_in_out)
                .setScaleType(ImageView.ScaleType.FIT_XY)
                .minFrame(85).maxFrame(95)
                .speed(1.5f).ephemeral(true);
    }

    private void hideContainer() {
        ExerciseActivity.render.getViewBinding().containerCountDown.setVisibility(View.GONE);
    }

    private void showContainer() {
        viewBinding.containerCountDown.setVisibility(View.VISIBLE);
    }

    private void startSound() {
        new SoundRender(R.raw.count_down_jogo).play();
    }

}
