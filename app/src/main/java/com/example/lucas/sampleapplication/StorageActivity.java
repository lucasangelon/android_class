package com.example.lucas.sampleapplication;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// Based on: http://developer.android.com/guide/topics/data/data-storage.html#filesInternal
public class StorageActivity extends AppCompatActivity {
    private static final String FILENAME = "internal_file";
    private static final String FILENAME_EXTERNAL = "external_file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
    }

    /* Internal Storage
     *
     * You can save files directly on the device's internal storage and, by default, these are
     * private to your application and no other apps can access them. When your app is
     * uninstalled, so are these files.
     *
     * No write/read permissions are required for this process.
     */
    public void internalStorageCreate(View v) {

        // Retrieving the user's text.
        EditText editText = (EditText) findViewById(R.id.edit_text_storage_internal);
        String text;

        if (editText != null) {
            try {
                if (!editText.getText().toString().equals("")) {
                    text = editText.getText().toString();
                } else {
                    text = "Hello, world!";
                    Toast.makeText(this, getString(R.string.storage_empty), Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                text = "Hello, world!";
                Toast.makeText(this, getString(R.string.storage_text_error), Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            text = "Hello, world!";
            Toast.makeText(this, getString(R.string.storage_text_error), Toast.LENGTH_SHORT)
                    .show();
        }

        // Creating a file.
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // Note that we are writing bytes, this needs to be decoded for the reading process
            // if we would like to get a text out of it.
            fos.write(text.getBytes());
            fos.close();

            Toast.makeText(this, getString(R.string.storage_internal_success), Toast.LENGTH_SHORT)
                    .show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.storage_file_error), Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.storage_io_error), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // Reading the file.
    public void internalStorageRead(View v) {
        try {

            // Retrieving our textView where we are going to add the text from the file.
            TextView textView = (TextView) findViewById(R.id.text_storage_internal);

            FileInputStream fis = openFileInput(FILENAME);

            if (textView != null) {

                // http://stackoverflow.com/questions/13488317/convert-text-file-to-string-in-java
                // Checking the size of the file and generating a byte array.
                int size = fis.available();
                byte[] buffer = new byte[size];

                // Reading the file.
                fis.read(buffer);
                fis.close();

                // Setting the text.
                textView.setText(new String(buffer));
            } else {
                fis.close();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.storage_file_error), Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.storage_io_error), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    /* External Storage
     *
     * Depending on the type of external storage you are using, it may be accessible via USB and
     * other means (in which case this type of storage does not offer much security and should not
     * be used for important information such as passwords, personal information and others).
     *
     * This requires the WRITE_EXTERNAL_STORAGE permission for the file creation process.
     * READ_EXTERNAL_STORAGE is also required, however, by requiring the WRITE permission, the READ
     * permission is given automatically.
     */
    public void externalStorageCreate(View v) {

        // Ensuring we can write to the external storage.
        if (isExternalStorageWritable()) {

            // Retrieving the path we are going to be writing to. In this case, the base external
            // directory.
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path, FILENAME_EXTERNAL);

            // Retrieving the user's text.
            EditText editText = (EditText) findViewById(R.id.edit_text_storage_external);
            String text;

            if (editText != null) {
                try {
                    if (!editText.getText().toString().equals("")) {
                        text = editText.getText().toString();
                    } else {
                        text = "Hello, world!";
                        Toast.makeText(this, getString(R.string.storage_empty), Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (Exception e) {
                    text = "Hello, world!";
                    Toast.makeText(this, getString(R.string.storage_text_error), Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                text = "Hello, world!";
                Toast.makeText(this, getString(R.string.storage_text_error), Toast.LENGTH_SHORT)
                        .show();
            }

            // http://developer.android.com/reference/android/os/Environment.html
            try {

                // Writing out to a file using the same process as the internal file.
                FileOutputStream os = new FileOutputStream(file);

                os.write(text.getBytes());
                os.close();

                // Ensuring this file is available to the user immediately after creation.
                MediaScannerConnection.scanFile(this,
                        new String[] { file.toString() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });

                Toast.makeText(this, getString(R.string.storage_external_success), Toast.LENGTH_SHORT)
                        .show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, getString(R.string.storage_file_error), Toast.LENGTH_SHORT)
                        .show();
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.storage_io_error), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // Reading the file from the external storage.
    public void externalStorageRead(View v) {

        // Ensuring we can read the external storage directory.
        if (isExternalStorageReadable()) {

            // Retrieving the file.
            File file = new File(Environment.getExternalStorageDirectory(), FILENAME_EXTERNAL);

            try {

                // Retrieving the TextView where we are going to show the text from the file.
                TextView textView = (TextView) findViewById(R.id.text_storage_external);

                if (textView != null) {

                    // http://www.mysamplecode.com/2012/06/android-internal-external-storage.html
                    // Reading the file in another way.
                    FileInputStream fis = new FileInputStream(file);
                    DataInputStream dis = new DataInputStream(fis);
                    BufferedReader br = new BufferedReader(new InputStreamReader(dis));

                    String line;
                    String text = "";

                    while ((line = br.readLine()) != null) {
                        text += line;
                    }

                    dis.close();

                    // Setting the text to the TextView.
                    textView.setText(text);
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, getString(R.string.storage_file_error), Toast.LENGTH_SHORT)
                        .show();
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.storage_io_error), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /* Retrieving the External File
     *
     * As previously mentioned, files written to the external directory can be accessed via the SD
     * Card. If you would like to check the file which we just created with the previous method (in
     * your emulator), the method I used when creating this sample was the following:
     *
     * 1. Start your emulator with this sample application;
     * 2. Navigate to the storage example;
     * 3. Create an external file and read from it (to ensure it was successful);
     * 4. With the emulator still running, go to Android Studio's interface;
     * 5. Go to the "Tools" menu in the toolbar;
     * 6. Select "Android" -> "Android Device Monitor";
     * 7. In the Android Device Monitor, select the device you are currently emulating from the
     * list on the left.
     * 8. On the right side, click on the "File Explorer" tab;
     * 9. On the file tree below, open the "Storage" folder;
     * 10. Inside, open the "sdcard" folder;
     * 11. If you maintained the names as given in the sample application, look for
     * "external_file.txt", otherwise look for the name you have given to the file;
     * 12. Select the file;
     * 13. On the top-right corner, click the floppy-disk icon with an arrow to the left, the
     * "Pull File" option;
     * 14. Save the file where you would like on your computer / laptop; and
     * 15. Now, navigate to the file on your computer / laptop and open it with a text-editing
     * software (Notepad, TextEdit, Notepad++, Sublime Text, anything that runs .txt files) and
     * voilà!
     */

    // Checks if the external storage writing properties are accessible from the app.
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

    // Checks if the external storage reading properties are accessible from the app.
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }
}
