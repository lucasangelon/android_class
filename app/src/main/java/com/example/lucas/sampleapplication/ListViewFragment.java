package com.example.lucas.sampleapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lucas.sampleapplication.helpers.DatabaseContract;
import com.example.lucas.sampleapplication.helpers.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Based on: Udacity's "Developing Android Apps" lessons 1 and 2.
public class ListViewFragment extends Fragment {

    // Log TAG.
    private static final String TAG = ListViewFragment.class.getSimpleName();

    // Array Adapter for the ListView.
    private ArrayAdapter<String> mForecastAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        // Defining some hardcoded data to display and show the refresh upon loading the web
        // service details.
        String[] weatherForecast = {
            "Hardcoded Data 1", "Hardcoded Data 2"
        };

        // Defining the array adapter.
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(weatherForecast));
        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, weekForecast);

        ListView listView = (ListView) view.findViewById(R.id.listview_dynamic);
        listView.setAdapter(mForecastAdapter);

        // Retrieving the bundle containing the list type from the previous activity.
        Bundle bundle = this.getArguments();
        String type = "";

        if (bundle != null) {
            if (bundle.containsKey("LIST_TYPE")) {

                // Retrieving the bundle. If no mapping of the desired key exists, it will return
                // the default value "DATABASE".
                type = bundle.getString("LIST_TYPE", "DATABASE");
            }
        }

        if (type.equals("")) {
            type = "DATABASE";
        }

        // Check the fragment required based on the type given and start it.
        startFragment(type);

        // Starting the buttons for the database mode.
        Button buttonOpenDatabase = (Button) view.findViewById(R.id.button_open_database);

        if (type.equals("WEB")) {
            disableButton(buttonOpenDatabase);
        } else {
            buttonOpenDatabase.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DatabaseActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void startFragment(String type) {

        // For the web service.
        if (type.equals("WEB")) {
            FetchWeatherTask weatherTask = new FetchWeatherTask();

            // This number is the City ID for Perth, WA (Australia) in the OpenWeatherMap API.
            weatherTask.execute("2063523");
        } else if (type.equals("DATABASE")) {
            String[] listContent = selectData();

            mForecastAdapter.clear();

            for (String weather : listContent) {
                mForecastAdapter.add(weather);
            }
        }
    }

    private void disableButton(View v) {
        if (v instanceof Button) {
            v.setEnabled(false);
        }
    }

    private String[] selectData() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            db.beginTransaction();

            String[] projection = {
                    DatabaseContract.Weather.COLUMN_NAME_DAY,
                    DatabaseContract.Weather.COLUMN_NAME_TYPE,
                    DatabaseContract.Weather.COLUMN_NAME_MAX,
                    DatabaseContract.Weather.COLUMN_NAME_MIN
            };

            Cursor c = db.query(DatabaseContract.Weather.TABLE_NAME, projection, null, null, null,
                    null, null);

            ArrayList<String> listContent = new ArrayList<String>();

            if (c.moveToFirst()) {
                do {
                    String data = c.getString(c.getColumnIndexOrThrow(
                                            DatabaseContract.Weather.COLUMN_NAME_DAY));
                    data += " - " + c.getString(c.getColumnIndexOrThrow(
                                            DatabaseContract.Weather.COLUMN_NAME_TYPE));
                    data += " - " + c.getDouble(c.getColumnIndexOrThrow(
                                            DatabaseContract.Weather.COLUMN_NAME_MAX));
                    data += " / " + c.getDouble(c.getColumnIndexOrThrow(
                                            DatabaseContract.Weather.COLUMN_NAME_MIN));

                    listContent.add(data);
                } while(c.moveToNext());
            }

            c.close();

            int arraySize = listContent.size();
            String[] weatherData = new String[arraySize];

            for (int i = 0; i < arraySize; i++) {
                weatherData[i] = listContent.get(i);
            }

            db.setTransactionSuccessful();

            return weatherData;
        } catch (SQLException e) {
            Toast.makeText(getActivity(), "Unable to retrieve rows, error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction();
            db.close();
        }

        String[] error = { "Unable to retrieve data." };

        return error;
    }

    /* Web Services
     *
     * This is one of the ways to fetch information from a web service. The course outlined above
     * this class from Udacity goes through this topic in a lot of detail and is very helpful.
     *
     * This is not the most optimized way to handle web services, a better way is explored later on
     * the Udacity course*.
     *
     *
     * Why do we need web services?
     *
     * In order to update your data at runtime and provide the users with the latest data, you can
     * make use of Web Services that retrieve information from a location on the internet and bring
     * it back to your application. This can be done in many ways and the data can be in many
     * formats. For this example, we are using a JSON file retrieved from the OpenWeatherMap API.
     *
     *
     * What is this process:
     * 1. We create the code to go over to a given URL;
     * 2. Retrieve the information from there;
     * 3. Bring it back to our application; and
     * 4. Make use of it.
     */
    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        // Log Tag for this class.
        private final String TAG = FetchWeatherTask.class.getSimpleName();

        // Transforms a given date to a readable format, users don't want to see our code in the
        // list.
        private String getReadableDateString(long time) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd");
            return sdf.format(time);
        }

        // Formats the maximum and minimum temperatures for the day.
        private String formatHighLows(double high, double low) {
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            return roundedHigh + "/" + roundedLow;
        }

        // gets the weather data from the retrieved JSON file.
        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            // Getting the array we need out of the JSON object.
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // Due to the way the tutorial was structured, the dates given by the JSON will be
            // based on a time zone. In order to get this right and not mistake any days or time,
            // we are going to make use of the Julian Calendar.
            //
            // This class is *****deprecated***** and is just being used for the purpose of the tutorial at
            // Udacity. Make sure you use the GregorianCalendar class instead.
            Time dayTime = new Time();
            dayTime.setToNow();

            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
            dayTime = new Time();

            // The actual process to read the strings from the JSON file.
            String[] resultStrs = new String[numDays];
            for (int i = 0; i < weatherArray.length(); i++) {
                String day;
                String description;
                String highAndLow;

                // Getting the forecast for a given day.
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // Retrieving and transforming the date.
                long dateTime;
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                // Getting the weather object out of the array.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);

                // Getting the description from the weather object.
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Getting the temperature object out of the array.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);

                // Getting the maximum and minimum temperatures from the temperature object.
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                // Formatting the temperatures.
                highAndLow = formatHighLows(high, low);

                // Formatting the final string to be used by our TextView on the list.
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            // A log will be printed for each variable, this should never make its way into
            // production, it is here for debugging and tutorial purposes only.
            for (String s : resultStrs) {
                Log.v(TAG, "Forecast entry: " + s);
            }

            // Return the formatted strings.
            return resultStrs;
        }

        /* Threads
         *
         * A very important topic for mobile development. To explain this, let's see an example.
         *
         * You download a very cool weather application that shows you loads and loads of data for
         * each day of the week as well as loads of statistics about it. Let's imagine this data is
         * very, very heavy for the purpose of this example.
         *
         * If you download this information through a web service in your "onCreate" method of an
         * activity, it would need to finish executing before the screen updates to the user.
         *
         * What this means is, until it finishes, the screen is probably going to freeze. Your user
         * won't know what is going on, they might press buttons, exit the application or even
         * delete it from their devices if they believe it was an error / bug or it was about to
         * crash.
         *
         * Android will throw you an exception if you perform these kinds of tasks on what we call
         * the "UI Thread". We don't want our users to feel like the application is slow / buggy,
         * because of this, we make use of this awesome process of using Threads.
         *
         * By using "Threads" we can make sure the web service / data runs in the background and the
         * user can see some kind of spinner or an icon that says "Downloading...", this will let
         * users know what is going on and that the application has not crashed.
         *
         *
         * Using Threads
         *
         * UI Thread -> used for user's input and output, this is what the users can see.
         * Other threads -> Should be used for processing / web tasks.
         *
         * By ensuring that whenever a download starts we "move the web code" to a different thread,
         * the UI will never freeze and, once the "web code" finishes, the UI should be updated
         * according to our code.
         *
         * AsyncTasks (The example used here) have methods such as:
         * 1. onPreExecute
         * 2. doInBackground
         * 3. onProgressUpdate
         * 4. onPostExecute
         *
         * 1 -> Before starting a new thread and processing code, you can do things here.
         * 2 -> This runs entirely in the background, do the web processing over here.
         * 3 -> If the process takes a long time, you can show the user some updated on the UI with
         * this method.
         * 4 -> Once the web processing finishes, this method runs and you can make use of your data
         * here. You should return the changes with this method (such as showing the weather
         * forecast).
         *
         * You could say working with threads is essentially multitasking. You will be doing many
         * things at the same time but one affects each other. The UI Thread is the most important
         * one and should not be interrupted (frozen), the user should always have some sort of
         * action happening so they know the app is working. The background threads can be used
         * for processing and they will not even notice it! You can make really fast apps by making
         * use of Threads.
         *
         *
         * PUT EVERYTHING ON THREADS AND HAVE A LIGHTNING-FAST APP!
         *
         * Hold on, not that fast. Threads take up processing power, memory and can increase battery
         * usage. We do not need to put everything in background threads.
         *
         * By ensuring you are using background threads for time-consuming tasks or web processes,
         * your applications will become more user-friendly but will not be heavy on your user's
         * device. If you put everything on background threads, it could become a huge problem.
         *
         * For this example we are going to retrieve the data from the OpenWeatherMap API in a
         * background thread and, once that is finished, we will update the UI. There is no
         * demonstration of this process to the user (such as with a spinner) in this example.
         *
         */
        @Override
        protected String[] doInBackground(String... params) {

            // If we do not have a City ID, do nothing.
            if (params.length == 0) {
                return null;
            }

            // Defining the connections.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Defining the parameters.
            String forecastJsonStr = null;

            // You can get an API Key by signing up on the Open Weather Map API website, this
            // is free up to a certain usage.
            String appId = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
            String id = params[0];
            String format = "json";
            String units = "metric";
            int numberOfDays = 7;

            try {

                // Building the required URL with the parameters.
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String APPID = "appid";
                final String CITY_PARAM = "id";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID, appId)
                        .appendQueryParameter(CITY_PARAM, id)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numberOfDays))
                        .build();

                // Transform the URI to an URL.
                URL url = new URL(builtUri.toString());

                // Log to check what was built.
                Log.v(TAG, "Bulit URI: " + builtUri.toString());

                // Starting the connection with a "GET" request.
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Reading what was found in the URL.
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                // If nothing was in the file, do nothing.
                if (buffer.length() == 0) {
                    return null;
                }

                // Get ALL the data from the JSON file that was previously read and add it to this
                // String.
                forecastJsonStr = buffer.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "Error " + e.getMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, "Error " + e.getMessage(), e);
            } finally {

                // If the connection is still on, disconnect it.
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                // If the reader is still up and running, close it.
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error Closing Stream " + e.getMessage(), e);
                    }
                }
            }

            // Get the weather data from the JSON and format it with that method.
            try {
                return getWeatherDataFromJson(forecastJsonStr, numberOfDays);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            // If something else failed along the way, do nothing.
            return null;
        }

        // When everything else is done:
        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {

                // Clear the current adapter with "Hardcoded Data".
                mForecastAdapter.clear();

                // Iterate through the formatted strings and add each one of them to the list. The
                // user will be able to see this.
                for (String dayForecastStr : result) {
                    mForecastAdapter.add(dayForecastStr);
                }
            }
        }
    }
}
