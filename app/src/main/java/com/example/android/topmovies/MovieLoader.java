package com.example.android.topmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Noga on 10/28/2017.
 */

public class MovieLoader extends AbstractLoader<ArrayList<MovieItem>> {
    ArrayList<MovieItem> mResults;
    public MovieLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<MovieItem> loadInBackground() {
        return null;
    }



}
