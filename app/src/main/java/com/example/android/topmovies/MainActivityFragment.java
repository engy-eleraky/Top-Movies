package com.example.android.topmovies;


import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener,onMoviesLoadedListner,ImageAdapter.ImageItemClickListner {
    public static final String RESULT_KEY = "myobj";
    public static  String SPINNER_SELECTION="decision";
    MovieTask movieTask;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    Spinner spinner;
    RecyclerView.LayoutManager layoutManager;
    Parcelable layout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String itemSelect;
    String itemselected;
    private static final String SAVED_LAYOUT_MANAGER = "layout";
    private static final String SAVED_SELECTED_POSITION="spinnerSelection";

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
        String pref=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SPINNER_SELECTION,"");
        outsate.putString(SAVED_SELECTED_POSITION,pref);
        super.onSaveInstanceState(outsate);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null  ){
            layout=savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
            itemSelect=savedInstanceState.getString(SAVED_SELECTED_POSITION,"");

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);
        spinner.setOnItemSelectedListener( this);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView =  rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //check portait or landscape
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ImageAdapter(getActivity(),null,this);
        recyclerView.setAdapter(adapter);


        movieTask = new MovieTask(getActivity(),this);
        movieTask.execute();

        return rootView;

    }//on create view


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            if(itemSelect!=null)
            { //restore the value iand save it n share
           //PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SPINNER_SELECTION,itemSelect).apply();
//            MovieTask movieTask = new MovieTask(getActivity(),this);
//            movieTask.execute();
       }
        else{
        itemselected = parent.getItemAtPosition(position).toString();
           getPrefernce(itemselected);
        }
    }

    private void getPrefernce(String s){
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        editor.putString(SPINNER_SELECTION,s);
        editor.apply();
        MovieTask movieTask = new MovieTask(getActivity(),this);
        movieTask.execute();
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

}//fragment

