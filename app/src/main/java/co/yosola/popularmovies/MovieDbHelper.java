package co.yosola.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + MovieContract.MoviesEntry.TABLE_NAME
                        + " ("
                        + MovieContract.MoviesEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MovieContract.MoviesEntry.COLUMN_MOVIEIMBD_ID + " TEXT NOT NULL, "
                        + MovieContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                        + MovieContract.MoviesEntry.COLUMN_SYNOPSIS + " TEXT, "
                        + MovieContract.MoviesEntry.COLUMN_POSTER + " TEXT, "
                        + MovieContract.MoviesEntry.COLUMN_AVERAGE_RATING + " REAL, "
                        + MovieContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
