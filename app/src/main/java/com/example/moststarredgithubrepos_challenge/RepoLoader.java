package com.example.moststarredgithubrepos_challenge;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of repos by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class RepoLoader extends AsyncTaskLoader<List<Repo>> {

    /** Tag for log messages */
    private static final String LOG_TAG = RepoLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link RepoLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public RepoLoader(Context context, String url)
    {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading()
    {
        Log.i(LOG_TAG, "TEST: Calling onStartLoading()");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Repo> loadInBackground()
    {   Log.i(LOG_TAG, "TEST: Calling loadInBackground())");

        if (mUrl == null)
        {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of repos.
        List<Repo> repos = QueryUtils.fetchRepoData(mUrl);
        return repos;
    }


}