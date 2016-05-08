package com.example.lucas.sampleapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucas.sampleapplication.helpers.BroadcastHelper;

/**
 * Created by lucas on 8/05/2016.
 */

// Based on: http://www.vogella.com/tutorials/AndroidBroadcastReceiver/article.html
public class BroadcastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        // From API 23 and up, it is necessary to check for permissions at runtime.
        // Check the tutorial below to see how this can be achieved.
        // http://developer.android.com/training/permissions/requesting.html
    }

    // This is linked to the Start Timer button via the XML file.
    public void startTimer(View view) {

        // Retrieving the text from the editText view.
        EditText text = (EditText) findViewById(R.id.edit_text_broadcast);

        int i = 0;
        boolean skip = false;

        // If the view was found, get the text from it.
        if (text != null) {
            try {
                i = Integer.parseInt(text.getText().toString());
            } catch (Exception e) {

                // If unable to parse the text into an Integer, default it to 10 and show a toast.
                i = 10;

                Toast.makeText(this, "Unable to parse text. Defaulted to " + i + " seconds",
                        Toast.LENGTH_SHORT).show();
                skip = true;
            }
        } else {
            i = 10;

            // If something happened with the view, default it to 10 and show a toast.
            Toast.makeText(this, "EditText error. Defaulted to " + i + " seconds",
                    Toast.LENGTH_SHORT).show();
            skip = true;
        }

        // Define the broadcast intent, this is an example of an intent where
        // we do not start a new UI activity for the user in the process.
        Intent intent = new Intent(this, BroadcastHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 123234345, intent, 0);

        // Start managing the alarm through the system service.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
        + (i * 1000), pendingIntent);

        // If a toast was previously shown, skip this one otherwise the time will seem a bit off
        // to the users.
        if (!skip) {

            // Language fix.
            if (i == 1) {
                Toast.makeText(this, "Alarm set for " + i + " second.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Alarm set for " + i + " seconds.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
