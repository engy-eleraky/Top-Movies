package com.example.android.topmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Noga on 10/31/2017.
 */

public class DetailsTask extends AsyncTask<String, String,MovieItem > {
    final String APPID_PARAM = "api_key";
    private final returnListener listener;
    Context context;
    MovieItem movie;
    String BaseUrl = "http://api.themoviedb.org/3/movie/";

    public DetailsTask (Context context,returnListener listener,MovieItem movie){
        this.context=context;
        this.listener=listener;
        this.movie=movie;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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

    private MovieItem getTrailersFromJson(MovieItem movie, String JsonStr)
            throws JSONException {
        JSONArray trailers = new JSONObject(JsonStr).getJSONArray("results");
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
            movie.addTrailers(traileer);
        }
       return movie;
    }

    private MovieItem getReviewsFromJson(MovieItem movie, String JsonStr)
            throws JSONException{
        JSONArray reviews = new JSONObject(JsonStr).getJSONArray("results");
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);
            ReviewItem revieew = new ReviewItem();
            String author=review.getString("author");
            revieew.setAuthor(author);
            String url=review.getString("url");
            revieew.setUrl(url);
            String content=review.getString("content");
            revieew.setContent(content);
            movie.addReviews(revieew);

        }
        return movie;
    }

    @Override
    protected  MovieItem doInBackground(String... params) {
        String ID =params[0];
        String JsonStr = httpConnection(TrailersUrl(ID));
        try {
            getTrailersFromJson(movie,JsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonStr = httpConnection(ReviewsUrl(ID));
        try {
            getReviewsFromJson(movie,JsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      return movie;
    }

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
    protected void onPostExecute( MovieItem result) {
        listener.onItemReturned(result);

    }//onpost

    public interface returnListener{
        void onItemReturned(MovieItem result );
    }
}
