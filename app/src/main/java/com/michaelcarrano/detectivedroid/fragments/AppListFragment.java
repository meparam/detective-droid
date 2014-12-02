package com.michaelcarrano.detectivedroid.fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelcarrano.detectivedroid.App;
import com.michaelcarrano.detectivedroid.R;
import com.michaelcarrano.detectivedroid.models.AppSource;
import com.michaelcarrano.detectivedroid.models.AppSources;
import com.michaelcarrano.detectivedroid.services.DetectIntentService;
import com.michaelcarrano.detectivedroid.utils.BusUtil;
import com.michaelcarrano.detectivedroid.views.AppListAdapter;
import com.michaelcarrano.detectivedroid.views.RecyclerItemClickListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcarrano on 11/29/14.
 */
public class AppListFragment extends Fragment {

    private PackageManager mPackageManager;

    private List<ApplicationInfo> mScannedApplications;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAppListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AppListFragment() {
    }

    @Subscribe
    public void onAppScanComplete(DetectIntentService.AppScannedEvent event) {
        AppSource app = AppSources.getInstance().getSources().get(event.getAppInfo().packageName);
        Log.e("AppListFragment", "Scan completed: " + app.getLibraries().size() + " " + app.getApplicationInfo().packageName);
        mScannedApplications.add(event.getAppInfo());
        mAppListAdapter.notifyItemInserted(mScannedApplications.size() - 1);
//        BusUtil.getInstance().post(AppSources.getInstance().getSources().get(mInstalledApplications.get(position)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPackageManager = getActivity().getPackageManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);

        getInstalledApplications();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.scrollToPosition(0);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mScannedApplications = new ArrayList<ApplicationInfo>();
        mAppListAdapter = new AppListAdapter(getActivity(), mScannedApplications);
        mRecyclerView.setAdapter(mAppListAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("AppListFragment", "Clicked: " + view.getTag().toString());
                        BusUtil.getInstance().post(new AppSelectedEvent(view.getTag().toString()));
                    }
                })
        );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusUtil.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusUtil.getInstance().unregister(this);
    }

    private void getInstalledApplications() {
        boolean system;
        for (ApplicationInfo appInfo : mPackageManager.getInstalledApplications(0)) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0
                    || (appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                    || ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)) {
                system = true;
            } else {
                system = false;
            }

            if (!appInfo.packageName.equalsIgnoreCase(getActivity().getApplicationInfo().packageName)) {
                AppSources.getInstance().getSources().put(appInfo.packageName, new AppSource(appInfo, system));
                App.getInstance().scanInstalledApplication(appInfo);
            }
        }
    }

    public class AppSelectedEvent {
        private String packageName;

        public AppSelectedEvent(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return packageName;
        }
    }
}
