package com.michaelcarrano.detectivedroid.models;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by michaelcarrano on 8/30/14.
 */
public class Libraries {

    private static Libraries instance;

    private Set<Library> libraries = new HashSet<Library>();

    public static Libraries getInstance() {
        if (instance == null) {
            instance = new Libraries();
        }
        return instance;
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

}

