package com.render.lottie;


public class LottieCalibration {

    LottieRender firstAnimation, secondAnimation, thirdAnimation;
    boolean flip;
    double x, y;
    int SECOND_THIRD_ANIMATION_SYNC = 22; // We want the animations to look smooth. 
    public LottieCalibration(int firstAnimationName, int secondAnimationName, int thirdAnimationName, double x, double y, boolean flip) {
        firstAnimation = new LottieRender(firstAnimationName);
        secondAnimation = new LottieRender(secondAnimationName);
        thirdAnimation = new LottieRender(thirdAnimationName);

        this.x = x;
        this.y = y;

        this.flip = flip;

        secondAnimation.animationContainer.addAnimatorUpdateListener(valueAnimator -> {
            if (secondAnimation.getFrame() == SECOND_THIRD_ANIMATION_SYNC) {
                playThirdAnimation();
            }
        });

        if (flip) {
            firstAnimation.setScaleX(-1);
            secondAnimation.setScaleX(-1);
            thirdAnimation.setScaleX(-1);
        }

    }

    public LottieCalibration(int firstAnimationName, int secondAnimationName, int thirdAnimationName, boolean flip) {
        this(firstAnimationName, secondAnimationName, thirdAnimationName, 0.5, 0.5, flip);
    }

    public LottieCalibration(int firstAnimationName, int secondAnimationName, int thirdAnimationName) {
        this(firstAnimationName, secondAnimationName, thirdAnimationName, false);
    }

    public LottieCalibration(int firstAnimationName, int secondAnimationName, int thirdAnimationName, double x, double y) {
        this(firstAnimationName, secondAnimationName, thirdAnimationName, x, y, false);
    }

    public void playThirdAnimation() {
        this.thirdAnimation.play();
    }

    public LottieRender getFirstAnimation() {
        return firstAnimation;
    }

    public LottieRender getSecondAnimation() {
        return secondAnimation;
    }

    public LottieRender getThirdAnimation() {
        return thirdAnimation;
    }

    public void hide() {
        firstAnimation.hideAnimation();
        secondAnimation.hideAnimation();
        thirdAnimation.hideAnimation();
    }

    public void delete() {
        firstAnimation.delete();
        secondAnimation.delete();
        thirdAnimation.delete();
    }

    public void setAnimationSize(float animationWidth, float animationHeight) {

        if (flip)
            animationWidth = -Math.abs(animationWidth); // flip x-scale to mirror the image

        firstAnimation.setScale(animationWidth, animationHeight);
        secondAnimation.setScale(animationWidth, animationHeight);
        thirdAnimation.setScale(animationWidth, animationHeight);

        firstAnimation.setNormalizedPosition(x, y).ephemeral(false).play().loop();
        secondAnimation.setNormalizedPosition(x, y).ephemeral(false);
        thirdAnimation.setNormalizedPosition(x, y).ephemeral(false);
    }

    public int getFrame(LottieRender lottieRender) {
        return lottieRender.getFrame();
    }


}
