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
    @Query("SELECT * FROM favorites ORDER BY title")
    LiveData<List<Favorites>> loadAllFavorites();

    @Query("SELECT * FROM favorites WHERE id = :id")
    Favorites getItemById(Long id);

    @Insert
    void insertFavorite(Favorites favoritesEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(Favorites favoritesEntry);

    @Delete
    void deleteEntry(Favorites favoritesEntry);
}

