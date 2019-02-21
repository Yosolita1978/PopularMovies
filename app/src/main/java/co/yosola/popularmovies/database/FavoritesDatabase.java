package co.yosola.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Favorites.class}, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavoritesDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "favorites";

    public abstract FavoritesDao FavoritesDao();

    private static FavoritesDatabase db;

    public static FavoritesDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(),
                    FavoritesDatabase.class,
                    FavoritesDatabase.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return db;
    }

}
