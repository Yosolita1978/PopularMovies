package co.yosola.popularmovies;


import java.util.ArrayList;

public class Movie {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private String mMovieTitle;
    private String mMovieReleaseDate;
    private String mMovieSynopsis;
    private String mMovieVoteAverage;
    private String mMoviePosterPath;


    /**
     * No args constructor for use in serialization
     */
    public Movie() {

    }

    //This is my constructor

    public Movie(String movieTitle, String movieReleaseDate, String movieSynopsis, String moviePosterPath, String movieVoteAverage) {
        mMovieTitle = movieTitle;
        mMovieReleaseDate = movieReleaseDate;
        mMovieSynopsis = movieSynopsis;
        mMoviePosterPath = IMAGE_BASE_URL + moviePosterPath;
        ;
        mMovieVoteAverage = movieVoteAverage;
    }

    public static ArrayList<Movie> createMovieList(int numJsonObjects) {
        ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();
        for (int i = 0; i < numJsonObjects; i++) {
            moviesArrayList.add(new Movie());
        }

        return moviesArrayList;
    }

    public String getmMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.mMovieTitle = movieTitle;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.mMovieReleaseDate = movieReleaseDate;
    }

    public String getmMovieSynopsis() {
        return mMovieSynopsis;
    }

    public void setMovieVoteSynopsis(String movieSynopsis) {
        this.mMovieSynopsis = movieSynopsis;
    }

    public String getmMoviePosterPath() {
        return mMoviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.mMoviePosterPath = moviePosterPath;
    }

    public String getmMovieVoteAverage() {
        return mMovieVoteAverage;
    }

    public void setMovieVoteAverage(String movieVoteAverage) {
        this.mMovieVoteAverage = movieVoteAverage;
    }


}
