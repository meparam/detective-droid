package com.michaelcarrano.detectivedroid.models;

import android.content.pm.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Identifies a single application which uses libraries
 * <p/>
 * Created by michaelcarrano on 8/30/14.
 */
public class AppSource {

    // The application
    private ApplicationInfo applicationInfo;

    // Whether application is a system installed app
    private boolean isSystem;

    // List of libraries found in an application
    private List<Library> libraries;

    public AppSource(ApplicationInfo applicationInfo, boolean isSystem) {
        this.applicationInfo = applicationInfo;
        this.isSystem = isSystem;
        this.libraries = new ArrayList<Library>();
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo packageInfo) {
        this.applicationInfo = applicationInfo;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }
}
