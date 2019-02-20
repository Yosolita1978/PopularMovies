package co.yosola.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


final public class NetworkUtils {

    final static String API_KEY_PARAM = "api_key";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/3/movie";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";
    private static final String PARAM_LANGUAGE = "language";
    private static final String language = "en-US";
    // A method to storage my api key private. See the build.gradle for more details.
    private static final String apiKey = BuildConfig.ApiKey;

    //The constructor should be overridden and marked as private so that they cannot be instantiated.
    private NetworkUtils() { }


    /**
     * This method builds the url from provided parameters
     */
    public static URL buildUrl(String sortOrder) {
        // build URL to return TMDb data in popular or top-rated sort order as desired
        Uri builtUri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(PARAM_LANGUAGE, language)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            //Log.e(TAG, "Problem making the HTTP request.", e);
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method builds the url to show the trailers
     */
    public static URL buildTrailersUrl(String movieid) {
        Uri uri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(movieid)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            //Log.e(TAG, "Problem making the HTTP request to trailers", e);
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method builds the url to show the reviews
     */
    public static URL buildReviewsUrl(String movieid) {
        Uri uri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon()
                .appendPath(movieid)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem making the HTTP request to reviews", e);
            e.printStackTrace();
        }
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {


        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            //Reading from open connection object
            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }
}
