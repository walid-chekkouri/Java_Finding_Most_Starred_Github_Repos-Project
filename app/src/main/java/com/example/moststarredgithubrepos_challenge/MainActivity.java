package com.example.moststarredgithubrepos_challenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Repo>> {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;

    private RecyclerView mRecyclerView;
    private RepoAdapter mRepoAdapter;
    private ArrayList<Repo> mRepos;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;
    private LoaderManager mLoaderManager;

    private int mPage=1;
    private static String mGithubUrl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
    private static final int REPO_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initialize();

    }

    private void initialize()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.repos_recycler_view);
        mRepos = new ArrayList<>();
        mRepoAdapter = new RepoAdapter(this, mRepos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            // for this tutorial, this is the ONLY method that we need, ignore the rest
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0)
                {

                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {


                        loadNextDataFromApi();

                    }
                }

                if (dy < 0) {


                    if (!recyclerView.canScrollVertically(-1)) {


                        loadPreviousDataFromApi();

                    }
                }

            }
        });

        mRecyclerView.setAdapter(mRepoAdapter);


        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
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
            mLoaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            mLoaderManager.initLoader(REPO_LOADER_ID, null, this);


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

        Uri baseUri = Uri.parse(mGithubUrl);
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

    }


    public void loadNextDataFromApi() {


        mGithubUrl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
        mGithubUrl+="&page=" + (++mPage);


        mLoaderManager.restartLoader(REPO_LOADER_ID, null, this);



    }

    public void loadPreviousDataFromApi() {


        mGithubUrl = "https://api.github.com/search/repositories?q=created:>2017-10-22&sort=stars&order=desc";
        String suffix="";

        if(mPage >1)
            suffix="&page=" + (--mPage);

        mGithubUrl+=suffix;

        mLoaderManager.restartLoader(REPO_LOADER_ID, null, this);



    }

}
