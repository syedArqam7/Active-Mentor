package com.utils;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;

public class JOGOPaint extends Paint {
    protected final static int drawDebugTextHeight = (int) new JOGOPaint().red().fillStroke().small().monospace().getTextSize();
    protected final static int drawDebugLargeTextHeight = (int) new JOGOPaint().red().fillStroke().large().getTextSize();

    //given that at most 1 excercise can exists, we could refactor this into a singleton
    public static final int xValue = 20;
    protected static int drawDebugYHeight = 0;
    protected static int drawDebugStart = 300;
    public static JOGOPaint debugPaint = new JOGOPaint().red().fillStroke().small().monospace();
    public static JOGOPaint debugLargePaint = new JOGOPaint().red().fillStroke().large();


    /********** draw functions **********/
    //add some debug information to the screen
    public static int getNewDrawDebugHeight() {
        return (drawDebugYHeight += drawDebugTextHeight) + drawDebugStart;
    }

    public static int getNewDrawDebugLargeHeight() {
        return (drawDebugYHeight += drawDebugLargeTextHeight) + drawDebugStart;
    }

    public static void resetDrawDebugHeight() {
        JOGOPaint.drawDebugYHeight = 0;
    }


    public static void drawDebugText(String text, Canvas canvas) {
        canvas.drawText(text, 20, JOGOPaint.getNewDrawDebugHeight(), JOGOPaint.debugPaint);

    }

    public JOGOPaint transparancy(double transparancy) {
        setAlpha((int) ((1 - transparancy) * 255));
        return this;
    }

    public JOGOPaint fillStroke() {
        this.setStyle(Paint.Style.FILL_AND_STROKE);
        return (this);
    }

    public JOGOPaint fill() {
        this.setStyle(Paint.Style.FILL);
        return (this);
    }

    public JOGOPaint stroke() {
        this.setStyle(Style.STROKE);
        return (this);
    }

    public JOGOPaint monospace() {
        this.setTypeface(Typeface.MONOSPACE);
        return (this);
    }

    public JOGOPaint bioSansBold(AssetManager assets) {
        Typeface face;
        face = Typeface.createFromAsset(assets, "biosans-bold.otf");
        setTypeface(face);
        return (this);
    }

    public JOGOPaint monoSpecMedium(AssetManager assets) {
        Typeface face;
        face = Typeface.createFromAsset(assets, "monospec_medium.otf");
        setTypeface(face);
        return (this);
    }

    public JOGOPaint bioSansBold() {
        /***
         * Can only be used if the exercise is running, and abstract exercise is initialized
         */
        AssetManager assets = eActivity.getAssets();


        Typeface face;
        face = Typeface.createFromAsset(assets, "biosans-bold.otf");
        setTypeface(face);
        return (this);
    }

    public JOGOPaint yellow() {
        this.setColor(Color.YELLOW);
        return (this);
    }

    public JOGOPaint largeStroke() {
        this.setStyle(Paint.Style.STROKE);
        this.setStrokeWidth(10);
        return (this);
    }

    public JOGOPaint mediumStroke() {
        this.setStyle(Paint.Style.STROKE);
        this.setStrokeWidth(5);
        return (this);
    }

    public JOGOPaint narrowStroke() {
        this.setStyle(Paint.Style.STROKE);
        this.setStrokeWidth(3);
        return (this);
    }

    public JOGOPaint small() {
        this.setTextSize(25);
        setStrokeWidth(3);
        return (this);
    }

    public JOGOPaint medium() {
        this.setTextSize(50);
        setStrokeWidth(5);
        return (this);
    }

    public JOGOPaint large() {
        this.setTextSize(150);
        setStrokeWidth(8);
        return (this);
    }

    public JOGOPaint xl() {
        this.setTextSize(250);
        setStrokeWidth(10);
        return (this);
    }

    public JOGOPaint textSize(int size) {
        this.setTextSize(size);
        return this;
    }

    public JOGOPaint strokeWidth(int size) {
        this.setStrokeWidth(size);
        return this;
    }

    public JOGOPaint white() {
        this.setColor(Color.WHITE);
        return this;
    }

    public JOGOPaint blue() {
        this.setColor(Color.BLUE);
        return (this);
    }

    public JOGOPaint green() {
        this.setColor(Color.GREEN);
        return (this);
    }

    public JOGOPaint cyan() {
        this.setColor(Color.CYAN);
        return (this);
    }

    public JOGOPaint magenta() {
        this.setColor(Color.MAGENTA);
        return (this);
    }

    public JOGOPaint gray() {
        this.setColor(Color.GRAY);
        return (this);
    }

    public JOGOPaint setColorWithHex(String colorCode) {
        this.setColor(Color.parseColor(colorCode));
        return (this);
    }

    public JOGOPaint setColorWithRGB(int red, int green, int blue) {
        this.setColor(Color.rgb(red, green, blue));
        return (this);
    }

    public JOGOPaint jogoYellow() {
        this.setColor(Color.parseColor("#DBFF00"));
        return (this);
    }

    public JOGOPaint activeOrange() {
        this.setColor(Color.parseColor("#EC9F05"));
        return (this);
    }

    public JOGOPaint black() {
        this.setColor(Color.BLACK);
        return (this);
    }

    public JOGOPaint red() {
        this.setColor(Color.RED);
        return (this);
    }

    public JOGOPaint align(Align align) {
        this.setTextAlign(align);
        return this;
    }

    public JOGOPaint center() {
        this.setTextAlign(Align.CENTER);
        return this;
    }

    public float getTextHeight() {
        FontMetrics fm = getFontMetrics();
        return fm.descent - fm.ascent;
    }

    public float centerY() {
        Rect bounds = new Rect();
        getTextBounds("THISISALARGETEXT", 0, 16, bounds);
        return bounds.exactCenterY();

    }

}
