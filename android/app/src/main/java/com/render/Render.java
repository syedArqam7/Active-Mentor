package com.render;

import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.jogo.NativeBridge;
import com.jogo.R;
import com.jogo.databinding.BasicExerciseViewBinding;
import com.render.lottie.LottieCounter;
import com.render.lottie.LottieRender;
import com.render.lottie.LottieTimer;
import com.render.sounds.ScoreSoundRender;
import com.render.sounds.SoundRender;
import com.utils.Gear;
import com.utils.SClock;

import java.util.Locale;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;

public class Render {
    //todo move the eActivity drawing to here

    private final ScoreSoundRender scoreSoundRender;
    protected BasicExerciseViewBinding viewBinding;
    protected RelativeLayout parentContainer;
    protected StopButton stopButton;
    protected RestartButton restartButton;
    private long pauseOffset;

    private LottieCounter lottieCounter;
    private boolean running = false;

    public Render() {
        scoreSoundRender = new ScoreSoundRender();
    }

    public void setScore(int count, boolean blink) {
        if (!running) return;
        //set counter when the exercise starts
        viewBinding.setScoreCount(String.format(Locale.ENGLISH, "%02d", count));
        if (blink) {
            lottieCounter.play();
        }
        scoreSoundRender.processReps(count);
    }

    public void showScore() {
        if (!running) return;
        eActivity.runOnUiThread(() -> {
            viewBinding.containerScore.setVisibility(View.VISIBLE);
            setScore(0, false);
        });
    }

    public void showTimer() {
        if (!running) return;
        eActivity.runOnUiThread(() -> {
            viewBinding.containerTimer.setVisibility(View.VISIBLE);
        });
    }

    public void hideTimer() {
        if (!running) return;
        eActivity.runOnUiThread(() -> {
            viewBinding.containerTimer.setVisibility(View.INVISIBLE);
        });
    }

    public void hideScore() {
        if (!running) return;
        eActivity.runOnUiThread(() -> {
            viewBinding.containerScore.setVisibility(View.INVISIBLE);
        });
    }

    public void setExerciseDescription(String description) {
        eActivity.runOnUiThread(() -> viewBinding.setExerciseDescription(description));//Descriptions are loaded for each exercise.
    }

    public void setHighScore(int score) {
        if (!running) return;
        viewBinding.setMaxScore(String.format(Locale.ENGLISH, "%02d", score));
    }

//    public void startCountdownClock(SClock sClock, long time) {
//        if (!running) return;
//        sClock.startCountdownClock(viewBinding, time);
//    }
//
//    public void startNormalClock(SClock sClock) {
//        if (!running) return;
//        sClock.startNormalClock(viewBinding);
//    }

    public boolean isRunning(){
        return running;
    }

    public void stopClock() {
        if (!running) return;
        eActivity.runOnUiThread(() -> {
            viewBinding.chrTimer.stop();
            viewBinding.lavTimer.setBackgroundColor(Color.TRANSPARENT);
        });
    }

    public long initNormalStopClock() {
        //Normal Timer shown on screen when exercise starts.
        eActivity.runOnUiThread(() -> {
            viewBinding.chrTimer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - viewBinding.chrTimer.getBase();
            long minutes = (pauseOffset / 1000) / 60;
            long seconds = (pauseOffset / 1000) % 60;
            viewBinding.setScoreCount(String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds));

        });
        return pauseOffset;
    }

    public void initNormalResetClock() {
        if (!running) return;

        //Normal Timer shown on screen when exercise starts.
        eActivity.runOnUiThread(() -> {
            viewBinding.chrTimer.setBase(SystemClock.elapsedRealtime());
            viewBinding.chrTimer.start();
        });

    }


    public void initViews() {

        viewBinding = getViewBinding();

        lottieCounter = new LottieCounter();
        LottieTimer lottieTimer = new LottieTimer();

        stopButton = new StopButton(viewBinding.stopButton);
        restartButton = new RestartButton(viewBinding.restartButton);

        if (NativeBridge.isDebug()) {
            viewBinding.gearSettings.setVisibility(View.VISIBLE);
            Gear.initGearIFF(viewBinding);
        }

        running = true;

    }

    public BasicExerciseViewBinding getViewBinding() {
        parentContainer = eActivity.findViewById(R.id.containerParent);

        if (viewBinding == null)
            viewBinding = DataBindingUtil.inflate(eActivity.getLayoutInflater(), R.layout.basic_exercise_view, parentContainer, true);
        return viewBinding;
    }

    public void scoreSound() {
        if (!running) return;
        new SoundRender(R.raw.active_score).play();
    }


    public void initiateCountDownSound() {
        viewBinding.chrTimer.setOnChronometerTickListener(chronometer -> scoreSoundRender.processTime((String) chronometer.getText()));
    }

    public void initiateNRepsSound(int totalScore) {
        scoreSoundRender.setTotalScore(totalScore);
    }

    public RelativeLayout getParentContainer() {
        return parentContainer;
    }


    public void stop() {
        running = false;
        scoreSoundRender.stop();
        stopButton.stop();
        restartButton.stop();

    }

    public void jump() {
        new LottieRender(R.raw.indicate_jump).play();
    }

    public void down() {
        new LottieRender(R.raw.indicate_down).play();
    }

    public void kneel() {
        new LottieRender(R.raw.indicate_kneel).play();
    }

    public void up() {
        //todo implement
    }
}
