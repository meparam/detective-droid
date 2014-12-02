package com.michaelcarrano.detectivedroid;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.michaelcarrano.detectivedroid.models.AppSource;
import com.michaelcarrano.detectivedroid.models.AppSources;
import com.michaelcarrano.detectivedroid.models.Libraries;
import com.michaelcarrano.detectivedroid.models.Library;
import com.michaelcarrano.detectivedroid.services.DetectIntentService;
import com.michaelcarrano.detectivedroid.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mcarrano on 11/29/14.
 */
public class App extends Application {

    private static App instance;

    private SharedPreferences mPreferences;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            parseLibrariesJson();
        } catch (JSONException e) {
            Log.i("App", "parseLibrariesJson: " + e.toString());
        }

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        addDetectiveDroidApp();
    }

    private void addDetectiveDroidApp() {
        AppSources.getInstance().getSources().put(getPackageName(), new AppSource(getApplicationInfo(), false));
        scanInstalledApplication(getApplicationInfo());
    }

    public void scanInstalledApplication(ApplicationInfo appInfo) {
        Intent intent = new Intent(this, DetectIntentService.class);
        intent.putExtra(DetectIntentService.KEY_APPLICATION, appInfo);
        startService(intent);
    }

    private void parseLibrariesJson() throws JSONException {
        String jsonString = StringUtils.convertStreamToString(
                getResources().openRawResource(R.raw.libraries));
        JSONArray json = new JSONArray(jsonString);
        Set<Library> libraries = new HashSet<Library>();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = json.getJSONObject(i);
            String name = jsonObject.getString("name");
            String path = jsonObject.getString("path");
            String source = jsonObject.getString("source");
            libraries.add(new Library(name, path, source));
        }
        Libraries.getInstance().setLibraries(libraries);
    }

    public int getPreferenceScanSystemApps() {
        return mPreferences.getBoolean("pref_skip_system_apps", true) ? 1 : 0;
    }
}
