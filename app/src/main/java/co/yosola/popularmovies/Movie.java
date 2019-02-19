package co.yosola.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

import java.util.ArrayList;

public class Movie implements Parcelable {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private String mMovieTitle;
    private String mMovieReleaseDate;
    private String mMovieSynopsis;
    private String mMovieVoteAverage;
    private String mMoviePosterPath;
    private int mMovieID;


    /**
     * No args constructor for use in serialization
     */
    public Movie() {

    }

    //This is my constructor

    public Movie(int movieID, String movieTitle, String movieReleaseDate, String movieSynopsis, String moviePosterPath, String movieVoteAverage) {
        mMovieID = movieID;
        mMovieTitle = movieTitle;
        mMovieReleaseDate = movieReleaseDate;
        mMovieSynopsis = movieSynopsis;
        mMoviePosterPath = IMAGE_BASE_URL + moviePosterPath;
        mMovieVoteAverage = movieVoteAverage;
    }

    //constructor used for parcel
    private Movie(Parcel in) {
        mMovieID = in.readInt();
        mMovieTitle = in.readString();
        mMovieSynopsis = in.readString();
        mMovieVoteAverage = in.readString();
        mMovieReleaseDate = in.readString();
        mMoviePosterPath = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mMovieID);
        out.writeString(mMovieTitle);
        out.writeString(mMovieSynopsis);
        out.writeString(mMovieVoteAverage);
        out.writeString(mMovieReleaseDate);
        out.writeString(mMoviePosterPath);
    }

    //used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };


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

    public int getMovieID() {
        return mMovieID;
    }

    public void setMovieID(int mMovieID) {
        this.mMovieID = mMovieID;
    }


}
