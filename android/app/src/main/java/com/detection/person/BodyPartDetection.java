package com.detection.person;

import android.graphics.Canvas;

import androidx.collection.ArraySet;

import com.detection.ObjectDetection;
import com.logger.SLOG;
import com.models.ModelManager;
import com.utils.JOGOPaint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BodyPartDetection extends ObjectDetection {
    // An Bodypart, consisting of multiple bodyElements

    protected final PersonDetection person;
    protected final List<ObjectDetection> bodyElements = new ArrayList<>();
    protected final List<Bone> bones = new ArrayList<>();
    protected JOGOPaint pointPaint;
    Set<String> associatedLabels = null;
    private Set<ObjectDetection> objectDetectionSet;


    public BodyPartDetection(String label, ModelManager.MODELTYPE modelType, boolean exerciseLead, PersonDetection person) {
        super(label, modelType, exerciseLead);
        this.person = person;

        // default paint
        this.pointPaint = new JOGOPaint().fillStroke().activeOrange();
    }

    public List<ObjectDetection> getBodyElements() {
        return bodyElements;
    }

    @Override
    public void drawDebug(Canvas canvas) {
        super.drawDebug(canvas);
        bones.forEach(bone -> bone.drawDebug(canvas));
        bodyElements.forEach(bodyElement ->
                bodyElement.drawDebug(canvas, pointPaint));
    }

    @Override
    public void setConfidenceScore(float confidenceScore) {
        super.setConfidenceScore(confidenceScore);
        bodyElements.forEach(bodyElement -> bodyElement.setConfidenceScore(confidenceScore));
    }

    @Override
    public void draw(Canvas canvas) {
        // extra draw of bones and body parts
    }

    public Set<String> getAssociatedLabels() {
        //ensures we do it only once
        if (associatedLabels == null) {
            associatedLabels = new ArraySet<>();

            bodyElements.forEach(bodyElement -> associatedLabels.add(bodyElement.getLabel()));
            SLOG.d("labels of " + getName() + " : " + associatedLabels);
        }
        return associatedLabels;
    }

    public Set<ObjectDetection> getObjectDetections() {
        if (objectDetectionSet == null) {
            objectDetectionSet = new ArraySet<>();
            objectDetectionSet.addAll(bodyElements);
        }
        //ensures we do it only once
        return objectDetectionSet;
    }

    public abstract String getName();


    public void writeToJSON(JSONObject dataJSONWriter, int locIdx) {
        bodyElements.forEach(element -> {
            try {
                element.writeToJSON(dataJSONWriter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void unSubscribe() {
        super.unSubscribe();
        bodyElements.forEach(ObjectDetection::unSubscribe);
    }
}
