package com.example.android.topmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Noga on 10/28/2017.
 */

public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {
    public AbstractLoader(Context context) {
        super(context);
    }


}
