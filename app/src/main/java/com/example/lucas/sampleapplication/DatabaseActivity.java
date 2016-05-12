package com.example.lucas.sampleapplication;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucas.sampleapplication.helpers.DatabaseContract;
import com.example.lucas.sampleapplication.helpers.DatabaseHelper;

// Based on: http://developer.android.com/guide/topics/providers/content-provider-creating.html
// http://developer.android.com/guide/topics/data/data-storage.html#db
// http://developer.android.com/training/basics/data-storage/databases.html
public class DatabaseActivity extends AppCompatActivity {

    // Defining the helper we are going to use throughout the class.
    private DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // Instantiating the helper.
        mDbHelper = new DatabaseHelper(this);
    }

    /* "C"RUD.
     *
     * The Create action for the database. This method allows the user to enter a row of data to
     * our SQLite database.
     */
    public void insertData(View v) {

        // Checking all the EditTexts.
        EditText day = (EditText) findViewById(R.id.database_add_day);
        EditText type = (EditText) findViewById(R.id.database_add_type);
        EditText max = (EditText) findViewById(R.id.database_add_max);
        EditText min = (EditText) findViewById(R.id.database_add_min);
        double maxDouble = 0.0;
        double minDouble = 0.0;

        if (day != null && type != null && max != null && min != null) {
            String dayText = day.getText().toString();
            String typeText = type.getText().toString();
            String maxText = max.getText().toString();
            String minText = min.getText().toString();

            // Ensuring nothing is null, empty or in the wrong data type.
            if (dayText.equals("")) {
                dayText = "Sunday";
            }

            if (typeText.equals("")) {
                typeText = "Clear";
            }

            if (maxText.equals("") || !isDouble(maxText)) {
                maxDouble = 26.3;
            } else {
                maxDouble = Double.parseDouble(maxText);
            }

            if (minText.equals("") || !isDouble(minText)) {
                minDouble = 13.7;
            } else {
                minDouble = Double.parseDouble(minText);
            }

            // Retrieving the Writable database with which we are going to add the data.
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            try {
                db.beginTransaction();

                // Creating a new ContentValues to hold the different parts of the data and to add
                // to the correct columns upon insertion.
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.Weather.COLUMN_NAME_DAY, dayText);
                values.put(DatabaseContract.Weather.COLUMN_NAME_TYPE, typeText);
                values.put(DatabaseContract.Weather.COLUMN_NAME_MAX, maxDouble);
                values.put(DatabaseContract.Weather.COLUMN_NAME_MIN, minDouble);

                // This newRowId will obtain the row number that our data was inserted into.
                long newRowId;
                newRowId = db.insert(DatabaseContract.Weather.TABLE_NAME,
                        DatabaseContract.Weather.COLUMN_NAME_ID, values);

                db.setTransactionSuccessful();

                // Notify the user of the successful action.
                Toast.makeText(this, "Added row: " + newRowId, Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(this, "Unable to add row, error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } finally {
                db.endTransaction();
                db.close();

                // Clear up the text inside the views. This iteration is useful when you have many
                // text fields to clear and don't want to instantiate each and every one of them to
                // remove the text inside them.
                ViewGroup linearLayout = (ViewGroup) findViewById(R.id.database_add_container);

                if (linearLayout != null) {
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        View view = linearLayout.getChildAt(i);

                        if (view instanceof EditText) {
                            ((EditText) view).setText("");
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Unable to add row due to EditTexts Error", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /* CRU"D"
     *
     * The "Delete" action for our database. This allows the user to delete a row through its ID.
     */
    public void deleteData(View v) {

        // Retrieving the ID.
        EditText editText = (EditText) findViewById(R.id.database_delete_id);

        if (editText != null) {
            if (editText.getText().toString().equals("")) {
                Toast.makeText(this, "Add an ID to be deleted.", Toast.LENGTH_SHORT).show();
            } else {

                // Ensuring it is an integer.
                if (isInteger(editText.getText().toString())) {
                    SQLiteDatabase db = mDbHelper.getReadableDatabase();

                    try {
                        db.beginTransaction();

                        Integer rowId = Integer.parseInt(editText.getText().toString());

                        // Adding the "WHERE" clause to the action:
                        // Column "ID" and data "rowID".
                        String selection = DatabaseContract.Weather.COLUMN_NAME_ID + " LIKE ?";
                        String[] selectionArgs = {
                                String.valueOf(rowId)
                        };

                        // Execute the delete action.
                        db.delete(DatabaseContract.Weather.TABLE_NAME, selection, selectionArgs);
                        db.setTransactionSuccessful();

                        Toast.makeText(this, "Delete Action Successful!", Toast.LENGTH_SHORT)
                                .show();
                    } catch (SQLException e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        db.endTransaction();
                        db.close();

                        // Reset the text in the view.
                        editText.setText("");
                    }

                } else {
                    Toast.makeText(this, "Add an ID to be deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /* CR"U"D
     *
     * The "Update" action for our database. This allows the user to update a record in the table.
     * For the purpose of this tutorial, only the "day" column is going to be editable.
     */
    public void updateData(View v) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try {
            db.beginTransaction();

            // Retrieving the text from the UI.
            EditText editText = (EditText) findViewById(R.id.database_update_day);
            String text = "";

            EditText editTextId = (EditText) findViewById(R.id.database_update_id);
            String id = "";
            int idInteger = 0;

            if (editText != null && editTextId != null) {

                // Ensuring the text is available, not null, not empty and in the correct data type.
                text = editText.getText().toString();
                id = editTextId.getText().toString();

                if (isInteger(id)) {
                    idInteger = Integer.parseInt(id);
                } else {
                    Toast.makeText(this, "ID defaulted to \"1\"", Toast.LENGTH_SHORT).show();
                }

                if (text.equals("")) {
                    text = "Sunday";
                    Toast.makeText(this, "Day defaulted to \"Sunday\"", Toast.LENGTH_SHORT).show();
                }
            } else {
                idInteger = 1;
                text = "Sunday";
                Toast.makeText(this, "An error has occurred, \n" +
                        "ID defaulted to 1 and \n" +
                        "Day defaulted to \"Sunday\"", Toast.LENGTH_SHORT).show();
            }

            // Creating a ContentValues variable to be used on the update process.
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Weather.COLUMN_NAME_DAY, text);

            // Adding the "WHERE" clause to update a single row.
            String selection = DatabaseContract.Weather.COLUMN_NAME_ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(idInteger) };

            // Executing the update action.
            int count = db.update(
                    DatabaseContract.Weather.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.setTransactionSuccessful();

            // Clearing the text views upon the successful completion of the action.
            if (editText != null && editTextId != null) {
                editText.setText("");
                editTextId.setText("");
            }

            Toast.makeText(this, "Row " + idInteger + " successfully updated.", Toast.LENGTH_SHORT)
                    .show();
        } catch (SQLException e) {
            Toast.makeText(this, "Unable to update row, error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // This method allows the user to add a few records through a single button click.
    public void seedData(View v) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try {

            // Using raw SQL to demonstrate another way to insert data.
            // Note the "null" values are used for the ID primary key which was added with the
            // autoincrement attribute.
            String[] insertSql = {
                    "INSERT INTO weather VALUES (null, 'Monday', 'Clear', 28, 10);",
                    "INSERT INTO weather VALUES (null, 'Tuesday', 'Sunny', 31, 14);",
                    "INSERT INTO weather VALUES (null, 'Wednesday', 'Sunny', 29, 16);",
                    "INSERT INTO weather VALUES (null, 'Thursday', 'Clear', 26, 12);",
                    "INSERT INTO weather VALUES (null, 'Friday', 'Rain', 22, 7);"
            };

            db.beginTransaction();

            // Executing the required queries.
            for (int i = 0; i < 5; i++) {
                db.execSQL(insertSql[i]);
            }

            db.setTransactionSuccessful();

            Toast.makeText(this, "5 rows added to the database!", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, "Unable to add rows to database, error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Removes all the data from the database.
    public void wipeData(View v) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try {
            db.beginTransaction();

            // Deletes everything from the table as no ID was provided (or a where clause for that
            // matter).
            db.execSQL("DELETE FROM " + DatabaseContract.Weather.TABLE_NAME);

            db.setTransactionSuccessful();

            Toast.makeText(this, "Successfully wiped data!.", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, "Unable to wipe database data, error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Returns true if the parameter is a double.
    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch(NumberFormatException e) {
            Toast.makeText(this, "Can't parse double, error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Returns true if the parameter is an integer.
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Can't parse integer, error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
