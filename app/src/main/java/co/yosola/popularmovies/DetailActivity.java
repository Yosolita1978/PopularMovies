package co.yosola.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    //private static final String TRAILER_THUMBNAIL_BASE_PATH = "https://img.youtube.com/vi/";
    //private static final String END_THUMBNAIL_PATH = "/0.jpg";

    private final String TRAILER_BASE_URL = "http://youtube.com/watch?v=";
    private final String PARAM_RESULTS = "results";
    private final String PARAM_KEY = "key";
    private final String PARAM_NAME = "name";

    private TextView mMovieTitle;

    private ImageView mMoviePoster;

    private TextView mMovieRelease;
    private TextView mMovieVote;
    private TextView mMoviesynopsis;

    private String[] mTrailerKeys;
    private String[] mTrailerNames;
    private String mMovieId;
    private LinearLayout mTrailerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Log.d(TAG, "onCreate: started.");

        mTrailerList = findViewById(R.id.trailer_list);

        getIncomingIntent();
        new FetchTrailersTask().execute();

    }

    private void getIncomingIntent() {
        //Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        int movieID = extras.getInt("movie_id");
        mMovieId = String.valueOf(movieID);
        String movieTitle_temp = extras.getString("movie_title");
        String movieRelease_temp = extras.getString("movie_release");
        String moviePoster_temp = extras.getString("movie_poster_url");
        String movieVote_temp = extras.getString("movie_vote");
        String movieSynopsis_temp = extras.getString("movie_synopsis");

        if (movieTitle_temp != null && movieRelease_temp != null && moviePoster_temp != null && movieVote_temp != null && movieSynopsis_temp != null) {
            Log.d(TAG, "getIncomingIntent: " + movieTitle_temp + movieRelease_temp + moviePoster_temp + movieVote_temp + movieSynopsis_temp);

            Movie newMovie = new Movie();
            newMovie.setMovieTitle(movieTitle_temp);
            newMovie.setMovieReleaseDate(movieRelease_temp);
            newMovie.setMoviePosterPath(moviePoster_temp);
            newMovie.setMovieVoteAverage(movieVote_temp);
            newMovie.setMovieVoteSynopsis(movieSynopsis_temp);

            setUI(newMovie);

        } else {
            //Log.d(TAG, "Something went  wrong with the intent");

            setUI(new Movie());
        }

    }

    private void setUI(Movie movie) {
        //Log.d(TAG, "setUI: setting the UI with the current Movie.");

        mMovieTitle = findViewById(R.id.movie_title_detail);
        mMovieTitle.setText(movie.getmMovieTitle());

        mMovieRelease = findViewById(R.id.movie_release_date_detail);
        mMovieRelease.setText(movie.getmMovieReleaseDate());

        mMovieVote = findViewById(R.id.movie_vote_detail);
        mMovieVote.setText(movie.getmMovieVoteAverage());

        mMoviePoster = findViewById(R.id.image_poster_details);
        String moviePosterImageURl = movie.getmMoviePosterPath();
        Picasso.get().load(moviePosterImageURl)
                .placeholder(R.drawable.moviedefaultscreen)
                .into(mMoviePoster);

        mMoviesynopsis = findViewById(R.id.movie_synopsis_detail);
        mMoviesynopsis.setText(movie.getmMovieSynopsis());

    }

    public class FetchTrailersTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL trailersRequestUrl = NetworkUtils.buildTrailersUrl(mMovieId);
                return NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            extractTrailerData(result);
            loadTrailerUI();
        }
    }

    public void extractTrailerData(String trailersResponse) {
        try {
            JSONObject jsonTrailersObject = new JSONObject(trailersResponse);
            JSONArray trailersResults = jsonTrailersObject.getJSONArray(PARAM_RESULTS);
            mTrailerKeys = new String[trailersResults.length()];
            mTrailerNames = new String[trailersResults.length()];
            for (int i = 0; i < trailersResults.length(); i++)
            {
                mTrailerKeys[i] = trailersResults.getJSONObject(i).optString(PARAM_KEY);
                mTrailerNames[i] = trailersResults.getJSONObject(i).optString(PARAM_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadTrailerUI() {
        if (mTrailerKeys.length == 0) {
            TextView noTrailers = new TextView(this);
            noTrailers.setText(R.string.no_trailers);
            noTrailers.setPadding(0, 0, 0, 50);
            noTrailers.setTextSize(15);
            mTrailerList.addView(noTrailers);
        }
        else {
            for (int i = 0; i < mTrailerKeys.length; i++) {
                Button trailerItem = new Button(this);
                trailerItem.setText(mTrailerNames[i]);
                trailerItem.setPadding(0, 30, 0, 30);
                trailerItem.setTextSize(15);
                final String trailerUrl = TRAILER_BASE_URL + mTrailerKeys[i];
                trailerItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        Uri youtubeLink = Uri.parse(trailerUrl);
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeLink);
                        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(youtubeIntent);
                        }
                    }
                });
                mTrailerList.addView(trailerItem);
            }
        }
    }


}
