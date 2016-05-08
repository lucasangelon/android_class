package com.example.lucas.sampleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by lucas on 8/05/2016.
 */

// An activity that can be either empty or have some text depending on the Intent Extras.
public class DynamicActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        // Retrieving the intent.
        Intent intent = getIntent();

        String text = "";

        // If the intent has the key-value pair we are looking for, execute the following code.
        if (intent.hasExtra(MainFragment.EXTRA_TEXT)) {

            // Using logs to check if the intent has extras or not.
            Log.i(TAG, "Intent has extra!");
            text = intent.getStringExtra(MainFragment.EXTRA_TEXT);
            Log.i(TAG, "The text is:" + text);

            TextView textView = (TextView) findViewById(R.id.text_dynamic_activity);
            if (textView != null) {
                textView.setText(text);
            }
        }
    }
}
