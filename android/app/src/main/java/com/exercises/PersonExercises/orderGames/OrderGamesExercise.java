package com.exercises.PersonExercises.orderGames;

import android.graphics.Canvas;
import android.os.SystemClock;

import com.exercises.base.exercise.AbstractPersonExercise;
import com.exercises.base.exercise.ExerciseSettings;
import com.jogo.R;
import com.models.ModelManager;
import com.render.lottie.LottieCalibration;
import com.utils.InfoBlob;
import com.utils.UtilArrayList;

import java.util.Random;
import java.util.stream.IntStream;

public class OrderGamesExercise extends AbstractPersonExercise {
    Random rd = new Random();
    private final UtilArrayList<OrderSequence> sequence = new UtilArrayList<>();
    private final UtilArrayList<Integer> sequenceNumbers = new UtilArrayList<>();
    private static final int SEQUENCE_SIZE = 6;
    private int nextSeqNumber = 0;
    private long coolDownTime = 0;
    private static final long COOLDOWNTIME = 1000;
    private static final double leftXstart = 0.40;
    private static final double rightXstart = 0.60;
    private int PARTITIONING_INDEX;


    public OrderGamesExercise(ExerciseSettings exerciseSettings) {
        super(new LottieCalibration(R.raw.basic_calibration_outlines, R.raw.basic_calibration_correct, R.raw.basic_calibration_calibrate), exerciseSettings, ModelManager.MODELTYPE.POSENET, true);
//        calibration.setYCalibration(20,30);
    }

    protected void initExercise(){
        initSequence();
    }

    @Override
    protected void drawExercise(Canvas canvas) {
        IntStream.range(0, SEQUENCE_SIZE).forEach(x ->
                sequence.get(x).draw(canvas));
    }



    @Override
    protected void drawExerciseDebug(Canvas canvas) {
        // uncomment when required or during debugging.
//        IntStream.range(0, SEQUENCE_SIZE).forEach(x ->
//                sequence.get(x).drawDebug(canvas));

    }

    private void initSeqNumbers() {
        sequenceNumbers.clear();
        // We can make different sequences
        IntStream.range(1, SEQUENCE_SIZE + 1).forEach(x -> sequenceNumbers.add(x));
    }

    private void initLocations() {
        double visibleRadius = 0.10;
        double circleRadius = visibleRadius + 0.01;
        // empty if array
        sequence.clear();
        // left points
        sequence.add(new OrderSequence(circleRadius, leftXstart, 0.10, 0, visibleRadius));
        sequence.add(new OrderSequence(circleRadius, leftXstart - 0.16, 0.3, 0, visibleRadius)); //0.2901
        sequence.add(new OrderSequence(circleRadius, leftXstart - 0.16, 0.64, 0, visibleRadius)); //0.298
        // right points
        sequence.add(new OrderSequence(circleRadius, rightXstart, 0.10, 0, visibleRadius));
        sequence.add(new OrderSequence(circleRadius, rightXstart + 0.16, 0.3, 0, visibleRadius));
        sequence.add(new OrderSequence(circleRadius, rightXstart + 0.16, 0.64, 0, visibleRadius));
    }

    private void initSequence() {

        initSeqNumbers();
        initLocations();

        int nextIndex;
        int sequenceCount = 0;
        nextSeqNumber = 0;

        while (sequenceCount < sequence.size()) {
            nextIndex = rd.nextInt(sequence.size());
            // if sequence number is assigned then skip the iteration
            if (sequence.get(nextIndex).getSeqNumber() != 0) continue;

            // push the sequence number in random index of sequence.
            sequence.get(nextIndex).setSeqNumber(sequenceNumbers.get(sequenceCount));
            sequenceCount++;
        }

    }

    protected void processExercise(InfoBlob infoBlob) {

        // Index at which sequence is partitioned into left and right
        PARTITIONING_INDEX = 3;

        if (coolDownTime > SystemClock.elapsedRealtime()) return;
        // cooldown before starting of new sequence
        if (nextSeqNumber == sequenceNumbers.size())
            initSequence();

        IntStream.range(0, SEQUENCE_SIZE).forEach(x ->
        {
            OrderSequence currentSeq = sequence.get(x);
            boolean touchedSeq;

            // check left
            if (x < PARTITIONING_INDEX) {
                touchedSeq = currentSeq.checkIntersection(person.leftArm.index)
                        || currentSeq.checkIntersection(person.leftArm.wrist);
            } else { // check right
                touchedSeq =
                        currentSeq.checkIntersection(person.rightArm.index)
                                || currentSeq.checkIntersection(person.rightArm.wrist);
            }

            if (touchedSeq && !currentSeq.isSelected() &&
                    currentSeq.getSeqNumber() == sequenceNumbers.get(nextSeqNumber)) {
                currentSeq.select(true);
                nextSeqNumber++;
                incrementScore();

//                renderModule.drawPlusOne(currentSeq.getX(), currentSeq.getY(), 15);

            } else if (touchedSeq && !currentSeq.isSelected()) // false selection
            {
                currentSeq.select(false);
                // remove incorrect number from sequence
                sequenceNumbers.remove(new Integer(currentSeq.getSeqNumber()));
            }
        });

        if (nextSeqNumber == sequenceNumbers.size()) {
            // 1.5 seconds pause before starting new sequence
            coolDownTime = SystemClock.elapsedRealtime() + COOLDOWNTIME;

        }
    }

    @Override
    public String getName() {
        return "order-games";
    }

    @Override
    public String toString() {
        return "OrderGames{" +
                "Count=" + score.getCount() +
                '}';
    }

}






