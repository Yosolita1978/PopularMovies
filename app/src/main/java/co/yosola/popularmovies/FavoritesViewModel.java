package co.yosola.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import co.yosola.popularmovies.database.Favorites;
import co.yosola.popularmovies.database.FavoritesDatabase;

public class FavoritesViewModel extends AndroidViewModel {

    private static final String TAG = FavoritesViewModel.class.getSimpleName();

    private LiveData<List<Favorites>> favoriteMovies;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        FavoritesDatabase database = FavoritesDatabase.getInstance(this.getApplication());
        favoriteMovies = database.getFavoritesDao().loadAllFavorites();
        Log.d(TAG, "ViewModel: !!!!" + database);
    }

    public LiveData<List<Favorites>> getFavoriteMovies() {
        return favoriteMovies;
    }


}
