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

    public void sendMessage (View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        TextView textView = (TextView) findViewById(R.id.text_implicit_activity);
        String text = "";

        if (textView != null) {
            text = (String) textView.getText();
        }

        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
