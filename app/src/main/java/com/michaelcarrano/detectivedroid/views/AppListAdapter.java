package com.michaelcarrano.detectivedroid.views;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelcarrano.detectivedroid.R;

import java.util.List;

/**
 * Created by mcarrano on 11/29/14.
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<ApplicationInfo> mApplicationInfo;
    private PackageManager mPackageManager;

    public AppListAdapter(Context ctx, List<ApplicationInfo> mScannedApplications) {
        mPackageManager = ctx.getPackageManager();
        mApplicationInfo = mScannedApplications;
    }

    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_app_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppListAdapter.ViewHolder holder, int position) {
        ApplicationInfo appInfo = mApplicationInfo.get(position);
        holder.icon.setImageDrawable(appInfo.loadIcon(mPackageManager));
        holder.name.setText(appInfo.loadLabel(mPackageManager));
        holder.itemView.setTag(appInfo.packageName);
    }

    @Override
    public int getItemCount() {
        return mApplicationInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;

        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            icon = (ImageView) view.findViewById(R.id.img_icon);
        }
    }

}