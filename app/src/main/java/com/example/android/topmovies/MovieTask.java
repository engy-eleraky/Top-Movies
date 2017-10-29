package com.example.android.topmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * Created by Noga on 9/25/2017.
 */

public class MovieTask extends AsyncTask<String, String, ArrayList<MovieItem>> {
    private final onMoviesLoadedListner listener;
    Context context;
    ProgressBar loadingIndicator;
    String BaseUrl = "http://api.themoviedb.org/3/movie/";
    final String APPID_PARAM = "api_key";
    public MovieTask(Context context,onMoviesLoadedListner listener,ProgressBar loadingIndicator) {
        this.listener = listener;
        this.context = context;
        this.loadingIndicator=loadingIndicator;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingIndicator.setVisibility(View.VISIBLE);
    }


    @Nullable
    private String httpConnection(Uri builtUri ){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL urli = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) urli.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null)

                return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0)
                return null;

            return buffer.toString();
        } catch (IOException e) {
            Log.v(TAG, "problem in connection", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ArrayList<MovieItem> getDataFromJson(String JsonStr)
            throws JSONException

    {
        //parameters we need to fetch
        JSONObject movieJson = new JSONObject(JsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");
        ArrayList<MovieItem> moviesList = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            MovieItem moviee = new MovieItem();
            String id=movie.getString("id");
            moviee.setId(id);
            String title = movie.getString("original_title");
            moviee.setTitle(title);
            String poster = movie.getString("poster_path");
            String Pooster = "http://image.tmdb.org/t/p/w185" + poster;
            moviee.setPoster(Pooster);
            String overView = movie.getString("overview");
            moviee.setOverView(overView);
            double voteAverage = movie.getDouble("vote_average");
            moviee.setVoteAverage(voteAverage);
            String releaseDate = movie.getString("release_date");
            moviee.setReleaseDate(releaseDate);
            moviesList.add(moviee);

        }//for

        return moviesList;

    }//string

    private  void getTrailersFromJson( String JsonStr)
            throws JSONException {
        MovieItem moviee=new MovieItem();
        JSONArray trailers = new JSONObject(JsonStr).getJSONArray("results");
        ArrayList<MovieItem> moviesList = new ArrayList<>();
        for (int i = 0; i < trailers.length(); i++) {
            JSONObject trailer = trailers.getJSONObject(i);
            TrailerItem traileer = new TrailerItem();
            String name = trailer.getString("name");
            traileer.setName(name);
            String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?";
            String PARAM = "v=";
            String key = trailer.getString("key");
            Uri uri = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                    .appendQueryParameter(PARAM, key)
                    .build();
            traileer.setUrl(uri.toString());
            String trailerImagee = "https://img.youtube.com/vi" + key + "0.jpg";
            traileer.setTrailerImage(trailerImagee);
            moviee.addTrailers(traileer);
        }
        moviesList.add(moviee);

    }
    private  void getReviewsFromJson( String JsonStr)
            throws JSONException{
        MovieItem moviee=new MovieItem();
        JSONArray reviews = new JSONObject(JsonStr).getJSONArray("results");
        ArrayList<MovieItem> moviesList = new ArrayList<>();
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);
            ReviewItem revieew = new ReviewItem();
            String author=review.getString("author");
            revieew.setAuthor(author);
            String url=review.getString("url");
            revieew.setUrl(url);
            String content=review.getString("content");
            revieew.setContent(content);
            moviee.addReviews(revieew);
        }

        moviesList.add(moviee);

    }
    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {
        MovieItem moviee = new MovieItem();
        String ID = moviee.getId();
        if(ID!=null) {
            String JsonStr = httpConnection(TrailersUrl(ID));
            try {
                getTrailersFromJson(JsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonStr = httpConnection(ReviewsUrl(ID));
            try {
                getReviewsFromJson(JsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//if
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String movii = preferences.getString(MainActivityFragment.SPINNER_SELECTION, "");

        Uri builtUri = Uri.parse(BaseUrl).buildUpon().appendEncodedPath(movii)
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_Movie_API_KEY).build();
        String JsonStr=httpConnection(builtUri);
        try {

            return getDataFromJson(JsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }//back

    private Uri TrailersUrl(String ID) {
        Uri uriBuilder = Uri.parse(BaseUrl).buildUpon()
                .appendEncodedPath(ID)
                .appendEncodedPath("videos")
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_Movie_API_KEY)
                .build();
        return uriBuilder;
    }

    private Uri ReviewsUrl(String ID) {
        Uri uriBuilder = Uri.parse(BaseUrl).buildUpon()
                .appendEncodedPath(ID)
                .appendEncodedPath("reviews")
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_Movie_API_KEY)
                .build();
        return uriBuilder;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> result) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        listener.onMoviesLoaded(result);

    }//onpost

}//Async

