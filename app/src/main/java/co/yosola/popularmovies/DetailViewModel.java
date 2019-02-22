package co.yosola.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import co.yosola.popularmovies.database.Favorites;
import co.yosola.popularmovies.database.FavoritesRepository;

public class DetailViewModel extends AndroidViewModel {
    private FavoritesRepository favoritesRepository;
    private LiveData<List<Favorites>> mLiveMovie;
    private Executor executor = Executors.newSingleThreadExecutor();



    public DetailViewModel(@NonNull Application application) {
        super(application);
        favoritesRepository = new FavoritesRepository(application);
        mLiveMovie = favoritesRepository.getmFavoritesMovies();
    }

    public LiveData<List<Favorites>> getAllFavorites() {
        return mLiveMovie;
    }

    public boolean isFavorite(Movie movie) {
        String movieTitle = movie.getmMovieTitle();
        Favorites tempMovie = favoritesRepository.getMovieByTitle(movieTitle);

        if(tempMovie == null) {
            return false;
        }else{
            return true;
        }
    }




}
