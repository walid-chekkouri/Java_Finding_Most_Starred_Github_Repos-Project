package com.example.moststarredgithubrepos_challenge;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving repos data from the github API.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils()
    {
    }

    /**
     * Return a list of {@link Repo} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Repo> extractReposFromJSON(String RepoJSON)
    {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(RepoJSON))
        {
            return null;
        }

        // Create an empty ArrayList that we can start adding repos to
        List<Repo> repos = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            // For each repo in the reposArray, create an {@link Repo} object
            JSONObject baseJsonResponse = new JSONObject(RepoJSON);


            ///////////////////
            JSONArray itemsArray =  baseJsonResponse.getJSONArray("items");
            for(int i=0; i <itemsArray.length(); i++)
            {
                JSONObject repoItem = itemsArray.getJSONObject(i);

                String repoName = repoItem.getString("name");
                String repoDescription = repoItem.getString("description");
                int stargazersCount = repoItem.getInt("stargazers_count");

                JSONObject owner = repoItem.getJSONObject("owner");
                String repoOwnerName = owner.getString("login");
                String avatarUrl = owner.getString("avatar_url");

                //creating a repo object with the specific attributes
                Repo repo = new Repo(repoName, repoDescription, repoOwnerName, stargazersCount, avatarUrl);
                //then add it to our repos list
                repos.add(repo);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG , "Problem parsing the repo JSON results", e);
        }

        // Return the list of repos
        return repos;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl)
    {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the repo JSON results.", e);
        } finally {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (inputStream != null)
            {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Query the github API and return a list of {@link Repo} objects.
     */
    public static List<Repo> fetchRepoData(String requestUrl)
    {
        Log.i(LOG_TAG, "TEST: fetchRepoeData() called");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Repo}s
        List<Repo> repos = extractReposFromJSON(jsonResponse);

        // Return the list of {@link Repo}s
        return repos;
    }

}