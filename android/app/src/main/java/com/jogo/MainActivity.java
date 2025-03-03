package com.jogo;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {


    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    private static Activity mCurrentActivity = null;

    public static Activity getActivity() {
        Activity activity;
        activity = mCurrentActivity;
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentActivity = this;
    }

    @Override
    protected String getMainComponentName() {
        return "jogo";
    }

}