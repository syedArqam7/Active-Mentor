package com.render;

import com.exercises.base.exercise.ExerciseClient;

public class StopButton extends Button {

    public StopButton(android.widget.Button button) {
        super(button);
    }

    @Override
    public void execute() {
        ExerciseClient.getClient().stopExercise();
    }

}
