package com.michaelcarrano.detectivedroid.activities;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.michaelcarrano.detectivedroid.R;
import com.michaelcarrano.detectivedroid.fragments.AppDetailFragment;
import com.michaelcarrano.detectivedroid.fragments.AppListFragment;
import com.michaelcarrano.detectivedroid.utils.BusUtil;
import com.michaelcarrano.detectivedroid.views.SlidingPanel;
import com.squareup.otto.Subscribe;

/**
 * Created by mcarrano on 11/29/14.
 */
public class MainActivity extends ActionBarActivity {

    private FrameLayout mAppListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mAppListLayout = (FrameLayout) findViewById(R.id.fragment_app_list_container);

        if (savedInstanceState == null) {
            // Setup the AppListFragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_app_list_container, new AppListFragment())
                    .commit();

            // Setup the AppDetailFragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_app_detail_container, new AppDetailFragment())
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusUtil.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusUtil.register(this);
    }

    @Subscribe
    public void onSlidingPanelStateChange(SlidingPanel.SlidingPanelStateChanged event) {
        int offset = 10; // Need to make sure panels overlap so sliding still works.
        float width = event.isPanelOpen() ? getResources().getDimension(R.dimen.menu_width_opened) :
                getResources().getDimension(R.dimen.menu_width_closed);
        SlidingPaneLayout.LayoutParams params = new SlidingPaneLayout.LayoutParams((int) width + offset, ViewGroup.LayoutParams.MATCH_PARENT);
        mAppListLayout.setLayoutParams(params);
    }
}
