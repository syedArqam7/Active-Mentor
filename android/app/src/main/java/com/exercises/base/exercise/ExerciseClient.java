package com.exercises.base.exercise;

import android.graphics.Canvas;
import android.graphics.Color;

import com.exercises.score.Score;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.jogo.NativeBridge;
import com.jogo.R;
import com.logger.SLOG;
import com.render.CanDraw;
import com.render.lottie.LottieFinish;
import com.render.lottie.LottieRender;
import com.utils.ExerciseCompleteHelper;
import com.utils.InfoBlob;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Collections;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;
import static com.exercises.base.exercise.ExerciseActivity.render;

public class ExerciseClient implements CanDraw {

    private static ExerciseClient exerciseClient;
    protected static final String jsonDir = "dataExercises/";
    public Score score;
    public String jsonName;
    public String jsonPath;
    protected ExerciseSettings exerciseSettings;
    private boolean running;
    private final AbstractExercise exercise;
    public ExerciseClient(ExerciseSettings exerciseSettings) {
        this.exerciseSettings = exerciseSettings;
        exerciseClient = this;
        score = exerciseSettings.createScore();

        exercise = ExerciseHolder.mapExercise(exerciseSettings);
        prepareForJSON();
        running = true;

    }

    public static ExerciseClient getClient() {
        return exerciseClient;
    }

    public void startPromo() {
        // Don't want to re-animate this during restart.
        if (!eActivity.getIntent().getExtras().getBoolean("restart"))
            new LottieRender(R.raw.logo2).background(Color.BLACK).play();
            // new LottieRender(R.raw.active_promo).background(Color.BLACK).play();
    }

    public void start() {
        exercise.start();
        render.setExerciseDescription(exerciseSettings.getDescription());
        startPromo();
    }

    public void process(InfoBlob infoBlob) {
        exercise.process(infoBlob);
    }

    public void stop() {
        stopExercise();
    }


    public void stopExercise() {
        if (!running) return;
        running = false;
        score.stop();
        exercise.stop();
        // if (NativeBridge.isToJson()) writeToJSON(); //save data to CSV - for analysis
        exerciseCompleteHelper();
        // end the exercise
        new LottieFinish().onEnd(eActivity::stopExercise).play();
    }

    /**
     * Mainly for restart and stop functionality.
     */
    public boolean isExerciseStatusCountDown() {
        return exercise.isInCountDownState();
    }


    public void restartExercise() {
        if (!running) return;
        running = false;
        ExerciseActivity.eActivity.getIntent().putExtra("restart", true);
        new LottieFinish().onEnd(eActivity::restart).play();
    }


    public void exerciseCompleteHelper() {
        WritableMap exerciseMap = Arguments.createMap();
        exerciseMap.putMap(exercise.getName(), getEndOfExerciseStats());

        WritableMap returnMap = Arguments.createMap();
        returnMap.putMap("exercises", exerciseMap);
        SLOG.d("ai-complete", returnMap.toString());

        new ExerciseCompleteHelper().sendEvent(eActivity.getreactContext(), "ai-complete", returnMap);
    }

    public WritableMap getEndOfExerciseStats() {
        WritableMap map = Arguments.createMap();
        map.putMap("score", score.toMap());
        map.putString("json-path", jsonPath);
        map.putString("exercise-name", exercise.getName());
        return map;
    }

    private void prepareForJSON() {
        if (!NativeBridge.isToJson()) return;

        jsonName = exercise.getName() + new Timestamp(System.currentTimeMillis()) + ".json";
        jsonPath = eActivity.getExternalFilesDir(jsonDir).getAbsolutePath() + "/" + jsonName;

        File directory = eActivity.getExternalFilesDir(jsonDir);
        if (!directory.exists() && !directory.mkdirs()) {
            SLOG.e("EClient Exception MKDIR");
            throw new SecurityException("cannot create dir");
        }
    }



    private void writeToJSON() {

        try {
            JSONObject exerciseData = new JSONObject();
            // all keys in snake_case (python)
            exerciseData.put("score_type", exerciseSettings.getScoreType());
            exerciseData.put("json_path", jsonPath);
            exerciseData.put("video_path", eActivity.getOutputMediaFile().getAbsolutePath());
            score.writeToJSON(exerciseData);
            exercise.writeToJSON(exerciseData);
            Files.write(Paths.get(jsonPath), Collections.singleton(exerciseData.toString()));
        } catch (JSONException | IOException e) {
            SLOG.e("writeToJSON error! " + e.getMessage());
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        if (exercise != null) exercise.draw(canvas);
    }

    @Override
    public void drawDebug(Canvas canvas) {
        if (exercise != null) exercise.drawDebug(canvas);
    }

    public AbstractExercise getExercise() {
        return exercise;
    }
}

