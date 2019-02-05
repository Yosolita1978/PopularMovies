package co.yosola.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    private TextView mMovieTitle;

    private ImageView mMoviePoster;

    private TextView mMovieRelease;
    private TextView mMovieVote;
    private TextView mMoviesynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Log.d(TAG, "onCreate: started.");

        getIncomingIntent();

    }

    private void getIncomingIntent() {
        //Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

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

}
