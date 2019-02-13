package co.yosola.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    // For debugging process
    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "favorites.db";
    public static final int VERSION_NUMBER = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + MovieContract.FavoritesEntry.TABLE_NAME
                        + " ("
                        + MovieContract.FavoritesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                        + MovieContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                        + MovieContract.FavoritesEntry.COLUMN_DESCRIPTION + " TEXT, "
                        + MovieContract.FavoritesEntry.COLUMN_POSTER + " TEXT, "
                        + MovieContract.FavoritesEntry.COLUMN_RATING + " REAL, "
                        + MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT);";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +
                MovieContract.FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
