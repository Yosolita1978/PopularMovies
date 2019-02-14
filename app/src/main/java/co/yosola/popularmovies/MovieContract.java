package co.yosola.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {

    public static final String CONTENT_AUTHORITY = "co.yosola.popularmovies";
    public static  final String PREFIX = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(PREFIX + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "favorites";


    // To prevent someone from accidentally instantiating the contract class
    private MovieContract(){

    }

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_ID = "_ID";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_AVERAGE_RATING = "average_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";


    }

}
