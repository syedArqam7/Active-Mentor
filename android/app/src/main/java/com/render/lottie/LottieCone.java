package com.render.lottie;

import android.os.SystemClock;
import android.widget.ImageView;

import com.jogo.R;

public class LottieCone extends LottieBase<LottieCone> {

    protected static final long DELAY = 33;
    protected static final long PERIOD = 100;

    public long fallOverTime = 0;
    public boolean fallOver = false;
    public boolean fallOverLeft = false;

    public LottieCone() {
        super(R.raw.cone, null);
        this.ephemeral(false).
                setScaleType(ImageView.ScaleType.FIT_START).
                wrapContent().
                setScale(0.40f, 0.40f).
                setLayout(10, 10, 500, 500)
                .maxFrame(100);
        play();

        animationContainer.addAnimatorUpdateListener(valueAnimator -> {
            if (fallOver)
                rotation(Math.min((90 * (SystemClock.elapsedRealtime() - fallOverTime) / (float) PERIOD), 90) * (fallOverLeft ? -1 : 1));
        });
    }

    public void fallOver(boolean fallOverLeft) {
        this.fallOver = true;
        this.fallOverLeft = fallOverLeft;
        this.fallOverTime = SystemClock.elapsedRealtime();
        setFrame(20).resume();
    }

    public void reset() {
        this.fallOver = false;
        this.fallOverTime = 0;
        rotation(0);
        play();
    }

}
