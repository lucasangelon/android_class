package com.example.lucas.sampleapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// The main menu class for our project.
public class MainActivity extends AppCompatActivity {

    /* Setting up a tag for the log. Some developers use their initials so they can quickly find
     * them in the logcat screen, others use the class name for this.
     *
     * According to this GitHub repository of Android Guidelines:
     * https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md
     * They use the class name as the tag, which is what we are doing over here.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButtons();
    }

    // Setting up some of the buttons in the page.
    private void setupButtons() {

        // Toast Button.
        Button toastButton = (Button) findViewById(R.id.button_toast);
        assert toastButton != null;
        toastButton.setOnClickListener(new View.OnClickListener() {

            // Let's create the toast inside this button.
            public void onClick(View v) {

                // Defining the required parameters: context, text and time.
                Context context = getApplicationContext();
                String text = "You clicked the \"Create Toast\" button!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    // This button is linked to this action via the XML file, check activity_main.xml for the
    // onClick event.
    public void basicIntentClick(View v) {

        // Setting up a log to check if this button was clicked. Check these out on LogCat.
        Log.i(TAG, "button_basic_intent clicked!");

        // Setting up a basic intent and moving onto a new activity, no extra parameters are used.
        Intent intent = new Intent(this, BasicIntentActivity.class);
        startActivity(intent);
    }
}
