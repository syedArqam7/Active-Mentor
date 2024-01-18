package com.render.sounds;

import android.speech.tts.TextToSpeech;

import com.exercises.base.exercise.ExerciseActivity;
import com.logger.SLOG;

import java.util.Locale;

public class ScoreSoundRender {

    TextToSpeech mTTS;
    boolean running = false;

    // for repetition exercises
    int totalScore = 0;

    //for time based exercises
    int countDown = 5;

    String MINUTES = "minutes to go";
    String SECONDS = "seconds to go";
    String TO_GO = "to go";

    //Score Literals
    private static final int FIVE = 5;
    private static final int TEN = 10;
    private static final int TWENTY_FIVE = 25;
    private static final int FIFTY = 50;
    private static final int HUNDRED = 100;

    //Time Literals
    private static final int FIVE_MINS = 300;
    private static final int TWO_MINS = 120;
    private static final int ONE_MIN = 60;
    private static final int THIRTY_SEC = 30;
    private static final int TEN_SEC = 10;
    private static final int FIVE_SEC = 5;
    private static final int ONE_SEC = 1;


    final static int SCHEDULER_DELAY = 1000;
    final static int SCHEDULER_PERIOD = 1000;


    public ScoreSoundRender() {
        mTTS = new TextToSpeech(ExerciseActivity.eActivity.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = mTTS.setLanguage(Locale.CANADA);

                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    SLOG.d("TTS", "Language not supported!");
                } else if (result == TextToSpeech.LANG_AVAILABLE) {
                    running = true;
                }
            } else {
                SLOG.d("TTS", "Initialization failed!");
            }
        });
        mTTS.setPitch(0.7f);
    }

    public void setTotalScore(int n) {
        this.totalScore = n;
    }

    private String getSpeechText(int textValue, String TEXT_TAG) {
        return "" + textValue + " " + TEXT_TAG;
    }


    private void alertStatus(String text) {
        if (running) {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


    public void processReps(int currentScore) {

        if (currentScore == 0)
            return;

        int scoreToGo = totalScore - currentScore;

        if (scoreToGo == FIVE || scoreToGo == TEN || scoreToGo == TWENTY_FIVE || scoreToGo == FIFTY || scoreToGo == HUNDRED) {
            String scoreToGoText = getSpeechText(scoreToGo, TO_GO);
            alertStatus(scoreToGoText);
        }

    }


    public void processTime(String text) {

        if (!running) return;
        int remainingTime = 0;

        try {
            remainingTime = Integer.parseInt(text.replace(":", ""));
        } catch (NumberFormatException e) {
            SLOG.e(e.getMessage());
        }

        // minutes
        if (remainingTime == FIVE_MINS || remainingTime == TWO_MINS || remainingTime == ONE_MIN) {
            String timeText = getSpeechText(remainingTime, MINUTES);
            alertStatus(timeText);
        }

        // seconds
        else if (remainingTime == THIRTY_SEC || remainingTime == TEN_SEC) {
            String timeText = getSpeechText(remainingTime, SECONDS);
            alertStatus(timeText);
        }

        // last 5 seconds countdown
        else if (remainingTime == FIVE_SEC) {
            String timeText = getSpeechText(remainingTime, "");
            alertStatus(timeText);
            ExerciseActivity.timer.scheduleAtFixedRate(() -> {
                        if (countDown > ONE_SEC)
                            alertStatus("" + (--countDown));
                    }, SCHEDULER_DELAY, SCHEDULER_PERIOD

            );
        }

    }


    public void stop() {
        if (!running) return;
        running = false;
        mTTS.stop();
        mTTS.shutdown();
    }

}
