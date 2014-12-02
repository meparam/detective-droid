package com.michaelcarrano.detectivedroid.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by mcarrano on 11/29/14.
 */
public class BusUtil {

    private static Bus instance;

    private BusUtil() {
    }

    public static Bus getInstance() {
        if (instance == null) {
            instance = new Bus(ThreadEnforcer.ANY);
        }
        return instance;
    }

    public static void post(Object event) {
        getInstance().post(event);
    }

    public static void register(Object o) {
        getInstance().register(o);
    }

    public static void unregister(Object o) {
        getInstance().unregister(o);
    }
}
