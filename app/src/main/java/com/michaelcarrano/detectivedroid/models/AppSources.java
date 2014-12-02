package com.michaelcarrano.detectivedroid.models;

import android.content.pm.ApplicationInfo;

import java.util.HashMap;

/**
 * Created by michaelcarrano on 8/30/14.
 */
public class AppSources {

    private static AppSources instance;

    private HashMap<String, AppSource> sources = new HashMap<String, AppSource>();

    public static AppSources getInstance() {
        if (instance == null) {
            instance = new AppSources();
        }
        return instance;
    }

    public HashMap<String, AppSource> getSources() {
        return sources;
    }

    public void setSources(HashMap<String, AppSource> sources) {
        this.sources = sources;
    }
}
