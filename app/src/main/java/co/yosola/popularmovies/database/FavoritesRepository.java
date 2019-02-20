package co.yosola.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoritesRepository {

    private FavoritesDatabase mDb;

    private static FavoritesRepository mInstance;
    public LiveData<List<Favorites>> mFavorites;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static FavoritesRepository getInstance(Context context) {

        if(mInstance == null){
            mInstance = new FavoritesRepository(context);
        }

        return mInstance;
    }

    private FavoritesRepository(Context context) {
        mDb = FavoritesDatabase.getInstance(context);
        mFavorites = getAllMovies();
    }



    private LiveData<List<Favorites>> getAllMovies(){

        return mDb.getFavoritesDao().loadAllFavorites();
    }

    public void insertNewFav(String movieid, String title, String posterUrl,
                                  String synopsis, String rating, String releaseDate) {

        Favorites favoriteMovie = new Favorites();
        favoriteMovie.setMovieIMBD_id(movieid);
        favoriteMovie.setTitle(title);
        favoriteMovie.setPosterUrl(posterUrl);
        favoriteMovie.setSynopsis(synopsis);
        favoriteMovie.setAverageRating(rating);
        favoriteMovie.setReleaseDate(releaseDate);

        insertNewFavMovie(favoriteMovie);
    }

    private void insertNewFavMovie(final Favorites favoriteEntry){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.getFavoritesDao().insertFavorite(favoriteEntry);
            }
        });
    }

    public Favorites getMovieByTitle(String title) {
        return mDb.getFavoritesDao().getMovieByTitle(title);
    }

    public Favorites getMovieByMovieId(String movieID) {
        return mDb.getFavoritesDao().getItemByMovieId(movieID);
    }

    public Favorites getMovieById(int movieId) {
        return mDb.getFavoritesDao().getItemById(movieId);
    }

    public void deleteFavMovie(final Favorites favoriteEntry) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.getFavoritesDao().deleteEntry(favoriteEntry);
            }
        });
    }

}
