package com.pose;

import android.graphics.Canvas;
import android.util.Pair;

import com.render.CanDraw;
import com.utils.JOGOPaint;
import com.utils.interfaces.SimpleLambda;

import java.util.ArrayList;
import java.util.List;

public class Pose implements CanDraw {
    private final List<Pair<SubPose, String>> ySubPose = new ArrayList<>();
    private final List<Pair<SubPose, StringMethod>> ySubPoseString = new ArrayList<>();

    private final List<Pair<SubPose, String>> nSubPose = new ArrayList<>();
    private final List<Pair<SubPose, StringMethod>> nSubPoseString = new ArrayList<>();

    private final List<SimpleLambda<String>> extraDraw = new ArrayList<>();

    private final String name;

    protected Pose(String name) {
        this.name = name;
    }

    public Pose y(SubPose subPose, String tag) {
        ySubPose.add(new Pair<>(subPose, tag));
        return this;
    }

    public Pose y(SubPose subPose, StringMethod tag) {
        ySubPoseString.add(new Pair<>(subPose, tag));
        return this;
    }

    public Pose n(SubPose subPose, String tag) {
        nSubPose.add(new Pair<>(subPose, tag));
        return this;
    }

    public Pose n(SubPose subPose, StringMethod tag) {
        nSubPoseString.add(new Pair<>(subPose, tag));
        return this;
    }


    public Pose y(SubPose subPose, String tag, SubPose inverse) {
        ySubPose.add(new Pair<>(subPose, tag));

        return this;
    }

    public Pose n(SubPose subPose, String tag, SubPose inverse) {
        nSubPose.add(new Pair<>(subPose, tag));
        return this;
    }

    protected Pose extraDraw(SimpleLambda<String> method) {
        extraDraw.add(method);
        return this;
    }

    public boolean match() {
        return ySubPose.stream().allMatch((p) -> p.first.match())
                && ySubPoseString.stream().allMatch((p) -> p.first.match())
                && nSubPose.stream().noneMatch((p) -> p.first.match())
                && nSubPoseString.stream().noneMatch((p) -> p.first.match());
    }

    @Override
    public void draw(Canvas canvas) {
//can be overridden if required
    }

    @Override
    public void drawDebug(Canvas canvas) {
        ySubPose.forEach(p -> canvas.drawText(p.second + ": " + p.first.match(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint));
        ySubPoseString.forEach(p -> canvas.drawText(p.second.get() + ": " + p.first.match(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint));

        nSubPose.forEach(p -> canvas.drawText(p.second + ": " + !p.first.match(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint));
        nSubPoseString.forEach(p -> canvas.drawText(p.second.get() + ": " + !p.first.match(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint));

        extraDraw.forEach(p -> canvas.drawText(p.run(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint));

        if (name != null) {
            canvas.drawText("pose " + name + ": " + match(), 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint);
        }

    }


    public interface StringMethod {
        String get();
    }


}