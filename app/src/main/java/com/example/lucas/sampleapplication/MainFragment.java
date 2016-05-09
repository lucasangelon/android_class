package com.example.lucas.sampleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by lucas on 8/05/2016.
 */

// The fragment used in the main activity.
public class MainFragment extends Fragment {
    public static final String EXTRA_TEXT = "com.example.lucas.sampleapplication.EXTRA_TEXT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout and make use of this to add functionality to the buttons within.
        // There are two layouts called "fragment_main" in the res folder. One is activated on
        // portrait mode and the other on landscape. Change the orientation and see what happens!
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button buttonEmptyIntent = (Button) view.findViewById(R.id.button_empty_intent);
        buttonEmptyIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Running an intent through the button inside a fragment, notice the "getActivity"
                // bit.
                Intent intent = new Intent(getActivity(), DynamicActivity.class);
                startActivity(intent);
            }
        });

        /* Intent Extras Button.
         * Intents can carry key-value pair bits of information between activities. Let's add some
         * text to the "DynamicActivity" which does not show any text if navigated to through the
         * "Empty Intent" button.
         */
        Button buttonExtraIntent = (Button) view.findViewById(R.id.button_extra_intent);
        buttonExtraIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DynamicActivity.class);

                /* Adding the extra, the EXTRA_TEXT bit is the key and needs to be defined prior
                 * to its usage. EXTRA_<name> is the usual style for this.
                 *
                 * We are using the text set on the Strings Resource again for this, we retrieve
                 * it through getString(resource id).
                 */
                intent.putExtra(EXTRA_TEXT, getString(R.string.dynamic_activity_text));
                startActivity(intent);
            }
        });

        // Broadcast Receiver button.
        Button buttonBroadcastReceiver = (Button) view.findViewById(R.id.button_broadcast_receiver);
        buttonBroadcastReceiver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BroadcastActivity.class);
                startActivity(intent);
            }
        });

        // Implicit Intent Button.
        Button buttonImplicitIntent = (Button) view.findViewById(R.id.button_implicit_intent);
        buttonImplicitIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImplicitActivity.class);
                startActivity(intent);
            }
        });

        // Result Intent Button.
        Button buttonResultIntent = (Button) view.findViewById(R.id.button_result_intent);
        buttonResultIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                startActivity(intent);
            }
        });

        // Hardcoded List Button.
        Button buttonListFake = (Button) view.findViewById(R.id.button_list_fake);
        buttonListFake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });

        // Internal and External Storage Button.
        Button buttonStorage = (Button) view.findViewById(R.id.button_storage);
        buttonStorage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StorageActivity.class);
                startActivity(intent);
            }
        });

        // Return the view required by the fragment manager.
        return view;
    }
}
