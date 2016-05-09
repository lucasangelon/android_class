package com.example.lucas.sampleapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

// Based on: http://developer.android.com/guide/components/intents-filters.html
public class ImplicitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit);
    }

    // Linked to the button via the XML file.
    public void sendMessage (View v) {

        // Creating an intent with the Send Message action.
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        // Retrieving the text from the TextView.
        TextView textView = (TextView) findViewById(R.id.text_implicit_activity);
        String text = "";

        if (textView != null) {
            text = (String) textView.getText();
        }

        // Putting the necessary "extra" on the intent.
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        // If possible, start the required intent (this will carry the message to an application
        // of your choice.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
