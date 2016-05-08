package com.example.lucas.sampleapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lucas.sampleapplication.adapters.SimpleListAdapter;

// List Activity with hardcoded data.
// Based on: http://developer.android.com/training/material/lists-cards.html
public class ListActivity extends AppCompatActivity {

    // Using a RecyclerView instead of a ListView, better performance-wise.
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }

        // Adding the required layout manager and obtaining the dataset from arrays.xml.
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] games = getResources().getStringArray(R.array.games);

        // Linking the adapter to the array and setting it to the RecyclerView.
        mAdapter = new SimpleListAdapter(games);
        mRecyclerView.setAdapter(mAdapter);
    }
}
