package com.example.lucas.sampleapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // Retrieving the Intent Extra.
        Intent intent = getIntent();
        String type = "";
        if (intent.hasExtra("LIST_TYPE")) {
            type = intent.getStringExtra("LIST_TYPE");
        }

        // Creating a bundle to hold our Intent Extra.
        Bundle bundle = new Bundle();
        bundle.putString("LIST_TYPE", type);

        // Instantiating the fragment and adding the bundle to it.
        ListViewFragment listFragment = new ListViewFragment();
        listFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_list_fragment, listFragment).commit();
    }
}
