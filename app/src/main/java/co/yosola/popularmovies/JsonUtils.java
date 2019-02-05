package co.yosola.popularmovies;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    //Create all the variables in the Movie Json

    private static final String RESULTS = "results";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String ORIGINAL_TITLE = "original_title"; //title
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview"; // synopsis
    private static final String RELEASE_DATE = "release_date";

    //For debugging reasons

    private static String TAG = JsonUtils.class.toString();

    //Parse the Json to create the new Movie object

    public static ArrayList<Movie> parseMovieJsonToList(Context context, String json) {

        ArrayList<Movie> mMoviesList = new ArrayList<Movie>();

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            JSONObject jsonData = new JSONObject(json);

            JSONArray jsonArray = jsonData.optJSONArray(RESULTS);

            int jsonLength = jsonArray.length();

            for (int i = 0; i < jsonLength; i++) {
                JSONObject movieData = jsonArray.optJSONObject(i);
                String title = movieData.optString(ORIGINAL_TITLE);
                String posterSrc = movieData.getString(POSTER_PATH);
                String synopsis = movieData.getString(OVERVIEW);
                String releaseDate = movieData.getString(RELEASE_DATE);
                double userRating = movieData.optDouble(VOTE_AVERAGE);
                String voteString = String.valueOf(userRating);

                mMoviesList.add(new Movie(title, releaseDate, synopsis, posterSrc, voteString));
                //Log.d(TAG, posterSrc);
            }


        } catch (JSONException e) {
            //Log.d(TAG, "An error had occurred on JSON Parsing");
            e.printStackTrace();
        }

        return mMoviesList;
    }

}
