package co.yosola.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.grid);
        mRecyclerView.setHasFixedSize(true);

        int numberOfColumns = GridLayoutUtils.calculateNumberOfColumns(getApplicationContext());
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
        mErrorMessageDisplay = (TextView) findViewById(R.id.errors_view);

        new DownloadTask().execute();

    }

    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // First, hide the currently visible data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class DownloadTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            if (urls[0] != null) {
                String jsonResponse = "";
                try {
                    jsonResponse = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return JsonUtils.parseMovieJsonToList(MainActivity.this, jsonResponse);

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> arrayList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (arrayList == null) {
                mErrorMessageDisplay.setText(R.string.error_message);
                showErrorMessage();

            } else {


                showJsonDataView();
                mMovieAdapter = new MovieAdapter(MainActivity.this, mMovieList);
                mRecyclerView.setAdapter(mMovieAdapter);
                mMovieAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        Toast.makeText(MainActivity.this, movie.getmMovieTitle(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }
}
