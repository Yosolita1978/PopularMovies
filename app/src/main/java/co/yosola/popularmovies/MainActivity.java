package co.yosola.popularmovies;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

import co.yosola.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up recyclerview and adapter to display the posters

        mErrorMessageDisplay = (TextView) findViewById(R.id.errors_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.grid);

        int numberOfColumns = GridLayoutUtils.calculateNumberOfColumns(getApplicationContext());
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);


        // display progress bar, and load and display posters in preferred sort order
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
        String sortOrder = getSortOrderSetting();
        loadPosters(sortOrder);

    }

    public String getSortOrderSetting() {
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        return mSettings.getString("sortOrder", POPULAR);
    }

    public void saveSortOrderSetting(String sortOrder) {
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("sortOrder", sortOrder);
        editor.apply();
    }

    @Override
    public void onClick(Movie movie) {
        // tapping a poster brings up DetailActivity with details. For now it makes a Toast
        Toast.makeText(MainActivity.this, movie.getmMovieTitle(), Toast.LENGTH_LONG).show();
    }

    private void loadPosters(String sortOrder) {
        showPosters();
        new FetchMovieTask().execute(sortOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu, with the settings action
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // bring up the settings dialog if the settings menu option is selected
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            settingsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingsMenu() {
        // the settings dialog lets you select betwen popular and top-rated poster display sort orders
        String sortOrder = getSortOrderSetting();
        final String previousSortOrder = sortOrder;
        int currentSetting = sortOrder.equals(POPULAR) ? 0 : 1;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // show radio buttons for the two options, with the current option selected
        builder.setTitle("Select poster sort order").setSingleChoiceItems(R.array.sort_orders, currentSetting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog alert = (AlertDialog) dialog;
                int selectedPosition = alert.getListView().getCheckedItemPosition();
                String sortOrder = new String[]{POPULAR, TOP_RATED}[selectedPosition];
                // if they changed the sort order and clicked OK, update both preferences and the display
                if (!sortOrder.equals(previousSortOrder)) {
                    saveSortOrderSetting(sortOrder);
                    loadPosters(sortOrder);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if they canceled, don't do anything
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPosters() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(errorMessage);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // using a background task, get the TMDb data and display the posters
    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            String sortOrder = params[0];

            URL postersRequestUrl = NetworkUtils.buildUrl(sortOrder);

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(postersRequestUrl);

                ArrayList<Movie> moviesFromJson = JsonUtils.parseMovieJsonToList(MainActivity.this, jsonResponse);

                return moviesFromJson;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showPosters();
                mMovieAdapter.setPosterData(movies);
            } else {
                showErrorMessage("No data was received - please check your Internet connection and try again.");
            }
        }
    }




}
