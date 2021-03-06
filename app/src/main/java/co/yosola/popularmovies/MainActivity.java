package co.yosola.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.yosola.popularmovies.database.Favorites;
import co.yosola.popularmovies.database.FavoritesRepository;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickLister {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVORITES = "Favorites";

    private static final String STATE_QUERY = "stateQuery";
    private static final String STATE_TITLE = "stateTitle";
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private String titleBySort = "Popular Movies";
    private String sortOrder = "popular";
    private ArrayList<Movie> movieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private Parcelable mSavedRecyclerLayoutState;
    private FavoritesRepository mFavRepo;
    private DetailViewModel mDetailViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up recyclerview and adapter to display the posters

        mErrorMessageDisplay = (TextView) findViewById(R.id.errors_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.grid);

        //to improve performance of the recycler view with the number of columns to show
        int numberOfColumns = GridLayoutUtils.calculateNumberOfColumns(getApplicationContext());
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //to improve performance of the recycler view
        mRecyclerView.setHasFixedSize(true);


        //initialize adapter and set to the recycler view object
        movieList = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(movieList, this);
        mRecyclerView.setAdapter(mMovieAdapter);


        // display progress bar, and load and display posters in preferred sort order
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);


        //check for network connection
        if (!isNetworkAvailable()) {
            showErrorMessage();
            mErrorMessageDisplay.setText(R.string.error_message_internet);
        }

        if (savedInstanceState != null) {
            sortOrder = savedInstanceState.getString(STATE_QUERY);
            titleBySort = savedInstanceState.getString(STATE_TITLE);
        }

        //set the tittle of the app - default to 'popular movies'
        setTitle(titleBySort);
        //build the url string - default to 'popular movies'
        startMovieSearch(sortOrder);

        // Initialize member variable for the data base
        mFavRepo = new FavoritesRepository(getApplication());
        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
    }

    //The void to check for network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnected());
    }

    //The method for show the error message
    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    //Bring the Json and bind it with the adapter
    private void showJSONData(String jsonData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        movieList = JsonUtils.parseMovieJsonToList(this, jsonData);
        mMovieAdapter.setPosterData(movieList);
    }

    //The method to star the task in the background.
    private void startMovieSearch(String sortOrder) {
        URL movieSearchURL = NetworkUtils.buildUrl(sortOrder);
        //fetch data on separate thread
        // and initialize the recycler viewer with data from movie adapter
        new FetchMovieTask().execute(movieSearchURL);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lastQuerySaved = sortOrder;
        String lastTitleSaved = titleBySort;
        outState.putString(STATE_QUERY, lastQuerySaved);
        outState.putString(STATE_TITLE, lastTitleSaved);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        }
    }


    @Override
    public void onListItemClick(Movie movie) {

        //Toast.makeText(this.getBaseContext(), "Movie: " + String.valueOf(movie.getMovieID()), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemThatWasSelected = menuItem.getItemId();
        if (itemThatWasSelected == R.id.most_popular) {
            sortOrder = POPULAR;
            titleBySort = "Popular Movies";
            setTitle(titleBySort);
            startMovieSearch(sortOrder);
            return true;
        }
        if (itemThatWasSelected == R.id.top_rated) {
            sortOrder = TOP_RATED;
            titleBySort = "Top Rated Movies";
            setTitle(titleBySort);
            startMovieSearch(sortOrder);
            return true;
        }
        if (itemThatWasSelected == R.id.favorites) {
            sortOrder = FAVORITES;
            titleBySort = "Favorites Movies";
            setTitle(titleBySort);
            loadFavorites();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // Helper method for saw the liveData favorites from db.
    private void loadFavorites() {

        final LiveData<List<Favorites>> favoriteslist = mDetailViewModel.getAllFavorites();
        favoriteslist.observe(this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(@Nullable List<Favorites> favorites) {
                populateFavorites(favorites);
            }
        });
    }

    // Helper method to populate the UI if there is favorites or not
    private void populateFavorites(List<Favorites> favorites) {

        if (favorites.size() == 0) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            showErrorMessage();
            mErrorMessageDisplay.setText(R.string.no_favorites);
        } else {
            ArrayList<Movie> favMoviesArray = new ArrayList<Movie>(favorites.size());
            for (int i = 0; i < favorites.size(); i++) {
                Favorites favMovie = favorites.get(i);
                Movie tempMovie = new Movie();
                tempMovie.setMovieID(Integer.valueOf(favMovie.getMovieIMBD_id()));
                tempMovie.setMovieTitle(favMovie.getTitle());
                tempMovie.setMovieReleaseDate(favMovie.getReleaseDate());
                tempMovie.setMovieVoteSynopsis(favMovie.getSynopsis());
                tempMovie.setMoviePosterPath(favMovie.getPosterUrl());
                tempMovie.setMovieVoteAverage(favMovie.getAverageRating());
                favMoviesArray.add(tempMovie);
            }
            mMovieAdapter.setPosterData(favMoviesArray);
        }

    }

    //Async inner class to fetch network data
    class FetchMovieTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl = params[0];
            String jsonData = null;
            try {
                jsonData = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (jsonData != null && !jsonData.equals("")) {
                super.onPostExecute(jsonData);
                showJSONData(jsonData);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            } else {
                showErrorMessage();
                mErrorMessageDisplay.setText(R.string.error_message);
            }
        }
    }


}
