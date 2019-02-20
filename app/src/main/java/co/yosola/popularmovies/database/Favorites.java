package co.yosola.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class Favorites {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private String movieIMBD_id;

    @ColumnInfo(name = "movie_title")
    private String title;

    @ColumnInfo(name = "movie_release_date")
    private String releaseDate;

    @ColumnInfo(name = "movie_poster_url")
    private String posterUrl;

    @ColumnInfo(name = "movie_rating")
    private String averageRating;

    @ColumnInfo(name = "movie_synopsis")
    private String synopsis;

    public Favorites(){}

    public Favorites(String movieid, String title, String releaseDate,
                          String posterUrlString, String averageRating, String synopsis)
    {
        this.movieIMBD_id = movieid;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrlString;
        this.averageRating = averageRating;
        this.synopsis = synopsis;
    }

    public String getdbMovieIMBDID(){
        return movieIMBD_id;
    }

    public void setdbMovieIMBDID(String movieIMBD_id){
        this.movieIMBD_id = movieIMBD_id;
    }

    public String getdbTitle() {
        return title;
    }

    public void setdbTitle(String title) {
        this.title = title;
    }

    public String getdbReleaseDate() {
        return releaseDate;
    }

    public void setdbReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getdbPosterUrl() {
        return posterUrl;
    }

    public void setdbPosterUrl(String posterUrlString) {
        this.posterUrl = posterUrlString;
    }

    public String getdbAverageRating() {
        return averageRating;
    }

    public void setdbAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public int getdbId() {
        return id;
    }

    public void setdbId(int id) {
        this.id = id;
    }

    public String getdbSynopsis() {
        return synopsis;
    }

    public void setdbSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

}
