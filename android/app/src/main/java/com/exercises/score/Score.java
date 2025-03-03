package com.exercises.score;

import android.os.SystemClock;
import android.util.Pair;

import com.exercises.base.exercise.ExerciseActivity;
import com.exercises.base.exercise.ExerciseClient;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.jogo.R;
import com.render.Render;
import com.render.sounds.SoundRender;
import com.utils.SClock;
import com.utils.STimer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

abstract public class Score {
    //todo should be refactored to protected
    public final List<Pair<Integer, Integer>> scoreFrameIDs = new ArrayList<>(); // <frameID, count>>

    protected final Render render;
    protected SClock sClock;
    protected boolean countdown = false;
    protected long time;
    protected long lastScoreTime = 0;
    protected boolean running = false;
    protected int currentFrameID = 0;
    STimer timer;
    private int count = 0;


    protected Score() {
        this.render = ExerciseActivity.render;
    }

    protected void setCount(int count, boolean blink) {
        if (!running) return;
        this.count = count;
        render.setScore(count, blink);
        scoreFrameIDs.add(new Pair<>(currentFrameID, count)); // ball - std dev
        lastScoreTime = SystemClock.elapsedRealtime();
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        incrementCount(1, true, true);
    }

    public void incrementCount(int amount) {
        incrementCount(amount, true, true);
    }

    public void incrementCount(int amount, boolean blink, boolean beep) {
        if (!running) return;
        if (beep) {
            render.scoreSound();
        }
        setCount(count + amount, blink);
    }

    public void decrementCount() {
        decrementCount(1, true, true);
    }

    public void decrementCount(int count, boolean blink, boolean beep) {
        if (!running) return;
        if (beep) new SoundRender(R.raw.jogo_error).play();
        setCount(Math.max(this.count - count, 0), blink);
    }

    public void resetCount() {
        resetCount(true, true);
    }

    public void resetCount(boolean blink, boolean beep) {
        scoreFrameIDs.add(new Pair<>(currentFrameID, 0));
        if (!running) return;
        if (beep) new SoundRender(R.raw.jogo_cone_error).play();
        setCount(0, blink);
    }

    public void reset() {
        if (!running) return;
        resetCount();
    }

    public long timeSinceLastScore() {
        return SystemClock.elapsedRealtime() - lastScoreTime;
    }

    public int getCurrentFrameID() {
        return this.currentFrameID;
    }

    public void setCurrentFrameID(int currentFrameID) {
        this.currentFrameID = currentFrameID;
    }

    public void setTimer(STimer timer) {
        if (running) throw new  IllegalStateException("timer should be set before start"); //todo lets see if this revoles it!!! check back again pose-scan!
        this.timer = timer;
    }

    public void stopTime() {
        lastScoreTime = SystemClock.elapsedRealtime();
        if (sClock.isRunning()) {
            sClock.stop();
            render.stopClock();
        }
    }

    public void continueTime() {
        lastScoreTime = SystemClock.elapsedRealtime();
        if (countdown) {
            startCountdownTime(time);
        } else {
            startNormalTime();
        }
    }

    protected void startNormalTime() {
        if (!sClock.isRunning()) {
            sClock.start();
            if (render.isRunning())
                sClock.startNormalClock(render.getViewBinding());
            //  render.startNormalClock(sClock);
        }
    }

    protected void startCountdownTime(long time) {
        if (!sClock.isRunning()) {
            countdown = true;
            sClock.start();
            if (render.isRunning())
                sClock.startCountdownClock(render.getViewBinding(), time);
            //render.startCountdownClock(sClock, time);
        }
    }

    public boolean timerRunning() {
        return sClock.isRunning();
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        running = true;
        lastScoreTime = SystemClock.elapsedRealtime();
        initView();

        if (timer == null) timer = new STimer(false);
        sClock = new SClock(timer);
    }

    public void stop() {
        running = false;
    }

    public void stopExercise() {
        sClock.stop();
        ExerciseClient.getClient().stopExercise();
        stop();
    }


    public void writeToJSON(JSONObject ballData) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Pair<Integer, Integer> p : scoreFrameIDs) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("frameid", p.first);
            jsonObject.put("score", p.second);
            jsonArray.put(jsonObject);
        }

        ballData.put("final_score", getFinalScore());
        ballData.put("score_frame_ids", jsonArray);

    }

    public int getFinalScore() {
        if (running) throw new IllegalStateException("score should not be running");
        return count;
    }


    public WritableMap toMap() {
        WritableMap params = Arguments.createMap();
        params.putInt("finalScore", getFinalScore());
        return params;
    }

    abstract protected void initView();

}
