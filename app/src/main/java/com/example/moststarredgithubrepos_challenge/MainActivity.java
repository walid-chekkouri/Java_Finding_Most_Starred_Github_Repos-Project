package com.example.moststarredgithubrepos_challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Repo>> {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private TextView mEmptyStateTextView;
    private RepoAdapter mRepoAdapter;
    View mLoadingIndicator;
    private static final String MY_URL_STRING = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
    private static final int REPO_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mRepoListView = (ListView) findViewById(R.id.reposListView);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mRepoListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of repos as input
        mRepoAdapter = new RepoAdapter(this, new ArrayList<Repo>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mRepoListView.setAdapter(mRepoAdapter);

        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected())
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(REPO_LOADER_ID, null, this);


        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator = findViewById(R.id.loading_indicator);
            mLoadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }


    @Override
    public Loader<List<Repo>> onCreateLoader(int i, Bundle bundle)
    {

        Uri baseUri = Uri.parse(MY_URL_STRING);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new RepoLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Repo>> loader, List<Repo> repos)
    {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        // Set empty state text to display "No repos found."
        mEmptyStateTextView.setText(R.string.no_repos);


        Log.i(LOG_TAG, "TEST: Calling on LoadFinished()");
        // Clear the adapter of previous repo data
        mRepoAdapter.clear();

        // If there is a valid list of {@link Repo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (repos != null && !repos.isEmpty())
        {
            mRepoAdapter.addAll(repos);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Repo>> loader)
    {


        // Loader reset, so we can clear out our existing data.
        mRepoAdapter.clear();
    }




}
