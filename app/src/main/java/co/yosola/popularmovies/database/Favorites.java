package co.yosola.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class Favorites {


    @PrimaryKey(autoGenerate = true)
    private int id;


    private String movieIMBD_id;


    private String title;


    private String releaseDate;


    private String posterUrl;


    private String averageRating;


    private String synopsis;

    public Favorites(){

    }


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

    @Ignore
    public Favorites(int id, String movieIMBDid, String title, String releaseDate,
                          String posterUrl, String averageRating, String synopsis)
    {
        this.id = id;
        this.movieIMBD_id = movieIMBDid;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.averageRating = averageRating;
        this.synopsis = synopsis;
    }

    public int getId() {
        return id;
    }

    public String getMovieIMBD_id() {
        return movieIMBD_id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieIMBD_id(String movieIMBD_id) {
        this.movieIMBD_id = movieIMBD_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
