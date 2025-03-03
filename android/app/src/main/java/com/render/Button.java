package com.render;

import android.view.View;

import com.exercises.base.exercise.ExerciseActivity;
import com.exercises.base.exercise.ExerciseClient;
import com.render.sounds.SoundBase;
import com.utils.sensors.callbackManager;

import java.util.TimerTask;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;

public abstract class Button {

    protected static final long BUTTON_DELAY = 800;
    protected static final double SPEED_THRESHOLD = 0.5;
    protected TimerTask buttonTask;
    protected callbackManager buttonListener;
    protected boolean running = false;
    protected android.widget.Button button;

    public Button(android.widget.Button button) {
        this.button = button;
        this.initButtonListener();
        this.setupOnClickButton();
    }

    private void hideButton() {
        if (eActivity == null) return;
        if (!running) return;
        eActivity.runOnUiThread(() -> button.setVisibility(View.GONE));
        buttonTask = null;
    }

    private void activateButton() {
        if (!running) return;
        if (buttonTask != null) buttonTask.cancel();
        buttonTask = ExerciseActivity.timer.schedule(this::hideButton, BUTTON_DELAY);
        eActivity.runOnUiThread(() -> button.setVisibility(View.VISIBLE));
    }

    private void initButtonListener() {
        running = true;
        buttonListener = eActivity.sensorManager.onMotion((v) -> activateButton(), SPEED_THRESHOLD);
    }

    public void stop() {
        running = false;
        buttonListener.cancel();
    }


    private void setupOnClickButton() {
        if (!running) return;
        button.setOnClickListener(view -> {
            buttonListener.cancel();
            hideButton();
            if (ExerciseClient.getClient().isExerciseStatusCountDown()) {
                ExerciseActivity.render.getViewBinding().containerCountDown.setVisibility(View.INVISIBLE);
                SoundBase.stopActivePlayers();
            }
            execute();
        });
    }

    public abstract void execute();

}

