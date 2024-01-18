package com.render;

import com.exercises.base.exercise.ExerciseClient;

public class RestartButton extends Button {

    public RestartButton(android.widget.Button button) {
        super(button);
    }

    @Override
    public void execute() {
        ExerciseClient.getClient().restartExercise();
    }

}
