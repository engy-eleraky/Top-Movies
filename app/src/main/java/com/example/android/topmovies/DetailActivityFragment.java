package com.example.android.topmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.topmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class DetailActivityFragment extends Fragment implements DetailsTask.returnListener,DetailsAdapter.ImageItemClickListner {
    private static final String SAVED_LAYOUT1_MANAGER = "trailerLayout";
    private static final String SAVED_LAYOUT2_MANAGER = "reviewLayout";
    RecyclerView recyclerViewTrailers;
    RecyclerView reclerViewReviews;
    DetailsAdapter trailerAdapter;
    DetailsAdapter reviewAdapter;
    RecyclerView.LayoutManager trailerLayout;
    RecyclerView.LayoutManager reviewLayout;
    Parcelable layout1;
    Parcelable layout2;
    CheckBox favoritsCheckBox;
    public DetailActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outsate) {
        outsate.putParcelable(SAVED_LAYOUT1_MANAGER, recyclerViewTrailers.getLayoutManager().onSaveInstanceState());
        outsate.putParcelable(SAVED_LAYOUT2_MANAGER, reclerViewReviews.getLayoutManager().onSaveInstanceState());

        super.onSaveInstanceState(outsate);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null  ){
            layout1=savedInstanceState.getParcelable(SAVED_LAYOUT1_MANAGER);
            layout2=savedInstanceState.getParcelable(SAVED_LAYOUT2_MANAGER);
            //rotation??????
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        final MovieItem movie= (MovieItem) intent.getSerializableExtra(MainActivityFragment.RESULT_KEY);


        recyclerViewTrailers =rootView.findViewById(R.id.trailerRecyclerView);
        recyclerViewTrailers.setHasFixedSize(true);

        reclerViewReviews=rootView.findViewById(R.id.reviewRecyclerView);
        reclerViewReviews.setHasFixedSize(true);

        trailerLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        reviewLayout = new LinearLayoutManager(getActivity());

        recyclerViewTrailers.setLayoutManager(trailerLayout);
        reclerViewReviews.setLayoutManager(reviewLayout);

        trailerAdapter = new DetailsAdapter(getActivity(), null,this);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        reviewAdapter=new DetailsAdapter(getActivity(),null,this);
        reclerViewReviews.setAdapter(reviewAdapter);

        //check connect to the internet or not ???????????
        //if connected
        //do task
        new DetailsTask(getActivity(),this,movie).execute(movie.getId());

        TextView TextTitle =  rootView.findViewById(R.id.textTitle);
        TextView TextRate =  rootView.findViewById(R.id.textRate);
        TextView TextReleaseDate =  rootView.findViewById(R.id.textRelease);
        TextView TextOverView =  rootView.findViewById(R.id.textOverView);
        ImageView ImagePoster= rootView.findViewById(R.id.imageView);
        TextTitle.setText(movie.getTitle());
        TextReleaseDate.setText(movie.getReleaseDate());
        String rate=String.valueOf(movie.getVoteAverage());
        TextRate.setText(rate);
        TextOverView.setText(movie.getOverView());
        Picasso.with(getContext()).load(movie.getPoster()).into(ImagePoster);

        favoritsCheckBox=rootView.findViewById(R.id.favCheck);
        //save selection????
        favoritsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //??????????
                if (favoritsCheckBox.isChecked()){
                    addMovie(movie);
                }
                else{
                    //????
                    deleteMovie(movie.getId());
                }


            }
        });

        return rootView;
    }
    @Override
    public void onImageItemClick(TrailerItem trailer) {
        Intent intent = new  Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.google.android.youtube");
        intent.setData(Uri.parse(trailer.getUrl()));

        startActivity(intent);

    }

    @Override
    public void onItemReturned(MovieItem result) {
        trailerAdapter=new DetailsAdapter(getActivity(),result.getTrailers(),this);
        reviewAdapter=new DetailsAdapter(getActivity(),result.getReviews(),this) ;
        recyclerViewTrailers.setAdapter(trailerAdapter);
        reclerViewReviews.setAdapter(reviewAdapter);
        restoreLayoutManagerPosition();

    }

    private void restoreLayoutManagerPosition() {
        if (layout1 != null &layout2 != null) {
            recyclerViewTrailers.getLayoutManager().onRestoreInstanceState(layout1);
            reclerViewReviews.getLayoutManager().onRestoreInstanceState(layout2);

        }

    }


    private void addMovie(MovieItem movie){

        ContentValues contentValue = new ContentValues();
        contentValue.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID,movie.getId());
        contentValue.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValue.put(MoviesContract.MovieEntry.COLUMN_OVER_VIEW, movie.getOverView());
        contentValue.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        //offline?????
        contentValue.put(MoviesContract.MovieEntry.COLUMN_POSTER, movie.getPoster());
        contentValue.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, contentValue);


    }
    private void deleteMovie(String id){
        String[] ID={id};
        getActivity().getContentResolver().delete(
                MoviesContract.MovieEntry.CONTENT_URI,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                ID
        );

    }


}//class
