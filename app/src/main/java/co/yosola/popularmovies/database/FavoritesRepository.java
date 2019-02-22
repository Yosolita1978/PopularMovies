package co.yosola.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import co.yosola.popularmovies.Movie;

public class FavoritesRepository {


    private static FavoritesDao favoritesDao;
    public LiveData<List<Favorites>> mFavoritesMovies;
    private FavoritesDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();



    public FavoritesRepository(Application application){

        mDb = FavoritesDatabase.getInstance(application);
        favoritesDao = mDb.FavoritesDao();
        mFavoritesMovies = favoritesDao.loadAllFavorites();
    }

    public LiveData<List<Favorites>> getmFavoritesMovies() {
        return mFavoritesMovies;
    }

    public void insertNewFav(Movie movie) {

        Favorites favMovie = new Favorites();
        favMovie.setMovieIMBD_id(String.valueOf(movie.getMovieID()));
        favMovie.setTitle(movie.getmMovieTitle());
        favMovie.setReleaseDate(movie.getmMovieReleaseDate());
        favMovie.setPosterUrl(movie.getmMoviePosterPath());
        favMovie.setAverageRating(movie.getmMovieVoteAverage());
        favMovie.setSynopsis(movie.getmMovieSynopsis());
        insertNewFavMovie(favMovie);
    }

    void insertNewFavMovie(final Favorites movie){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                favoritesDao.insertFavorite(movie);
            }
        });
    }

    public Favorites getMovieByTitle(String title) {
        return favoritesDao.getMovieByTitle(title);
    }

    public Favorites getMovieById(int movieId) {
        return favoritesDao.getItemById(movieId);
    }

    public Favorites getMovieByMovieId(String movieId) {
        return favoritesDao.getItemByMovieId(movieId);
    }

    public void deleteFavMovie(final Favorites movie) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                favoritesDao.deleteEntry(movie);
            }
        });
    }

}
