package com.example.lucas.sampleapplication.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucas.sampleapplication.R;

/**
 * Created by lucas on 8/05/2016.
 */

// RecyclerView adapter for hardcoded data.
public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolder> {
    private String[] mDataset;

    // Constructor setting the dataset.
    public SimpleListAdapter(String[] dataset) {
        mDataset = dataset;
    }

    // Generating a new ViewHolder based on a TextView, here we can edit the properties of the view.
    @Override
    public SimpleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_simple, parent, false);

        ViewHolder vh = new ViewHolder((TextView) v);
        return vh;
    }

    // Setting the text based on the position in the Games array.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    // Getting the item count to generate the RecyclerView
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Defining the ViewHolder for this adapter.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }
}
