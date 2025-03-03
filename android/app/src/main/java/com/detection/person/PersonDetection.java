package com.detection.person;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Pair;

import androidx.collection.ArraySet;

import com.detection.ObjectDetection;
import com.detection.person.arm.ArmDetection;
import com.detection.person.leg.LegDetection;
import com.location.DetectionLocation;
import com.logger.SLOG;
import com.models.ModelManager;
import com.utils.InfoBlob;
import com.utils.JOGOPaint;
import com.utils.UtilArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PersonDetection extends ObjectDetection {
    public final FaceDetection face;

    public final ArmDetection leftArm;
    public final ArmDetection rightArm;

    public LegDetection leftLeg;
    public LegDetection rightLeg;
    public Bone shoulderToShoulder, rightShoulderRightHip, leftShoulderLeftHip, hipToHip;
    public boolean up = true;

    UtilArrayList<BodyPartDetection> bodyParts = new UtilArrayList<>();
    private static final UtilArrayList<Bone> bones = new UtilArrayList<>();


    public DetectionLocation highestLeftAnkle, highestRightAnkle, lowestLeftHeel, lowestRightHeel, lowestHighLeftHeel, lowestHighRightHeel;

    int JUMP_LOOKBACK = 15;
    int GROUND_LOOKBACK = 6, HEAD_LOOKBACK = 6;

    public double MIN_JUMP = 0.04;
    private Set<String> associatedLabels = null;
    private Set<ObjectDetection> objectDetectionSet;

    //todo should be relpaced by the gear
    private static final boolean jumpDebug = false;

    float POINT_RADIUS = 0.006f;

    public PersonDetection(boolean exerciseLead, ModelManager.MODELTYPE modelType, boolean upperBodyOnly) {
        super("person", modelType, false); //with skip it's not subscribing
        leftArm = new ArmDetection(ArmDetection.Orientation.LEFT, modelType, exerciseLead, this);
        bodyParts.add(leftArm);

        rightArm = new ArmDetection(ArmDetection.Orientation.RIGHT, modelType, exerciseLead, this);
        bodyParts.add(rightArm);

        face = new FaceDetection(modelType, exerciseLead, this);
        bodyParts.add(face);

        shoulderToShoulder = new Bone(leftArm.shoulder, rightArm.shoulder);
        bones.add(shoulderToShoulder);


        if (!upperBodyOnly) {
            leftLeg = new LegDetection(LegDetection.Orientation.LEFT, modelType, exerciseLead, this);
            bodyParts.add(leftLeg);

            rightLeg = new LegDetection(LegDetection.Orientation.RIGHT, modelType, exerciseLead, this);
            bodyParts.add(rightLeg);

            rightShoulderRightHip = new Bone(rightArm.shoulder, rightLeg.hip);
            bones.add(rightShoulderRightHip);

            leftShoulderLeftHip = new Bone(leftArm.shoulder, leftLeg.hip);
            bones.add(leftShoulderLeftHip);

            hipToHip = new Bone(leftLeg.hip, rightLeg.hip);
            bones.add(hipToHip);
        }

    }


    public PersonDetection() {
        //default POSENET (ACCURATE MODE)
        this(false, ModelManager.MODELTYPE.POSENET, false);
    }

    public PersonDetection(ModelManager.MODELTYPE modelType) {
        this(false, modelType, false);

    }

    public Set<String> getAssociatedLabels() {
        //ensures we do it only once
        if (associatedLabels == null) {
            associatedLabels = new ArraySet<>();
            bodyParts.forEach(bodyPart -> associatedLabels.addAll(bodyPart.getAssociatedLabels()));
        }
        return associatedLabels;
    }

    public Set<ObjectDetection> getDetectionSubClasses() {
        if (objectDetectionSet == null) {
            objectDetectionSet = new ArraySet<>();
            bodyParts.forEach(bodyPart -> objectDetectionSet.addAll(bodyPart.getObjectDetections()));
        }
        //ensures we do it only once
        return objectDetectionSet;
    }

    public Pair<Double, Double> getCenterPoint() {
        DetectionLocation hl, hr, sl, sr;
        hl = leftLeg.hip.getDetectedLocation();
        hr = rightLeg.hip.getDetectedLocation();
        sl = leftArm.shoulder.getDetectedLocation();
        sr = rightArm.shoulder.getDetectedLocation();

        if (hl == null || hr == null || sl == null || sr == null) return null;
        return new Pair<Double, Double>((hl.getX() + hr.getX() + sl.getX() + sr.getX()) / 4f, (hl.getY() + hr.getY() + sl.getY() + sr.getY()) / 4f);
    }


    @Override
    public void setConfidenceScore(float confidenceScore) {
        //!!NOTE this changes the confidence score of posenet, but this confidence score is only used for the inframe likelihood.
        // we cannot change the posenet detection accuracy, as it's determined internally by the posenet module
        super.setConfidenceScore(confidenceScore);
        bodyParts.forEach(bodyPartDetection -> bodyPartDetection.setConfidenceScore(confidenceScore));
    }


    @SuppressLint("DefaultLocale")
    public int didJump(int lastJumpFrameID) {
        if (getInfoBlobArrayList().size() < JUMP_LOOKBACK) return 0;
        int lastFrameID = getInfoBlobArrayList().getLast().getFrameID();

        final int JUMP_FRAME_GAP = 2;

        UtilArrayList<DetectionLocation> leftHeels = leftLeg.heel.getNDetectedLocations(Math.min(JUMP_LOOKBACK, lastFrameID - lastJumpFrameID));
        UtilArrayList<DetectionLocation> rightHeels = rightLeg.heel.getNDetectedLocations(Math.min(JUMP_LOOKBACK, lastFrameID - lastJumpFrameID));
        lowestLeftHeel = leftHeels.getMin(DetectionLocation::compareByY);
        lowestRightHeel = rightHeels.getMin(DetectionLocation::compareByY);
        int leftHighIndex = leftHeels.indexOf(lowestLeftHeel);
        int rightHighIndex = rightHeels.indexOf(lowestRightHeel);

        // Lowest Ankle (by Position)
        UtilArrayList<DetectionLocation> leftAnkles = new UtilArrayList<>(leftHeels.size());
        leftAnkles.addAll(leftHeels.subList(0, leftHighIndex));
        UtilArrayList<DetectionLocation> rightAnkles = new UtilArrayList<>(rightHeels.size());
        rightAnkles.addAll(rightHeels.subList(0, rightHighIndex));
        highestLeftAnkle = leftAnkles.getMax(DetectionLocation::compareByY);
        highestRightAnkle = rightAnkles.getMax(DetectionLocation::compareByY);


        // Lowest Heel after Highest Heel (by Position)       => Downward Motion
        UtilArrayList<DetectionLocation> leftHighHeels = new UtilArrayList<>(leftHeels.size());
        leftHighHeels.addAll(leftHeels.subList(leftHighIndex + 1, leftHeels.size()));
        UtilArrayList<DetectionLocation> rightHighHeels = new UtilArrayList<>(rightHeels.size());
        rightHighHeels.addAll(rightHeels.subList(rightHighIndex + 1, rightHeels.size()));
        lowestHighLeftHeel = leftHighHeels.getMax(DetectionLocation::compareByY);
        lowestHighRightHeel = rightHighHeels.getMax(DetectionLocation::compareByY);
        //SLOG.d("highestLeftAnkle"+String.format("%.2f", highestLeftAnkle.getY()) + " highestRightAnkle"+String.format("%.2f", highestRightAnkle.getY()) +" diff: "+String.format("%.2f",highestLeftAnkle.getY() - lowestLeftHeel.getY()) +" lowestRightHeel " + String.format("%.2f", lowestRightHeel.getY()) + " lowestLeftHeel " + String.format("%.2f", lowestLeftHeel.getY()) + " diff: "+String.format("%.2f", highestLeftAnkle.getY() - lowestLeftHeel.getY())+ "highestHighLeftHeel "+String.format("%.2f", lowestHighLeftHeel.getY())+ "highestHighrightHeel "+String.format("%.2f", lowestHighRightHeel.getY())+" diff: "+String.format("%.2f",lowestHighLeftHeel.getY() - lowestLeftHeel.getY()));
        //if the heel is lower(so actually higher...) than the ankle, we can consider this a jump


        if (
                (lowestLeftHeel != null && lowestRightHeel != null && lowestHighLeftHeel != null
                        && lowestHighRightHeel != null && highestLeftAnkle != null && highestRightAnkle != null)
                        && (
                        ((lowestLeftHeel.getY() + MIN_JUMP) < highestLeftAnkle.getY() && (lowestRightHeel.getY() + MIN_JUMP) < highestRightAnkle.getY()) &&
                                ((lowestHighLeftHeel.getY() - MIN_JUMP) > lowestLeftHeel.getY() && (lowestHighRightHeel.getY() - MIN_JUMP) > lowestRightHeel.getY())
                                && ((Math.max(lowestHighLeftHeel.getFrameID(), lowestHighRightHeel.getFrameID())) - lastJumpFrameID <= JUMP_FRAME_GAP)
                )
        ) {
//                SLOG.d("DIDJUMP: " + lowestLeftHeel.getY() +" < "+ highestLeftAnkle.getY() +" && " +lowestRightHeel.getY() +" < "+ highestRightAnkle.getY() +" && "+ lowestHighLeftHeel.getY() +" > "+ lowestLeftHeel.getY() +" && " + lowestHighRightHeel.getY() +" > "+ lowestRightHeel.getY());
//                SLOG.d("DIDJUMP LastFrame: "+lastJumpFrameID + "  FrameID: "+Math.max(lowestHighLeftHeel.getFrameID(), lowestHighRightHeel.getFrameID()));

            return lastJumpFrameID;
        } else {
            return Math.max(lowestLeftHeel.getFrameID(), lowestRightHeel.getFrameID());
        }
    }

    public double getGroundLine() {

        UtilArrayList<DetectionLocation> leftAnkleLocations = leftLeg.ankle.getNDetectedLocations(GROUND_LOOKBACK);
        UtilArrayList<DetectionLocation> rightAnkleLocations = rightLeg.ankle.getNDetectedLocations(GROUND_LOOKBACK);

        return Math.max(leftAnkleLocations.getMax(DetectionLocation::compareByY).getY(), rightAnkleLocations.getMax(DetectionLocation::compareByY).getY());
    }

    public double getHeadLine() {

        UtilArrayList<DetectionLocation> leftEyeLocations = face.leftEye.getNDetectedLocations(HEAD_LOOKBACK);
        UtilArrayList<DetectionLocation> rightEyeLocations = face.rightEye.getNDetectedLocations(HEAD_LOOKBACK);

        return Math.min(leftEyeLocations.getMin(DetectionLocation::compareByY).getY(), rightEyeLocations.getMin(DetectionLocation::compareByY).getY());

    }

    @Override
    public void parse(List<DetectionLocation> detectedLocations, InfoBlob info) {
        synchronized (this) {
            super.parse(detectedLocations, info);
            bones.forEach(Bone::updateLength);
        }
    }


    public void writeToJSON(JSONObject dataJSONWriter, int locIdx) {
        bodyParts.forEach(bodyPart -> {
            bodyPart.writeToJSON(dataJSONWriter, locIdx);
        });
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        bodyParts.forEach(bodyPart -> bodyPart.draw(canvas));
        bones.forEach(bone -> bone.draw(canvas));
    }


    JOGOPaint bbPaint = new JOGOPaint().activeOrange().strokeWidth(30).stroke().transparancy(0.5);

    public void drawBBox(Canvas canvas) {
        List<DetectionLocation> lastLocations = getDetectionSubClasses().stream().map(ObjectDetection::getLocation)
                .filter(Objects::nonNull)
                .filter((d -> d.getStatus() == DetectionLocation.STATUS.DETECTED))
                .collect(Collectors.toCollection(ArrayList::new));


        // todo might be optimized using a normal loop
        if (lastLocations.isEmpty()) return;
        try {
            float minX = lastLocations.stream().min(DetectionLocation::compareByX).get().getXf();
            float maxX = lastLocations.stream().max(DetectionLocation::compareByX).get().getXf();
            float minY = lastLocations.stream().min(DetectionLocation::compareByY).get().getYf();
            float maxY = lastLocations.stream().max(DetectionLocation::compareByY).get().getYf();
            canvas.drawRect(new RectF(minX * canvas.getWidth(), minY * canvas.getHeight(), maxX * canvas.getWidth(), maxY * canvas.getHeight()), bbPaint);
        } catch (Resources.NotFoundException e) {
            SLOG.e(e);
        }
    }


    public void drawJumpDebug(Canvas canvas) {
        int id = didJump(0);
        if (lowestRightHeel != null && highestRightAnkle != null && lowestLeftHeel != null && highestLeftAnkle != null && lowestHighLeftHeel != null && lowestLeftHeel != null && lowestHighRightHeel != null && lowestRightHeel != null) {
            JOGOPaint bluePaint = new JOGOPaint().blue().largeStroke();
            JOGOPaint bottomPaint = new JOGOPaint().red().largeStroke();

            //to indicate we have jumped
            if (id != 0) {
                bottomPaint.green().small();
                canvas.drawText("" + id, 210, lowestHighLeftHeel.canvY(canvas), bottomPaint);
            }

            canvas.drawLine(10, (float) (lowestLeftHeel.getY() + MIN_JUMP) * canvas.getHeight(), 200, (float) (lowestLeftHeel.getY() + MIN_JUMP) * canvas.getHeight(), bottomPaint);
            canvas.drawLine(10, (float) highestLeftAnkle.getY() * canvas.getHeight(), 200, (float) highestLeftAnkle.getY() * canvas.getHeight(), bluePaint);

            canvas.drawLine(270, (float) (lowestLeftHeel.getY()) * canvas.getHeight(), 550, (float) (lowestLeftHeel.getY()) * canvas.getHeight(), bottomPaint);
            canvas.drawLine(270, (float) (lowestHighLeftHeel.getY() - MIN_JUMP) * canvas.getHeight(), 550, (float) (lowestHighLeftHeel.getY() - MIN_JUMP) * canvas.getHeight(), bluePaint);

            canvas.drawLine(canvas.getWidth() - 200, (float) (lowestRightHeel.getY() + MIN_JUMP) * canvas.getHeight(), canvas.getWidth() - 10, (float) (lowestRightHeel.getY() + MIN_JUMP) * canvas.getHeight(), bottomPaint);
            canvas.drawLine(canvas.getWidth() - 200, (float) highestRightAnkle.getY() * canvas.getHeight(), canvas.getWidth() - 10, (float) highestRightAnkle.getY() * canvas.getHeight(), bluePaint);

            canvas.drawLine(620, (float) (lowestRightHeel.getY()) * canvas.getHeight(), 760, (float) (lowestRightHeel.getY()) * canvas.getHeight(), bottomPaint);
            canvas.drawLine(620, (float) (lowestHighRightHeel.getY() - MIN_JUMP) * canvas.getHeight(), 760, (float) (lowestHighRightHeel.getY() - MIN_JUMP) * canvas.getHeight(), bluePaint);
        }

    }


    @Override
    public void drawDebug(Canvas canvas) {
        super.drawDebug(canvas);
        bones.forEach(bone -> bone.drawDebug(canvas));
        bodyParts.forEach(b -> b.drawDebug(canvas));

        if (jumpDebug) drawJumpDebug(canvas);

    }

    Map<String, Long> tempInfoBlobMap = new ConcurrentHashMap<>();

    private void inferTimeStamps() {
        //todo use a normal list instead of a hashmap
        tempInfoBlobMap.clear();

        // first add all the timeStamps for known/available frameIDs
        if (getInfoBlobArrayList().isEmpty()) return;
        long timeOffset = getInfoBlobArrayList().get(0).getStartTime();
        for (InfoBlob infoBlob : getInfoBlobArrayList()) {
            tempInfoBlobMap.put("" + infoBlob.getFrameID(), infoBlob.getStartTime() - timeOffset);
        }
        final int DIFF_LIMIT = 1;

        // approximate the timeStamps for the missing frames
        for (int idx = 1; idx < getInfoBlobArrayList().size(); idx++) {
            int prevFrameID = getInfoBlobArrayList().get(idx - 1).getFrameID();
            int currFrameID = getInfoBlobArrayList().get(idx).getFrameID();
            int diff = Math.abs(currFrameID - prevFrameID);
            if (diff > DIFF_LIMIT) { // a frame was skipped (or multiple frames were skipped)
                int i = 1;
                while (i <= diff - 1) {
                    long prevTimeStamp = getInfoBlobArrayList().get(idx - 1).getStartTime() - timeOffset;
                    long currTimeStamp = getInfoBlobArrayList().get(idx).getStartTime() - timeOffset;
                    long timeDiff = Math.abs(currTimeStamp - prevTimeStamp);
                    long newTime = timeDiff / (diff + 1);
                    newTime = (newTime * i) + prevTimeStamp;
                    tempInfoBlobMap.put("" + (prevFrameID + i), newTime);
                    i++;
                }
            }

        }
    }


    public void writeToJSON(JSONObject dataJSONWriter) throws JSONException {

        try {

            // some of the frames in infoBlobArrayList are skipped, therefore we don't have the timeStamps for every frame/location.
            // that's why we will have to approximate the timeStamps for the missing frames.
            inferTimeStamps();  // the timeStamps for the missing frames and for the available frames are contained in the tempInfoBlobMap <frameID : timeStamp>

            JSONObject data = new JSONObject();
            for (ObjectDetection obj : getDetectionSubClasses()) {
                JSONObject object = new JSONObject();
                obj.writeToJSON(object);
                data.put(obj.getLabel(), object);
            }

            JSONArray time = new JSONArray();
            JSONArray frame = new JSONArray();

            for (int i = 0; i < locations.size(); i++) {
                frame.put(locations.get(i).getFrameID());
                time.put(tempInfoBlobMap.get("" + i));
            }
            // data.put(object);
            dataJSONWriter.put("time_stamps", time);
            dataJSONWriter.put("frame", frame);
            dataJSONWriter.put("person_data", data);

        } catch (JSONException e) {
            throw new JSONException(e);
        }

    }


    @Override
    public void unSubscribe() {
        super.unSubscribe();
        bodyParts.forEach(BodyPartDetection::unSubscribe);
    }

    @Override
    public boolean inFrame() {
        return getDetectionSubClasses().stream().allMatch(o -> o.inFrame());
    }
}
