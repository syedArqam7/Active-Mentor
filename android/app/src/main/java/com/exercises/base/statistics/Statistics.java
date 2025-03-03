package com.exercises.base.statistics;

import com.exercises.score.Score;
import com.utils.InfoBlob;
import com.utils.UtilArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class Statistics {

    protected Score score;
    protected UtilArrayList<InfoBlob> infoBlobArrayList;

    public Statistics(Score score, UtilArrayList<InfoBlob> infoBlobArrayList) {
        this.score = score;
        this.infoBlobArrayList = infoBlobArrayList;
    }

    public abstract void writeStatsToJSON(JSONObject dataJSONWriter) throws JSONException;
}
