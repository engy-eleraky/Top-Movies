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
    RecyclerView recyclerView;
    ImageAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String selection;
    Spinner spinner;
    SharedPreferences.Editor editor;
    Parcelable layout;
    String itemSelect;
    //SharedPreferences preferences;
    //String item;
    String itemselected;
    private static final String SAVED_LAYOUT_MANAGER = "layout";
    String newSpinnerPostion;
    private static final String SAVED_SELECTED_POSITION="spinnerSelection";
    MovieTask movieTask;

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
        // outsate.putInt(SAVED_SELECTED_POSITION,spinner.getSelectedItemPosition());
        String pref=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SPINNER_SELECTION,"");
       outsate.putString(SAVED_SELECTED_POSITION,pref);
        super.onSaveInstanceState(outsate);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null  ){
            layout=savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
            //spinner.setSelection(savedInstanceState.getInt(SAVED_SELECTED_POSITION));
            itemSelect=savedInstanceState.getString(SAVED_SELECTED_POSITION,"");

//            if(itemselected!=null){
//               itemSelect=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(itemselected,null);
////                movieTask = new MovieTask(getActivity(),this);
//                movieTask.execute();

             //getPrefernce(itemSelection);
            //}
//            if(item != null){
//                editor.putString(SPINNER_SELECTION,item);
//                editor.apply();
//                MovieTask movieTask = new MovieTask(getActivity(),this);
//                movieTask.execute();
            //}
            //newSpinnerPostion=savedInstanceState.getString(SAVED_SELECTED_POSITION,"");
//                item=newSpinnerPostion;
            //null
//            if(item!=null){
//            getPrefernce(item);}
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
        if(layout!=null){
            if(itemSelect=="top_rated")
                spinner.setSelection(1);
            else if (itemSelect=="popular"){
                spinner.setSelection(0);
            }
           // getPrefernce(itemSelect);
        }
        //restoreLayoutManagerPosition();
//        if(itemSelect=="top_rated")
//            spinner.setSelection(1);
//        else {
//            spinner.setSelection(0);
//        }
//        itemselected=itemSelect;
//        getPrefernce(itemselected);

        //getPrefernce(itemSelect);
        //restoreLayoutManagerPosition();
        spinner.setOnItemSelectedListener( this);
        //Adapter.notifyDataSetChanged();
        //itemselected=spinner.getSelectedItem().toString();

//        selection= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SPINNER_SELECTION,null);
//
//        if(selection=="popular"){
//            spinner.setSelection(0);
//        }
//        else if(selection=="top_rated") {
//            spinner.setSelection(1);
//        }

        //restoreLayoutManagerPosition();
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

//            getPrefernce(itemselected);
//        }
//        else{
        //spinner.onRestoreInstanceState(itemSelect);
         //if(itemSelect==null)

        itemselected = parent.getItemAtPosition(position).toString();
            getPrefernce(itemselected);

//        else if(itemSelect=="top_rated")
////        if(itemSelect!=null){
//        {itemselected="top_rated";}

       // getPrefernce(itemselected);


        //getPrefernce(itemselected);
       // }
       // getPrefernce(itemselected);
        //itemselected=null;
       //spinner.getItemAtPosition(position);
//restoreLayoutManagerPosition();



        //String item =spinner.getItemAtPosition(position).toString();
//         if(position==1){
//           item ="top_rated";
//        }
//        else if(position==0){
//            item="popular";
//        }
//        spinner.setSelection(position);

//        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        editor = preferences.edit();
//        editor.putString(SPINNER_SELECTION,item);
//        editor.apply();
//        MovieTask movieTask = new MovieTask(getActivity(),this);
//        movieTask.execute();
//        preferences.registerOnSharedPreferenceChangeListener(this);
        //restoreLayoutManagerPosition();

        //editor.clear();
        //item=newSpinnerPostion;
    }

private void getPrefernce(String s){
  SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    editor = preferences.edit();
    editor.putString(SPINNER_SELECTION,s);
    editor.apply();
    MovieTask movieTask = new MovieTask(getActivity(),this);
    movieTask.execute();
    //preferences.registerOnSharedPreferenceChangeListener(this);
}
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


//interface

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

            //item=newSpinnerPostion;
            // preferences.getString(newSpinnerPostion,item);
//            if(preferences!=null){
//                item=newSpinnerPostion;
//            }
        // spinner.getSelectedItem();
        }
//        if(PreferenceManager.getDefaultSharedPreferences(getActivity()).edit() !=null){
//            getPrefernce(itemselected);
//
//        }
//        if(itemSelect!=null){
//           // Parcelable ITEM=itemSelect;
//            spinner.onRestoreInstanceState();
//            getPrefernce(itemSelect);
//        }
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
//        if(key.equals(SPINNER_SELECTION))
//      preferences.getString(key,);
//    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
////PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
//    }
}//fragment

