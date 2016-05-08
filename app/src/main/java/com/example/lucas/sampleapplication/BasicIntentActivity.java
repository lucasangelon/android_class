package com.example.lucas.sampleapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lucas on 8/05/2016.
 */

/* This page is empty, it is just an example of a new activity.
 * All activities must be declared within the manifest file.
 */
public class BasicIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_intent);
    }
}
