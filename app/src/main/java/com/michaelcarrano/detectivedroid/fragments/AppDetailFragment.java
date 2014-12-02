package com.michaelcarrano.detectivedroid.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelcarrano.detectivedroid.R;
import com.michaelcarrano.detectivedroid.models.AppSource;
import com.michaelcarrano.detectivedroid.models.AppSources;
import com.michaelcarrano.detectivedroid.utils.BusUtil;
import com.michaelcarrano.detectivedroid.views.AppDetailAdapter;
import com.michaelcarrano.detectivedroid.views.RecyclerItemClickListener;
import com.squareup.otto.Subscribe;

/**
 * Created by mcarrano on 11/29/14.
 */
public class AppDetailFragment extends Fragment {

    private TextView mAppName;

    private PackageManager mPackageManager;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAppDetailAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AppDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPackageManager = getActivity().getPackageManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_detail, container, false);
        mAppName = (TextView) rootView.findViewById(R.id.txt_app_name);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.scrollToPosition(0);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("AppListFragment", "Clicked: " + view.getTag().toString());
                        // TODO: Create intent to launch library link
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

    @Subscribe
    public void onAppSelected(AppListFragment.AppSelectedEvent event) {
        AppSource app = AppSources.getInstance().getSources().get(event.getPackageName());
        mAppName.setText(app.getApplicationInfo().loadLabel(mPackageManager));

        // TODO: Figure out if we can set the adapter in onCreateView (wasn't working at first)
        mAppDetailAdapter = new AppDetailAdapter(app.getLibraries());
        mRecyclerView.setAdapter(mAppDetailAdapter);
    }
}
