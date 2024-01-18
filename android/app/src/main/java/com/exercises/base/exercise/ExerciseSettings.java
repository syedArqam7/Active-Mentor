package com.exercises.base.exercise;

import android.hardware.camera2.CameraCharacteristics;

import com.exercises.score.CountdownScore;
import com.exercises.score.HighScore;
import com.exercises.score.InfiniteScore;
import com.exercises.score.MaxSeconds;
import com.exercises.score.NRepetitions;
import com.exercises.score.Score;
import com.exercises.score.TillFailureScore;
import com.exercises.score.TimerScore;
import com.google.gson.annotations.SerializedName;

public class ExerciseSettings {
    //todo exposing non-final attributes like this is bad practice
    public long TIME_IN_ALERT = 5000L;
    @SerializedName("description")
    public String description;
    @SerializedName("scoreType")
    public ScoreType scoreType;
    @SerializedName("score")
    public int score = 0;
    @SerializedName("highScore")
    public int highScore = 0;
    @SerializedName("countDownMiliSeconds")
    public long countDownMiliSeconds = 0L;


    @SerializedName("nSeconds")
    public long nSeconds = 3_000_0L;
    @SerializedName("selectedCameraFacing")
    public CameraFacing selectedCameraFacing;
    @SerializedName("exerciseVariation")
    public int exerciseVariation = 0;


    public ExerciseSettings(ScoreType scoreType, String description) {
        this.scoreType = scoreType;
        this.description = description;
    }

    public ExerciseSettings(ScoreType scoreType) {
        this(scoreType, "");
    }

    public CameraFacing getSelectedCameraFacing() {
        // If json is un parsable or camera facing is not present then CAMERA_LENS_FRONT will be the default camera facing
        return selectedCameraFacing == null ? CameraFacing.CAMERA_LENS_FRONT : selectedCameraFacing;
    }

    public boolean isBackCameraUsed() {
        return getSelectedCameraFacing() == ExerciseSettings.CameraFacing.CAMERA_LENS_BACK;
    }

    public Integer getSelectedCameraLens() {
        Integer selectedCameraFacing;
        if (isBackCameraUsed()) {
            selectedCameraFacing = CameraCharacteristics.LENS_FACING_BACK;
        } else {
            selectedCameraFacing = CameraCharacteristics.LENS_FACING_FRONT;
        }
        return selectedCameraFacing;
    }

    public long getCountDownMiliSeconds() {
        // 30000 is default value if the value is not present
        return countDownMiliSeconds == 0 ? 3_000_0 : countDownMiliSeconds;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getScore() {
        // 50 is default value is the score is not present
        return score;
    }

    public Score createScore() {
        switch (scoreType) {
            case COUNTDOWN:
                //todo fix this to come from exercisesettings
                return new CountdownScore(countDownMiliSeconds);
            case INFINITE:
            case NON_AI:
                return new InfiniteScore();
            case FAILURE:
                return new TillFailureScore();
            case TOTALREPETITIONS:
            case NREPETITIONS:
                return new NRepetitions(score);
            case HIGHSCORE:
                return new HighScore(highScore);
            case NSECONDS:
            case TOTAL:
                return new TimerScore(nSeconds);
            case MAXSECONDS:
                return new MaxSeconds();
            default:
                throw new IllegalStateException("No Relevant ScoreType Found!");
        }
    }

    public int getExerciseVariation() {
        return exerciseVariation;
    }

    @Override
    public String toString() {
        return "ExerciseSettings{" +
                "TIME_IN_ALERT=" + TIME_IN_ALERT +
                ", description='" + description + '\'' +
                ", scoreType=" + scoreType +
                ", score=" + score +
                ", highScore=" + highScore +
                ", countDownMiliSeconds=" + countDownMiliSeconds +
                ", nSeconds=" + nSeconds +
                ", selectedCameraFacing=" + selectedCameraFacing +
                ", exerciseVariation=" + exerciseVariation +
                '}';
    }

    public enum CameraFacing {
        @SerializedName("BACK")
        CAMERA_LENS_BACK,
        @SerializedName("FRONT")
        CAMERA_LENS_FRONT
    }

    public enum ScoreType {
        @SerializedName("COUNTDOWN")
        COUNTDOWN, //Do this for T MiliSeconds
        @SerializedName("INFINITE")
        INFINITE, //Do this until the exercise is canceled
        @SerializedName("FAILURE")
        FAILURE, //Do this until failure
        @SerializedName("NREPETITIONS")
        NREPETITIONS,
        @SerializedName("TOTALREPETITIONS")
        TOTALREPETITIONS,
        @SerializedName("HIGHSCORE")
        HIGHSCORE,
        @SerializedName("NON_AI")
        NON_AI,
        @SerializedName("NSECONDS")
        NSECONDS,
        @SerializedName("TOTAL")
        TOTAL,
        @SerializedName("MAXSECONDS")
        MAXSECONDS,

    }
}
