package co.yosola.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class FavoritesProvider extends ContentProvider {

    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = FavoritesProvider.class.getSimpleName();

    private static final int FAVORITES = 100;

    private static final int FAVORITE_ID = 101;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        mUriMatcher.addURI(
                MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, FAVORITES);

        mUriMatcher.addURI(
                MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", FAVORITE_ID);
    }

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        final SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = mUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                cursor = database.query(MovieContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return insertMovie(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }


    private Uri insertMovie(Uri uri, ContentValues values) {

        String movieId = values.getAsString(MovieContract.MoviesEntry.COLUMN_MOVIEIMBD_ID);

        if (movieId == null) {
            throw new IllegalArgumentException("Movie requires valid id");
        }

        //If everything is correct, we proceed to write into the database
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long newRowId = database.insert(MovieContract.MoviesEntry.TABLE_NAME, null, values);

        if (newRowId != -1) {
            Log.d(LOG_TAG, getContext().getResources().getString(R.string.log_insert_ok));
        } else {
            Log.d(LOG_TAG, getContext().getResources().getString(R.string.log_insert_failed));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, newRowId);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case FAVORITES:
                count = db.delete(MovieContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("No update is necessary for this application.");
    }


    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("getType is not necessary for this application.");
    }


}
