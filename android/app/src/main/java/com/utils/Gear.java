package com.utils;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.jogo.MainActivity;
import com.jogo.databinding.BasicExerciseViewBinding;
import com.logger.SLOG;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class Gear {
    /*
     * This function solves the compile time constants issue
     *  With this we can turn on and off specific branches of the code graph,
     * and change specific parameters.
     *
     */
    private static final Map<String, Boolean> boolMaps =  new ConcurrentHashMap<>();
    private static final Map<String, MutablePair<Consumer<Float>, Float>> floatMaps =  new ConcurrentHashMap<>();
    private static final Map<String, MutablePair<Consumer<Long>, Long>> longMaps =  new ConcurrentHashMap<>();

    public static boolean iff(String name, boolean onInit) {
        if (boolMaps.get(name) == null) {
            boolMaps.put(name, onInit);
        }
        return boolMaps.get(name);
    }

    public static String[] getGearList() {
        SLOG.d("Gear List: " + boolMaps.keySet());

        return boolMaps.entrySet().stream().map((e) -> e.getKey() + " : " + e.getValue()).toArray(String[]::new);
    }

    public static String[] getNameList() {
        SLOG.d("Gear List: " + boolMaps.keySet());
        return boolMaps.keySet().toArray(new String[0]);
    }

    public static boolean iff(String name) {
        return iff(name, true);
    }

    public static void setIff(String name, boolean b) {
        boolMaps.put(name, b);
    }

    public static boolean getIff(String name) {
        if (boolMaps.get(name) == null) return false;
        else return boolMaps.get(name);
    }

    public static void toggleIff(String name) {
        SLOG.d("Get Iff value: " + getIff(name));

        setIff(name, !getIff(name));
    }

    public static void FLOAT(String name, Consumer<Float> c, Float f) {
        if (floatMaps.get(name) == null) {
            floatMaps.put(name, new MutablePair<>(c, f));
        }
    }

    public static void setFLOAT(String name, float value) {
        floatMaps.get(name).left.accept(value);
        floatMaps.get(name).right = value;
    }

    public static float getFLOAT(String name) {
        return floatMaps.get(name).right;
    }

    public static String[] getGearFloatList() {
        return floatMaps.keySet().toArray(new String[0]);
    }

    public static void LONG(String name, Consumer<Long> c, Long l) {
        if (longMaps.get(name) == null) {
            longMaps.put(name, new MutablePair<>(c, l));
        }
    }

    public static void setLONG(String name, long value) {
        longMaps.get(name).left.accept(value);
        longMaps.get(name).right = value;
    }

    public static void initGearIFF(BasicExerciseViewBinding viewBinding) {
        SLOG.d("init gear");
        String[] gearSettings = getGearList();
        String[] nameList = getNameList();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getActivity(), android.R.layout.simple_spinner_dropdown_item);
        adapter.add("");
        // gear lists
        adapter.addAll(gearSettings);

        viewBinding.gearSettings.setAdapter(adapter);
        viewBinding.gearSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SLOG.d("item selected:" + id);
                // to skip default option
                if (id > 0) {
                    Gear.toggleIff(nameList[(int) id - 1]);
                    initGearIFF(viewBinding);
                } else
                    // clear selection, the selected one not work so we unclear the selection to work for the next selected gear.
                    viewBinding.gearSettings.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SLOG.d("nothing selected");
            }
        });
    }

}

