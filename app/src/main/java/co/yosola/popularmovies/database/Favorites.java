package co.yosola.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class Favorites {

    // title, date, poster, average vote, synopsis, id
    @PrimaryKey @NonNull
    private String id;
    private String movieIMBD_id;
    private String title;
    private String releaseDate;
    private String posterUrl;
    private String averageRating;
    private String synopsis;

    public Favorites(String movieid, String title, String releaseDate,
                          String posterUrlString, String averageRating, String synopsis, String id)
    {
        this.movieIMBD_id = movieid;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrlString;
        this.averageRating = averageRating;
        this.synopsis = synopsis;
        this.id = id;
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

    public String getdbId() {
        return id;
    }

    public void setdbId(String id) {
        this.id = id;
    }

    public String getdbSynopsis() {
        return synopsis;
    }

    public void setdbSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

}
