package com.example.lucas.sampleapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

// Based on: http://developer.android.com/training/basics/intents/result.html
public class ResultActivity extends AppCompatActivity {
    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    /* Linked to the button via the XML file. This opens the Contacts section and allows you to pick
     * a contact.
     *
     * For this example, if you are running this code on an emulator, it is necessary to add a
     * Contact with a Phone number. To do this, simply go to the contacts app and add a contact
     * with any phone number.
     *
     * Upon selecting that contact externally, the number will be saved to this activity via the
     * next method.
     */
    public void pickContact(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    // Upon return to this activity after selecting a contact, we are adding that phone number to
    // a TextView on the layout.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {

                // Retrieving the data from the intent.
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Starting to go through the data.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);

                if (cursor != null) {

                    // Picking the first result.
                    cursor.moveToFirst();

                    // Retrieving the phone number.
                    int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(column);
                    cursor.close();

                    // Adding it to the TextView.
                    TextView editText = (TextView) findViewById(R.id.edit_text_result);
                    if (editText != null) {
                        editText.setText(number);
                    }
                }
            }
        }
    }

    /* Note about this specific Permission
     *
     * According to the developer.android website's page used for this section, before Android
     * 2.3, performing such a contact retrieval query would require a permission declaration for
     * READ_CONTACTS. After this specific API (level 9), your application can get a temporary
     * permission to read contacts when it returns you a result.
     *
     * The temporary permission is only applied to the specific contact requested, meaning you
     * won't be able to query a contact other than the one specified by the Intent's URI unless
     * the permission READ_CONTACTS is declared / requested.
     */
}
