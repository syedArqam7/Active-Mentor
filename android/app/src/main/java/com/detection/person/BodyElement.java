package com.detection.person;

import com.detection.ObjectDetection;
import com.logger.SLOG;
import com.models.ModelManager;

public class BodyElement extends ObjectDetection {
    // One small Element, like hand or feet

    public BodyElement(String label, ModelManager.MODELTYPE modelType, boolean exerciseLead) {
        super(label, modelType, exerciseLead);
        SLOG.d("body element" + label + "instantiated");
    }

}
