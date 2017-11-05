package com.example.android.topmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.android.topmovies.data.MoviesContract;
import com.example.android.topmovies.data.MoviesDbHelper;

import java.io.Serializable;
import java.util.ArrayList;


public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        onMoviesLoadedListner,ImageAdapter.ImageItemClickListner {
    public static final String RESULT_KEY = "myobj";
    private static final String SAVED_LAYOUT_MANAGER = "layout";
    private static final String[] QUERY_COLUMNS = {
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_OVER_VIEW,
            MoviesContract.MovieEntry.COLUMN_POSTER,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };
    private static final int MOVIE_ID_INDEX = 0;
    private static final int MOVIE_ORIGINAL_TITLE_INDEX = 1;
    private static final int MOVIE_POSTER_INDEX = 2;
    private static final int MOVIE_OVERVIEW_INDEX = 3;
    private static final int MOVIE_RELEASE_DATE_INDEX = 4;
    private static final int MOVIE_VOTE_AVERAGE_INDEX = 5;
    public static  String SPINNER_SELECTION="decision";
    MovieTask movieTask;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    Spinner spinner;
    RecyclerView.LayoutManager layoutManager;
    Parcelable layout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String itemselected;
    ProgressBar loadingIndicator;
    MoviesDbHelper moviesDbHelper;
    Cursor cursor;
    private ArrayList<MovieItem> result = new ArrayList<>();


    public MainActivityFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outsate) {
        outsate.putParcelable(SAVED_LAYOUT_MANAGER, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outsate);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null  ){
            layout=savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
//            String prefs=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SPINNER_SELECTION,"");
//            if(prefs.equals("favorits")){
//                queryFavoritMovies();
//                onMoviesLoaded(result);}
//           else{
//                movieTask = new MovieTask(getActivity(), this, loadingIndicator);
//                movieTask.execute();
//
//           }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_list_item_array, R.layout.spinner_item);
        Adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(Adapter);
        spinner.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);

        String pref=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SPINNER_SELECTION,"");
        if(pref.equals("top_rated")){
            spinner.setSelection(1);

        }else if(pref.equals("popular")){
            spinner.setSelection(0);
        }
        else {spinner.setSelection(2);}
        spinner.setOnItemSelectedListener( this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageAdapter(getActivity(), null, this);
        recyclerView.setAdapter(adapter);

        loadingIndicator = rootView.findViewById(R.id.loading_indicator);
        recyclerView.setVisibility(View.VISIBLE);


        movieTask = new MovieTask(getActivity(), this, loadingIndicator);
        movieTask.execute();



        return rootView;

    }//on create view


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        itemselected = parent.getItemAtPosition(position).toString();
        getPrefernce(itemselected);
        if(itemselected.equals("favorits"))
        {
           queryFavoritMovies();
            onMoviesLoaded(result);
        }
        else {

        MovieTask movieTask = new MovieTask(getActivity(),this,loadingIndicator);
        movieTask.execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    @Override
    public void onMoviesLoaded(ArrayList<MovieItem> result) {
        ImageAdapter adapter = new ImageAdapter(getActivity(),result,this);
        recyclerView.setAdapter(adapter);
        restoreLayoutManagerPosition();

    }

    @Override
    public void onImageItemClick(Object movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class)
                .putExtra(RESULT_KEY, (Serializable) movie);
        startActivity(intent);


    }
    private void restoreLayoutManagerPosition() {
        if (layout != null ) {
            recyclerView.getLayoutManager().onRestoreInstanceState(layout);

        }
    }

    private void getPrefernce(String s){
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        editor.putString(SPINNER_SELECTION,s);
        editor.apply();

    }

    private ArrayList<MovieItem> queryFavoritMovies()
  {
    cursor=getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,QUERY_COLUMNS,
        null,null,null,null);
    if (cursor.moveToFirst()) {
        do {
            MovieItem movie = new MovieItem();
            movie.setId(cursor.getString(MOVIE_ID_INDEX));
            movie.setTitle(cursor.getString(MOVIE_ORIGINAL_TITLE_INDEX));
            movie.setPoster(cursor.getString(MOVIE_POSTER_INDEX));
            movie.setOverView(cursor.getString(MOVIE_OVERVIEW_INDEX));
            movie.setReleaseDate(cursor.getString(MOVIE_RELEASE_DATE_INDEX));
            movie.setVoteAverage(cursor.getDouble(MOVIE_VOTE_AVERAGE_INDEX));

            result.add(movie);
        } while (cursor.moveToNext());
    }
    return result;
  }
}//fragment

