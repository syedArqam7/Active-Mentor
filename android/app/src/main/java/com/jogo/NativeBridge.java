package com.jogo;

import android.content.Intent;

import com.exercises.base.exerciseLoaders.ExerciseLoaderLandscape;
import com.exercises.base.exerciseLoaders.ExerciseLoaderPortrait;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


public class NativeBridge extends ReactContextBaseJavaModule {

    private final static boolean DEBUG = true;
    private final static boolean TO_JSON = true;

    public static boolean isDebug(){
        return DEBUG;
    }

    public static boolean isToJson(){
        return TO_JSON;
    }

    public NativeBridge(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "JugglingBridge";
    }

    @ReactMethod
    void startPortraitActivity(String exerciseSetting, int index) {
        ReactApplicationContext context = getReactApplicationContext();
        Intent intent = new Intent(context, ExerciseLoaderPortrait.class);
        intent.putExtra("exerciseSettings", exerciseSetting);
        intent.putExtra("index", index);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @ReactMethod
    void startLandscapeActivity(String exerciseSetting, int index) {
        ReactApplicationContext context = getReactApplicationContext();
        Intent intent = new Intent(context, ExerciseLoaderLandscape.class);
        intent.putExtra("exerciseSettings", exerciseSetting);
        intent.putExtra("index", index);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}
