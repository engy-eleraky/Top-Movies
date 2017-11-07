package com.example.android.topmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.topmovies.data.MoviesContract;
import com.example.android.topmovies.data.MoviesDbHelper;

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
    private static final String TAG = MovieTask.class.getSimpleName();
    private static final String[] QUERY_COLUMNS = {
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_OVER_VIEW,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_POSTER,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };
    private static final int MOVIE_ID_INDEX = 0;
    private static final int MOVIE_ORIGINAL_TITLE_INDEX = 1;
    private static final int MOVIE_POSTER_INDEX = 2;
    private static final int MOVIE_OVERVIEW_INDEX = 3;
    private static final int MOVIE_RELEASE_DATE_INDEX = 4;
    private static final int MOVIE_VOTE_AVERAGE_INDEX = 5;
    final String APPID_PARAM = "api_key";
    private final onMoviesLoadedListner listener;
    Context context;
    ProgressBar loadingIndicator;
    String BaseUrl = "http://api.themoviedb.org/3/movie/";


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


    private ArrayList<MovieItem> queryFavoritMovies()
    {        ArrayList<MovieItem> moviesList = new ArrayList<>();
        Cursor cursor=context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,QUERY_COLUMNS,
                null,null,null);
        if (cursor.moveToFirst()) {
            do {
                MovieItem movie = new MovieItem();
              String id=  cursor.getString(MOVIE_ID_INDEX);
                movie.setId(cursor.getString(MOVIE_ID_INDEX));
                String title=cursor.getString(MOVIE_ORIGINAL_TITLE_INDEX);
                movie.setTitle(cursor.getString(MOVIE_ORIGINAL_TITLE_INDEX));
                movie.setOverView(cursor.getString(MOVIE_OVERVIEW_INDEX));
                movie.setReleaseDate(cursor.getString(MOVIE_RELEASE_DATE_INDEX));
                movie.setPoster(cursor.getString(MOVIE_POSTER_INDEX));
                movie.setVoteAverage(cursor.getDouble(MOVIE_VOTE_AVERAGE_INDEX));

                moviesList.add(movie);
            } while (cursor.moveToNext());
        }
        return moviesList;
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String movii = preferences.getString(MainActivityFragment.SPINNER_SELECTION, "");
        Uri builtUri = Uri.parse(BaseUrl).buildUpon().appendEncodedPath(movii)
                .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_Movie_API_KEY).build();
        String JsonStr=httpConnection(builtUri);
          if(movii.equals("favorits")){
              try {
                  return  queryFavoritMovies();
              } catch (Exception e) {
                  e.printStackTrace();
                  Log.e(TAG,"failed to query");
              }
          }
        else{

        try {

            return getDataFromJson(JsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }}
        return null;

    }//back


    @Override
    protected void onPostExecute(ArrayList<MovieItem> result) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        listener.onMoviesLoaded(result);

    }//onpost

}//Async

