package com.exercises.base.exercise;

import android.graphics.Canvas;
import android.os.Bundle;

import com.activities.CameraActivity;
import com.jogo.NativeBridge;
import com.jogo.R;
import com.render.Render;
import com.utils.Gear;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.STimer;
import com.utils.sensors.SSensorManager;
import com.views.OverlayView;


abstract public class ExerciseActivity extends CameraActivity {


    public static ExerciseActivity eActivity;
    public static Render render;

    public static STimer timer;
    public SSensorManager sensorManager;
    //todo remove this
    public int exerciseIndex;
    /**
     * exercise Client
     **/
    protected ExerciseClient exerciseClient;
    protected OverlayView trackingOverlay;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // static
        timer = new STimer(false);
        eActivity = this;
        render = new Render();

        //singletons
        sensorManager = new SSensorManager(this);
        exerciseIndex = getIntent().getExtras().getInt("index", -1);
    }

    public void onStart() {
        super.onStart();
        startExerciseClient();
        running = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.stop();
        render.stop();
        exerciseClient.stop();
        exerciseClient = null;
        eActivity = null;
        render = null;
        timer.cancel();
    }

    //can be overwritten by subclass if we want to load next exercise instead of stop exercise.
    public void stopExercise() {
        running = false;
        finish();
    }

    public void restart() {
        running = false;
        this.recreate();
    }

    @Override
    public void onImageProcessed(InfoBlob infoBlob) {
        if (!running) return;
        exerciseClient.process(infoBlob);
    }

    public void startExerciseClient() {
        exerciseClient = new ExerciseClient(exerciseSettings);
        exerciseClient.start();
    }

    @Override
    protected void initViews() {
        render.initViews();
        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(this::exerciseDraw);
    }


    public void exerciseDraw(Canvas canvas) {
        trackingOverlay.postInvalidate();
        exerciseClient.draw(canvas);
        if (Gear.iff("debug", true) && NativeBridge.isDebug()) {
            JOGOPaint.resetDrawDebugHeight();
            exerciseClient.drawDebug(canvas);
        }
    }

    protected String getName() {
        return "exercise_" + exerciseIndex;
    }

}
