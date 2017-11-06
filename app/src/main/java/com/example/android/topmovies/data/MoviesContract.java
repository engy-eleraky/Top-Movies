package com.example.android.topmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Noga on 11/4/2017.
 */

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.topmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";


    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVER_VIEW = "over_view";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER= "poster";
        public static final String COLUMN_VOTE_AVERAGE= "vote_average";


        public static Uri buildMovieUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }


    }

}
