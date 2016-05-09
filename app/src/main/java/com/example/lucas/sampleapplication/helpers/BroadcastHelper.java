package com.example.lucas.sampleapplication.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by lucas on 8/05/2016.
 */

// Alarm Broadcast Receiver.
// Based on: http://www.vogella.com/tutorials/AndroidBroadcastReceiver/article.html
public class BroadcastHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Shortened version of a Toast.
        Toast.makeText(context, "Time is up!", Toast.LENGTH_LONG).show();

        // Making use of the Vibrator Service, this is unavailable on the Android Emulator.
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
