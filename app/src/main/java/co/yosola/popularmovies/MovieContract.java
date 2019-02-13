package co.yosola.popularmovies;

// The contract to create a DB.

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "co.yosola.popularmovies";

    public static final String PREFIX = "content://";

    public static final Uri URI_BASE = Uri.parse(PREFIX + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favorites";

    // To prevent someone from accidentally instantiating here is an empty constructor
    private MovieContract() {
    }

    public static class FavoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI = URI_BASE.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();

        public final static String TABLE_NAME = "favorites";

        /* Here are the columns */

        public static final String COLUMN_ID = "_ID";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster_key";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
