package co.yosola.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

//Content Provider for retrieving favorites data from the SQLite database
public class FavoritesProvider extends ContentProvider {

    private MovieDbHelper mMoviesDBHelper;

    @Override
    public boolean onCreate() {
        mMoviesDBHelper = new MovieDbHelper(this.getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = mMoviesDBHelper.getWritableDatabase();
        long movieId = database.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, values);
        if (movieId > 0) {
            Uri result = ContentUris.withAppendedId(uri, movieId);
            getContext().getContentResolver().notifyChange(result, null);
            return result;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        cursor = mMoviesDBHelper.getReadableDatabase().query(
                MovieContract.FavoritesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mMoviesDBHelper.getWritableDatabase();
        if (null == selection) {
            selection = "1";
        }
        int deleted = database.delete(MovieContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("No update is necessary for this application.");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("getType is not necessary for this application.");
    }

}
