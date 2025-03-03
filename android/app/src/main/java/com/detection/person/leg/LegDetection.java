package com.detection.person.leg;

import com.detection.person.BodyElement;
import com.detection.person.BodyPartDetection;
import com.detection.person.Bone;
import com.detection.person.PersonDetection;
import com.location.DetectionLocation;
import com.models.ModelManager;
import com.models.Thunder;
import com.utils.InfoBlob;
import com.utils.SMath;

import java.util.List;

public class LegDetection extends BodyPartDetection {
    public final BodyElement hip;
    public final BodyElement knee;
    public final BodyElement ankle;
    public final Bone lowerLeg;
    public final Bone upperLeg;
    public final BodyElement heel;
    public final BodyElement index;
    Orientation orientation;

    public LegDetection(Orientation orientation, ModelManager.MODELTYPE modeltype, boolean exerciseLead, PersonDetection person) {
        super("Leg", modeltype, exerciseLead, person);

        pointPaint =
                orientation == Orientation.LEFT ? pointPaint.red() : pointPaint.blue();

        switch (orientation) {
            case LEFT:
                hip = new BodyElement(Thunder.BODYPART.LEFT_HIP.label, modeltype, exerciseLead);
                knee = new BodyElement(Thunder.BODYPART.LEFT_KNEE.label, modeltype, exerciseLead);
                ankle = new BodyElement(Thunder.BODYPART.LEFT_ANKLE.label, modeltype, exerciseLead);

                if (modeltype == ModelManager.MODELTYPE.OLD_POSENET || modelType == ModelManager.MODELTYPE.THUNDER) {
                    heel = ankle;
                    index = ankle;
                } else {
                    heel = new BodyElement(Thunder.BODYPART.LEFT_HEEL.label, modeltype, exerciseLead);
                    index = new BodyElement(Thunder.BODYPART.LEFT_FOOT_INDEX.label, modeltype, exerciseLead);
                }
                break;
            case RIGHT:
                hip = new BodyElement(Thunder.BODYPART.RIGHT_HIP.label, modeltype, exerciseLead);
                knee = new BodyElement(Thunder.BODYPART.RIGHT_KNEE.label, modeltype, exerciseLead);
                ankle = new BodyElement(Thunder.BODYPART.RIGHT_ANKLE.label, modeltype, exerciseLead);

                if (modeltype == ModelManager.MODELTYPE.OLD_POSENET || modelType == ModelManager.MODELTYPE.THUNDER) {
                    heel = ankle;
                    index = ankle;
                } else {
                    heel = new BodyElement(Thunder.BODYPART.RIGHT_HEEL.label, modeltype, exerciseLead);
                    index = new BodyElement(Thunder.BODYPART.RIGHT_FOOT_INDEX.label, modeltype, exerciseLead);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }

        this.orientation = orientation;
        bodyElements.add(hip);
        bodyElements.add(knee);
        bodyElements.add(ankle);
        bodyElements.add(heel);

        lowerLeg = new Bone(ankle, knee);
        upperLeg = new Bone(knee, hip);
        bones.add(lowerLeg);
        bones.add(upperLeg);

    }

    @Override
    public String getName() {
        return orientation + "leg";
    }

    public double getHipAngle() {
        DetectionLocation shoulderLocation;
        if (orientation == Orientation.LEFT)
            shoulderLocation = person.leftArm.shoulder.getDetectedLocation();
        else shoulderLocation = person.rightArm.shoulder.getDetectedLocation();

        return SMath.calculateAngle3Points(
                shoulderLocation,
                hip.getDetectedLocation(),
                ankle.getDetectedLocation(),
                false
        );
    }

    public double getLegAngle() {
        //180 - getHipAngle
        DetectionLocation shoulderLocation;
        if (orientation == Orientation.LEFT)
            shoulderLocation = person.leftArm.shoulder.getDetectedLocation();
        else shoulderLocation = person.rightArm.shoulder.getDetectedLocation();

        return SMath.calculateAngle3Points(
                shoulderLocation,
                hip.getDetectedLocation(),
                ankle.getDetectedLocation(),
                true
        );
    }

    public double getKneeAngle() {
        return SMath.calculateAngle3Points(hip.getDetectedLocation(), knee.getDetectedLocation(), ankle.getDetectedLocation(), true);
    }

    @Override
    public void parse(List<DetectionLocation> detectedLocations, InfoBlob info) {
        super.parse(detectedLocations, info);
        bones.forEach(Bone::updateLength);
    }

    public double getLength() {
        return lowerLeg.getFullLength() + upperLeg.getFullLength();
    }

    @Override
    public String toString() {
        return "LegDetection{" +
                "hip=" + hip +
                ", knee=" + knee +
                ", ankle=" + ankle +
                ", orientation=" + orientation +
                '}';
    }


    public enum Orientation {
        LEFT, RIGHT
    }
}
