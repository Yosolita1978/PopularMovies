package co.yosola.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class FavoritesProvider extends ContentProvider {

    /** Tag for log messages */
    public static final String LOG_TAG = FavoritesProvider.class.getSimpleName();

    private static final int FAVORITES = 100;

    private static final int FAVORITE_ID = 101;

    public static final Uri MOVIES_URI = Uri.parse(MovieContract.BASE_CONTENT_URI + "/" + MovieContract.MoviesEntry.TABLE_NAME);

    public static final Uri MOVIES_URI_ID = Uri.parse(MovieContract.BASE_CONTENT_URI + "/" + MovieContract.MoviesEntry.TABLE_NAME + "/" + FAVORITE_ID);

    private MovieDbHelper mDbHelper;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.MoviesEntry.TABLE_NAME, FAVORITES);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.MoviesEntry.TABLE_NAME + "/#", FAVORITE_ID);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (mUriMatcher.match(uri)){
            case FAVORITES:
                cursor = db.query(MovieContract.MoviesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase database = mDbHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case FAVORITES:
                long movieId = database.insert(MovieContract.MoviesEntry.TABLE_NAME, null, contentValues);
                if (movieId > 0) {
                    Uri result = ContentUris.withAppendedId(uri, movieId);
                    getContext().getContentResolver().notifyChange(result, null);
                }
                break;
            default:
                throw new SQLException("Failed to insert row" + uri);
        }
        return uri;

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)){
            case FAVORITES:
                count = db.delete(MovieContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case FAVORITE_ID:
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
