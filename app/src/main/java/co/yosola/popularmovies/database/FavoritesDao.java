package co.yosola.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    LiveData<List<Favorites>> loadAllFavorites();

    @Query("SELECT * FROM favorites WHERE id = :id")
    Favorites getItemById(int id);

    @Query("SELECT * FROM favorites WHERE movieIMBD_id = :movieid")
    Favorites getItemByMovieId(String movieid);

    @Query("SELECT * FROM Favorites WHERE title LIKE :title")
    Favorites getMovieByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(Favorites favoritesEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(Favorites favoritesEntry);

    @Delete
    void deleteEntry(Favorites favoritesEntry);
}

