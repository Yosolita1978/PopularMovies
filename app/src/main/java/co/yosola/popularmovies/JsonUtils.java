package co.yosola.popularmovies;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    //Create all the variables in the Movie Json

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String SYNOPSIS = "overview";
    private static final String POSTER_PATH = "poster_path";
    private static final String VOTE_AVERAGE = "vote_average";

    //For debugging reasons

    private static String TAG = JsonUtils.class.toString();

    //Parse the Json to create the new Movie object

    public static ArrayList<Movie> parseMovieJson(Context context, String json) {

        ArrayList<Movie> mMoviesList = new ArrayList<Movie>();

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            JSONObject jsonData = new JSONObject(json);

            JSONArray jsonArray = jsonData.optJSONArray(RESULTS);

            int jsonLength = jsonArray.length();

            for(int i = 0; i < jsonLength; i++){
                JSONObject movieTemp = jsonArray.optJSONObject(i);
                String title = movieTemp.optString(TITLE);
                String releaseDate = movieTemp.optString(RELEASE_DATE);
                String synopsis = movieTemp.optString(SYNOPSIS);
                String posterUrl = movieTemp.optString(POSTER_PATH);
                Double voteNum = movieTemp.optDouble(VOTE_AVERAGE);
                String voteString = String.valueOf(voteNum);

                mMoviesList.add(new Movie(title, releaseDate, synopsis, posterUrl, voteString));
            }


        } catch (JSONException e) {
            Log.d(TAG, "An error had occurred on JSON Parsing");
            e.printStackTrace();
        }

        return mMoviesList;
    }

}
