package com.detection.person.arm;

import com.detection.person.BodyElement;
import com.detection.person.BodyPartDetection;
import com.detection.person.Bone;
import com.detection.person.PersonDetection;
import com.location.DetectionLocation;
import com.logger.SLOG;
import com.models.ModelManager;
import com.models.Thunder;
import com.utils.InfoBlob;
import com.utils.SMath;

import java.util.List;

public class ArmDetection extends BodyPartDetection {

    public final BodyElement shoulder;
    public final BodyElement elbow;
    public final BodyElement wrist;
    public final BodyElement pinky;
    public final BodyElement index;
    public final BodyElement thumb;
    public final Bone underArm;
    public final Bone upperArm;
    protected static final float EXTENSION_THRESHOLD = 0.90f;
    Orientation orientation;
    float scaler = 10;

    public ArmDetection(Orientation orientation, ModelManager.MODELTYPE modelType, boolean exerciseLead, PersonDetection person) {
        super("Arm", modelType, exerciseLead, person);

        pointPaint =
                orientation == Orientation.LEFT ? pointPaint.red() : pointPaint.blue();

        switch (orientation) {
            case LEFT:
                shoulder = new BodyElement(Thunder.BODYPART.LEFT_SHOULDER.label, modelType, exerciseLead);
                elbow = new BodyElement(Thunder.BODYPART.LEFT_ELBOW.label, modelType, exerciseLead);
                wrist = new BodyElement(Thunder.BODYPART.LEFT_WRIST.label, modelType, exerciseLead);

                if (modelType == ModelManager.MODELTYPE.OLD_POSENET || modelType == ModelManager.MODELTYPE.THUNDER) {
                    pinky = wrist;
                    index = wrist;
                    thumb = wrist;
                } else { // Thunder_FASTMODE or Thunder
                    pinky = new BodyElement(Thunder.BODYPART.LEFT_PINKY.label, modelType, exerciseLead);
                    index = new BodyElement(Thunder.BODYPART.LEFT_INDEX.label, modelType, exerciseLead);
                    thumb = new BodyElement(Thunder.BODYPART.LEFT_THUMB.label, modelType, exerciseLead);
                }

                break;

            case RIGHT:
                shoulder = new BodyElement(Thunder.BODYPART.RIGHT_SHOULDER.label, modelType, exerciseLead);
                elbow = new BodyElement(Thunder.BODYPART.RIGHT_ELBOW.label, modelType, exerciseLead);
                wrist = new BodyElement(Thunder.BODYPART.RIGHT_WRIST.label, modelType, exerciseLead);

                if (modelType == ModelManager.MODELTYPE.OLD_POSENET || modelType == ModelManager.MODELTYPE.THUNDER) {
                    pinky = wrist;
                    index = wrist;
                    thumb = wrist;
                } else { // Thunder_FASTMODE or Thunder
                    pinky = new BodyElement(Thunder.BODYPART.RIGHT_PINKY.label, modelType, exerciseLead);
                    index = new BodyElement(Thunder.BODYPART.RIGHT_INDEX.label, modelType, exerciseLead);
                    thumb = new BodyElement(Thunder.BODYPART.RIGHT_THUMB.label, modelType, exerciseLead);

                }


                break;
            default:
                throw new IllegalStateException("Unexpected value: " + orientation);
        }
        this.orientation = orientation;
        bodyElements.add(shoulder);
        bodyElements.add(elbow);
        bodyElements.add(wrist);


        bodyElements.add(pinky);
        bodyElements.add(index);
        bodyElements.add(thumb);


        underArm = new Bone(wrist, elbow);
        upperArm = new Bone(elbow, shoulder);
        bones.add(underArm);
        bones.add(upperArm);
    }

    @Override
    public String getName() {
        return orientation + "arm";
    }

    public double getShoulderAngle() {
        //gets the sideways extended shoulder angle
        DetectionLocation shoulderLocation = shoulder.getDetectedLocation();
        DetectionLocation elbowLocation = elbow.getDetectedLocation();
        DetectionLocation hipLocation;
        if (orientation == Orientation.LEFT) hipLocation = person.leftLeg.hip.getDetectedLocation();
        else hipLocation = person.rightLeg.hip.getDetectedLocation();
        return SMath.calculateAngle3Points(elbowLocation, shoulderLocation, hipLocation, false);
    }

    public double getElbowFlexion() {
        DetectionLocation shoulderLocation = shoulder.getDetectedLocation();
        DetectionLocation elbowLocation = elbow.getDetectedLocation();
        DetectionLocation wristLocation = wrist.getLocation();
        return SMath.calculateAngle3Points(shoulderLocation, elbowLocation, wristLocation, false);
    }

    public double getSideWaysExtendedCosine() {
        //if the arm is extended, the cosine similarity between the shoulder-elbow and shoulder-wrist should be high
        DetectionLocation elbowD = elbow.getDetectedLocation();
        DetectionLocation wristD = wrist.getDetectedLocation();
        DetectionLocation shoulderD = shoulder.getDetectedLocation();
        if (elbowD == null || wristD == null || shoulderD == null) return 0;
        //calculate the cosine similarity
        double shoulderX = shoulderD.getX();
        double shoulderY = shoulderD.getY();

        double scaledElbowX = (elbowD.getX() - shoulderX) * scaler;
        double scaledElbowY = (elbowD.getY() - shoulderY) * scaler;
        double scaledWristX = (wristD.getX() - shoulderX) * scaler;
        double scaledWristY = (wristD.getY() - shoulderY) * scaler;


        double xsim = (scaledElbowX) * (scaledWristX);
        double ysim = (scaledElbowY) * (scaledWristY);
        double enorm = Math.sqrt(Math.pow(scaledElbowX, 2) + Math.pow(scaledElbowY, 2));
        double wnorm = Math.sqrt(Math.pow(scaledWristX, 2) + Math.pow(scaledWristY, 2));

        double cosineSim = ((xsim + ysim) / (enorm * wnorm));
        SLOG.d("xsim: " + xsim + "ysim: " + ysim + "wnorm: " + wnorm + "enorm: " + enorm + "cosine: " + cosineSim);
        return cosineSim;

    }

    public boolean sidewaysExtended() {
        return getSideWaysExtendedCosine() > EXTENSION_THRESHOLD;
    }

    public double getLength() {
        return underArm.getFullLength() + upperArm.getFullLength();
    }

    @Override
    public void parse(List<DetectionLocation> detectedLocations, InfoBlob info) {
        super.parse(detectedLocations, info);
        bones.forEach(Bone::updateLength);
    }

    @Override
    public String toString() {
        return "ArmDetection{" +
                "orientation=" + orientation +
                ", shoulder=" + shoulder +
                ", elbow=" + elbow +
                ", wrist=" + wrist +
                '}';
    }


    public enum Orientation {
        LEFT, RIGHT
    }
}
