package com.example.lucas.sampleapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String TAG_FRAGMENT = "com.example.lucas.sampleapplication.MainFragment";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Upon a change of orientation (Landscape / Portrait), the activity is recreated BUT not
         * the fragments, these persist and, if you always "add" a fragment every time the activity
         * is created, you will end up with multiple layers of fragment using up memory and other
         * unwanted side effects.
         *
         * For the purpose of this application, we have a fragment that shows up upon clicking on
         * a button and the button disappears afterwards. When you change orientation, that button
         * comes back and will cause confusion to users. This "checkFragment" method ensures that
         * the fragment is removed upon change of orientation.
         */
        checkFragment();

        setupButton();

        // Show Fragment Button.
        final Button fragmentButton = (Button) findViewById(R.id.button_show_fragment);
        if (fragmentButton != null) {
            fragmentButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // Ensuring the layout has the view we wish to add our fragment to.
                    if (findViewById(R.id.frame_fragment) != null) {

                        /* If we are returning from a previously saved state, we should not
                         * generate a new fragment as we might end up with overlapping fragments.
                         */
                        if(savedInstanceState != null) {
                            Fragment fragment = getSupportFragmentManager()
                                    .findFragmentByTag(TAG_FRAGMENT);
                            if (fragment != null) {
                                return;
                            }
                        }

                        // Adding the fragment to the FrameLayout.
                        MainFragment fragment = new MainFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_fragment, fragment, TAG_FRAGMENT).commit();

                        // Let's remove the "Show Fragment" button after adding the fragment.
                        removeButton(fragmentButton);
                    }
                }
            });
        }
    }

    // Setting up the onClick event of the Toast Button without the aid of the XML file.
    private void setupButton() {

        // Toast Button.
        Button toastButton = (Button) findViewById(R.id.button_toast);
        if (toastButton != null) {
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
    }

    // Handling orientation changes.
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            checkFragment();
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            checkFragment();
        }
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

    // Removing a button from the view.
    private void removeButton(Button b) {
        if (b.getParent() != null) {
            ViewGroup vg = (ViewGroup) b.getParent();
            vg.removeView(b);
        }
    }

    // Orientation change helper, ensures the fragment is removed on change of orientation.
    private void checkFragment() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
