package com.exercises.PersonExercises.growingGrass;

import android.graphics.Canvas;
import android.widget.ImageView;

import com.detection.person.PersonDetection;
import com.jogo.R;
import com.location.Location;
import com.render.lottie.LottieRender;
import com.utils.JOGOPaint;

import java.util.Random;

public class GrowingGrassAssets extends Location {

    private static final int MIN_JUMP_GAP = 2; // Minimum difference threshold to consider a valid jump.
    private static final int CHOP_MARGIN = 30; // Sets the progress of Grass lottie to the specified frame (30).
    private static final int JUMP_FRAMES = 20; // Display jump arrows back to the screen when person hasn't jumped for JUMP_FRAMES.
    private final int ASSET_ID;  // Every growingGrassAsset is maintained an ID for autonomous positioning of its lottieAssets.
    private static final float SPEED = 0.2f; // Sets the playback speed for Grass lottie asset only.
    private final double WIDTH; // To help separate the assets and check for condition in method->isPersonIn().
    private final LottieRender lottieGrass;
    boolean showLottieJump = true; // Set the flag depending on whether person jumps or hasn't jumped for JUMP_FRAMES.
    float max = 0.55f, min = 0.2f;
    private int jumpedSinceFrameId = 0; // Keeps track of No. of frames since Person last jumped.
    private int lastJumpFrameId = 0; // Maintains the FrameID when person jumps.
    private boolean isGrassFullyGrown = false; // Set to true when the grass lottie is fully grown / all frames have been played.
    private LottieRender lottieJumpArrow;


    public GrowingGrassAssets(double x, double y, double width, int assetID) {
        super(x, y);

        this.WIDTH = width;
        this.ASSET_ID = assetID;
        this.x = x + (assetID * width);

        lottieJumpArrow = new LottieRender(R.raw.jumparrow)
                .setScale(0f, 0f);

        lottieGrass = new LottieRender(R.raw.grass)
                .ephemeral(false)
                .setNormalizedLayout(0, 0, 0.1, 1)
                .setScaleType(ImageView.ScaleType.FIT_START)
                .speed(SPEED)
                .setNormalizedPosition(this.x, y)
                .play();

        lottieJumpArrow.onEnd(this::resetJumpArrow).play();
        lottieGrass.onEnd(this::gameOver);
    }

    private void resetJumpArrow() {

        // when person has jumped, hideLottieJump would be set to True. so that lottieJumpArrow wouldn't be reset again.
        if (showLottieJump) {

            lottieJumpArrow.clear(); //clear old animation. it looks messy on the screen.

            float yVal = 0.8f - (lottieGrass.getFrame() / lottieGrass.getMaxFrame()); // set the y_position of lottieJumpArrow w.r.t #frames processed for lottieGrass
            float xVal = (float) x;

            lottieJumpArrow = new LottieRender(R.raw.jumparrow)
                    .setScale(0.18f, 0.18f)
                    .setScaleType(ImageView.ScaleType.FIT_END)
                    .setNormalizedPosition(xVal, yVal);

            lottieJumpArrow.onEnd(this::resetJumpArrow).play();
        }
    }

    private void gameOver() {
        isGrassFullyGrown = true;
    }

    public void hideLottieJumpArrow() {
        lottieJumpArrow.hide();
    }

    public void hideLottieGrass() {
        lottieGrass.hide();
    }

    public void chop() {
        lottieGrass.setFrame(lottieGrass.getFrame() - CHOP_MARGIN);
    }

    public boolean isGrassFullyGrown() {
        return isGrassFullyGrown;
    }

    public boolean isPersonIn(PersonDetection person) {
        double rightAnkleX = person.rightLeg.ankle.getDetectedLocation().getX();
        double leftAnkleX = person.leftLeg.ankle.getDetectedLocation().getX();
        return ((rightAnkleX >= this.x && rightAnkleX <= this.x + this.WIDTH) || (leftAnkleX >= this.x && leftAnkleX <= this.x + this.WIDTH));
    }

    public void setRandomSpeed() {
        Random rand = new Random();
        float randomNum = (min + (max - min) * rand.nextFloat());
        this.lottieGrass.speed(randomNum);
    }


    public boolean hasJumped(PersonDetection person) {
        int jumpFrame = person.didJump(lastJumpFrameId);

        if (jumpFrame != lastJumpFrameId && (jumpFrame - lastJumpFrameId) >= MIN_JUMP_GAP) {
            this.lottieJumpArrow.hide(); // don't bother displaying the jump_arrows for (few frames) when person has jumped.
            showLottieJump = false;
            this.lastJumpFrameId = jumpFrame;
            jumpedSinceFrameId = lastJumpFrameId;
            return true;
        }
        return false;
    }


    public void process(PersonDetection person) {

        jumpedSinceFrameId++;

        // remind the person to jump when he hasn't jumped for some time (3.5 seconds).
        if (!showLottieJump && (jumpedSinceFrameId - lastJumpFrameId) > JUMP_FRAMES) {
            showLottieJump = true;
            resetJumpArrow();
        }

        if (hasJumped(person) && isPersonIn(person)) {
            this.setRandomSpeed();
            chop();
        }
    }


    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine((float) x * canvas.getWidth(), 0, (float) x * canvas.getWidth(), canvas.getHeight(), new JOGOPaint().blue().largeStroke());
        canvas.drawLine((float) (x + WIDTH) * canvas.getWidth(), 0, (float) (x + WIDTH) * canvas.getWidth(), canvas.getHeight(), new JOGOPaint().red().largeStroke());
    }

    @Override
    public void drawDebug(Canvas canvas) {
//can be overridden if required
    }


}
