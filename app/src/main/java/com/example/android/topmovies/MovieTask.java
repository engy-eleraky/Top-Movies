package com.example.android.topmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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


/**
 * Created by Noga on 9/25/2017.
 */

public class MovieTask extends AsyncTask<String, String, ArrayList<MovieItem>> {
    private final onMoviesLoadedListner listener;
    Context context;
    ProgressBar loadingIndicator;
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


    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonStr = null;
        String BaseUrl = "http://api.themoviedb.org/3/movie/";
        final String APPID_PARAM = "api_key";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String movii = preferences.getString(MainActivityFragment.SPINNER_SELECTION, "");


        try {

            Uri builtUri = Uri.parse(BaseUrl).buildUpon().appendEncodedPath(movii)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_Movie_API_KEY).build();
            URL urli = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) urli.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }
            JsonStr = buffer.toString();

        } catch (IOException e) {

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {

                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }

        try {

            return getDataFromJson(JsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }//back

    @Override
    protected void onPostExecute(ArrayList<MovieItem> result) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        listener.onMoviesLoaded(result);

    }//onpost

}//Async

