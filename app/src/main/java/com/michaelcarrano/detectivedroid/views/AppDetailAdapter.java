package com.michaelcarrano.detectivedroid.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelcarrano.detectivedroid.R;
import com.michaelcarrano.detectivedroid.models.Library;

import java.util.List;

/**
 * Created by mcarrano on 11/29/14.
 */
public class AppDetailAdapter extends RecyclerView.Adapter<AppDetailAdapter.ViewHolder> {

    private List<Library> mAppLibraries;

    public AppDetailAdapter(List<Library> mAppLibraries) {
        Log.e("AppDetailAdapter", "Constructor");
        this.mAppLibraries = mAppLibraries;
    }

    @Override
    public AppDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_app_detail_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppDetailAdapter.ViewHolder holder, int position) {
        Library library = mAppLibraries.get(position);
        Log.e("AppDetailAdapter", "Library: " + library.getName());
        holder.name.setText(library.getName());
        holder.itemView.setTag(library.getSource());
    }

    @Override
    public int getItemCount() {
        return mAppLibraries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
        }
    }

}