package com.example.android.topmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.topmovies.data.MoviesContract.MovieEntry;

/**
 * Created by Noga on 11/4/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +


                        MovieEntry._ID                 + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +

                        MovieEntry.COLUMN_MOVIE_ID     + " TEXT  NOT NULL, "                     +

                        MovieEntry.COLUMN_TITLE        + " TEXT NOT NULL,"                       +

                        MovieEntry.COLUMN_OVER_VIEW    + " TEXT NOT NULL, "                      +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "                      +

                        MovieEntry.COLUMN_POSTER       + " TEXT NOT NULL, "                      +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "                      +

                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
