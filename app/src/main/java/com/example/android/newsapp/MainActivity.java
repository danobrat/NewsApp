package com.example.android.newsapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

/**
 * Displays information about a single earthquake.
 */
public class MainActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** URL to query the USGS dataset for earthquake information */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=531a9fe4-715c-48dd-ae74-8532372d338f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        // Kick off an  to perform the network request
        NewsAsyncTask task = new NewsAsyncTask();
        task.execute();
    }

    /**
     * Update the screen to display information from the given {NewsArticle}.
     */
    private void updateUi(NewsArticle news) {
        // Display the earthquake title in the UI
        TextView titleTextView = (TextView) findViewById(R.id.section_name);
        titleTextView.setText(news.section_name);

        // Display the earthquake date in the UI
        TextView dateTextView = (TextView) findViewById(R.id.web_title);
        dateTextView.setText(news.web_title);

        // Display whether or not there was a tsunami alert in the UI
        TextView tsunamiTextView = (TextView) findViewById(R.id.date);
        tsunamiTextView.setText(getDateString(news.date));
    }
    /**
     * Returns a formatted date and time string for when the earthquake happened.
     */
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'at' THH:mm:ss z");
        return formatter.format(timeInMilliseconds);
    }

    /**
     * {NewsAsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class NewsAsyncTask extends AsyncTask<URL, Void, NewsArticle> {

        @Override
        protected NewsArticle doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(GUARDIAN_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            NewsArticle news = extractResultsFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return news;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {NewsAsyncTask}).
         */
        @Override
        protected void onPostExecute(NewsArticle news) {
            if (news == null) {
                return;
            }

            updateUi(news);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode()==200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }else{
                    Log.e(LOG_TAG, "Error response code: "+urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the news article JSON results.", e);

                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {NewsArticle} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private NewsArticle extractResultsFromJson(String newsJSON) {
            try {
                JSONObject baseJsonResponse = new JSONObject(newsJSON);
                JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

                // If there are results in the results array
                if (resultsArray.length() > 0) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject properties = resultsArray.getJSONObject(0);

                    // Extract out the title, time, and tsunami values
                    String section_name = properties.getString("sectionName");
                    String web_title = properties.getString("webTitle");
                    int date = properties.getInt("webPublicationDate");

                    // Create a new {@link Event} object
                    return new NewsArticle(section_name, web_title, date);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the news article JSON results", e);
            }
            return null;
        }
    }
}