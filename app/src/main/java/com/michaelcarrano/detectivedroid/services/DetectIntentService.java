package com.michaelcarrano.detectivedroid.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.michaelcarrano.detectivedroid.models.AppSource;
import com.michaelcarrano.detectivedroid.models.AppSources;
import com.michaelcarrano.detectivedroid.models.Libraries;
import com.michaelcarrano.detectivedroid.models.Library;
import com.michaelcarrano.detectivedroid.utils.BusUtil;

import java.util.ArrayList;

/**
 * Created by mcarrano on 11/29/14.
 */
public class DetectIntentService extends IntentService {

    public static final String KEY_APPLICATION = "KEY_APPLICATION";

    public DetectIntentService() {
        super("DetectIntentService");
    }

    private void detect(ApplicationInfo appInfo) {
        ArrayList<Library> libraries = new ArrayList<Library>();
        // Loop through known libraries
        for (Library library : Libraries.getInstance().getLibraries()) {
            try {
                Context ctx = createPackageContext(appInfo.packageName,
                        Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
                Class<?> clazz = Class.forName(library.getPath(), false, ctx.getClassLoader());

                // Detected a library!!!
                if (clazz != null) {
                    libraries.add(library);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Update the AppSource Object
        AppSource appSource = AppSources.getInstance().getSources().get(appInfo.packageName);
        appSource.setLibraries(libraries);
        update(appInfo, libraries);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final ApplicationInfo appInfo = intent.getParcelableExtra(KEY_APPLICATION);
        // Detect the Libraries in the application
        detect(appInfo);
    }

    // Post event to update Adapter in AppListFragment
    private void update(final ApplicationInfo appInfo, final ArrayList<Library> libraries) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Only post event if App has libaries
                if (!libraries.isEmpty()) {
                    Log.e("DetectIntentService", "Handler app scanned: " + appInfo.packageName);
                    BusUtil.getInstance().post(new AppScannedEvent(appInfo));
                }
            }
        });
    }

    public static class AppScannedEvent {
        private ApplicationInfo appInfo;

        public AppScannedEvent(ApplicationInfo appInfo) {
            this.appInfo = appInfo;
        }

        public ApplicationInfo getAppInfo() {
            return appInfo;
        }
    }
}
