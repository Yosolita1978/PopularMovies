package co.yosola.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private static final String TRAILER_THUMBNAIL_BASE_PATH = "https://img.youtube.com/vi/";
    private static final String END_THUMBNAIL_PATH = "/0.jpg";
    private static final String TRAILER_BASE_URL = "http://youtube.com/watch?v=";

    private static final String LAST_XCOORD = "stateXCoord";
    private static final String LAST_YCOORD = "stateYCoord";

    //Variables to the Movie Object
    private ScrollView mDetailScrollView;
    private String mMovieId;
    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieRelease;
    private TextView mMovieVote;
    private TextView mMoviesynopsis;
    private Movie mCurrentMovie;

    //Variables to the Trailer object
    private LinearLayout mTrailerList;
    private String[] mTrailerKeys;

    //Variables to the Review Object
    private LinearLayout mReviewList;
    private String[] mReviewAuthors;
    private String[] mReviewContent;
    private int reviewCounter;
    private TextView mAuthorReview;
    private TextView mContentReview;
    private Button mNextReview;

    private MovieDbHelper mDbHelper;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Log.d(TAG, "onCreate: started.");

        mDetailScrollView = findViewById(R.id.detail_scroll_view);
        mTrailerList = findViewById(R.id.trailer_list);
        mReviewList = findViewById(R.id.review_list);

        reviewCounter = 0;

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        mDbHelper = new MovieDbHelper(this);

        getIncomingIntent();

        new FetchTrailersTask().execute();
        new FetchReviewsTask().execute();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int xCoord = mDetailScrollView.getScrollX();
        int yCoord = mDetailScrollView.getScrollY();
        outState.putInt(LAST_XCOORD, xCoord);
        outState.putInt(LAST_YCOORD, yCoord);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mDetailScrollView.scrollTo(savedInstanceState.getInt(LAST_XCOORD),
                    savedInstanceState.getInt(LAST_YCOORD));
        }
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
            //Log.d(TAG, "getIncomingIntent: " + movieTitle_temp + movieRelease_temp + moviePoster_temp + movieVote_temp + movieSynopsis_temp);

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

        mCurrentMovie = movie;

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

        // Setup FAB to open EditorActivity
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        if(isTheMovieInFavorites()){
            mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.favorite_button_off)));
            mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
        } else{
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertFavoriteMovie();
                }
            });
        }
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
            loadCorrectTrailerUI();
        }
    }

    public void extractTrailerData(String trailersResponse) {
        try {
            JSONObject trailersJSON = new JSONObject(trailersResponse);
            JSONArray trailersArray = trailersJSON.getJSONArray("results");
            mTrailerKeys = new String[trailersArray.length()];

            for (int i = 0; i < trailersArray.length(); i++) {

                mTrailerKeys[i] = trailersArray.getJSONObject(i).optString("key");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Helper method to know if the results of the request are display in a Textview or and ImageView
    private void loadCorrectTrailerUI(){
        if (mTrailerKeys.length == 0){
            TextView trailerView = createNoTrailersView(this, mTrailerList);
        } else {
            for (int i = 0; i < mTrailerKeys.length; i++){
                final String trailerKey = mTrailerKeys[i];
                ImageView trailerView = createTrailerView(this, mTrailerList, i);
                loadMovieTrailerThumbnail(trailerView, trailerKey);
                setTrailerOnClickListener(this, trailerView, trailerKey);
            }
        }
    }

    // Helper method to return the TextView with no Trailers inside the LinearLayout
    private static TextView createNoTrailersView(Context context, LinearLayout container) {
        TextView trailerView = new TextView(context);
        trailerView.setText(R.string.no_trailers);
        trailerView.setPadding(0, 0, 0, 50);
        trailerView.setTextSize(15);
        container.addView(trailerView);

        return trailerView;
    }

    // Helper method to return the ImageView inside the LinearLayout
    private static ImageView createTrailerView(Context context, LinearLayout container, int index) {
        ImageView trailerView = new ImageView(context);
        setTrailerViewProperties(context, trailerView);
        container.addView(trailerView, index);

        return trailerView;
    }

    // Helper method to set the image in the ImageView using Picasso
    private void loadMovieTrailerThumbnail(ImageView trailerView, String trailerKey) {

        String trailerURL = TRAILER_THUMBNAIL_BASE_PATH + trailerKey + END_THUMBNAIL_PATH;

        Picasso.get().load(trailerURL)
                .placeholder(R.drawable.teasertrailers)
                .fit()
                .into(trailerView);
    }

    // Helper method to set an OnClickListener for each trailer in the results
    private static void setTrailerOnClickListener(final Context context, ImageView trailerView, final String trailerKey) {
        trailerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTrailer(context, trailerKey);
            }
        });
    }

    // Helper method to start the Youtube intent on each trailer in the list
    private static void launchTrailer(Context context, String trailerKey) {
        Uri youtubeLink = Uri.parse(TRAILER_BASE_URL  + trailerKey);
        Intent intent = new Intent(Intent.ACTION_VIEW, youtubeLink);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    // Helper method to set all the Layout params in the LinearLayout trailer_list
    private static void setTrailerViewProperties(Context context, ImageView trailerView) {

        int width = (int) context.getResources().getInteger(R.integer.trailerWidth);
        int height = (int) context.getResources().getInteger(R.integer.trailerHeight);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        params.setMargins(16, 8, 16, 8);

        trailerView.setLayoutParams(params);

        trailerView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        trailerView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL reviewsRequestUrl = NetworkUtils.buildReviewsUrl(mMovieId);
                return NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            extractReviews(result);
            loadCorrectReviewUI();
        }
    }

    public void extractReviews(String reviewsResponse) {
        try {
            JSONObject jsonReviewsObject = new JSONObject(reviewsResponse);
            JSONArray reviewsResults = jsonReviewsObject.getJSONArray("results");
            mReviewAuthors = new String[reviewsResults.length()];
            mReviewContent = new String[reviewsResults.length()];
            for (int i = 0; i < reviewsResults.length(); i++)
            {
                mReviewAuthors[i] = reviewsResults.getJSONObject(i).optString("author");
                mReviewContent[i] = reviewsResults.getJSONObject(i).optString("content");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadCorrectReviewUI(){
        if (mReviewContent.length == 0){
            TextView reviewView = createNoReviewsView(this, mReviewList);
        } else{
            if (mReviewContent.length == 1) {
                mNextReview = findViewById(R.id.next_review_button);
                mNextReview.setVisibility(View.GONE);

            }
            String authorLabel = getResources().getString(R.string.author_review_label);
            String authorText = mReviewAuthors[reviewCounter];
            String authorHeader = authorLabel + " " +  authorText;
            mAuthorReview = findViewById(R.id.author_text);
            mAuthorReview.setText(authorHeader);

            String contentText = mReviewContent[reviewCounter];
            mContentReview = findViewById(R.id.content_text);
            mContentReview.setText(contentText);

            mNextReview = findViewById(R.id.next_review_button);
            mNextReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewCounter < mReviewContent.length - 1) { reviewCounter++; }
                    else { reviewCounter = 0; }
                    loadCorrectReviewUI();
                }
            });
        }
    }

    // Helper method to return the TextView with no Reviews inside the LinearLayout
    private TextView createNoReviewsView(Context context, LinearLayout container) {

        mAuthorReview = findViewById(R.id.author_text);
        mAuthorReview.setVisibility(View.GONE);

        mContentReview = findViewById(R.id.content_text);
        mContentReview.setVisibility(View.GONE);

        mNextReview = findViewById(R.id.next_review_button);
        mNextReview.setVisibility(View.GONE);


        TextView reviewView = new TextView(context);
        reviewView.setText(R.string.no_reviews);
        reviewView.setPadding(0, 0, 0, 50);
        reviewView.setTextSize(15);
        container.addView(reviewView);

        return reviewView;
    }


    /**
     * Temporary helper method to search is the movie is in the db or not
     */
    private boolean isTheMovieInFavorites(){

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String selection = MovieContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(mCurrentMovie.getMovieID())};

        Cursor cursor = db.query(
                MovieContract.MoviesEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0){
            return false;
        }
        return true;
    }

    /**
     * Helper method to insert data into the database.
     */
    private void insertFavoriteMovie() {

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,

        ContentValues mValues = new ContentValues();
        mValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ID, mCurrentMovie.getMovieID());
        mValues.put(MovieContract.MoviesEntry.COLUMN_TITLE, mCurrentMovie.getmMovieTitle());
        mValues.put(MovieContract.MoviesEntry.COLUMN_POSTER, mCurrentMovie.getmMoviePosterPath());
        mValues.put(MovieContract.MoviesEntry.COLUMN_SYNOPSIS, mCurrentMovie.getmMovieSynopsis());
        mValues.put(MovieContract.MoviesEntry.COLUMN_AVERAGE_RATING, mCurrentMovie.getmMovieVoteAverage());
        mValues.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, mCurrentMovie.getmMovieReleaseDate());

        // Insert a new row for Toto in the database, returning the ID of that new row.

        long newRowId = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, mValues);

        if (newRowId == -1){
            mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.favorite_button_on)));
            Toast.makeText(DetailActivity.this,
                    "There was an error with the db", Toast.LENGTH_SHORT).show();

        } else{
            mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.favorite_button_off)));
            mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
            Toast.makeText(DetailActivity.this,
                    "Saved to Favorites!", Toast.LENGTH_SHORT).show();
        }
    }

}
