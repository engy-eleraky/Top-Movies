package com.example.android.topmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailActivityFragment extends Fragment implements DetailsTask.returnListener {
    RecyclerView recyclerViewTrailers;
    RecyclerView reclerViewReviews;
    DetailsAdapter adapter;
    public DetailActivityFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        MovieItem movie= (MovieItem) intent.getSerializableExtra(MainActivityFragment.RESULT_KEY);

        recyclerViewTrailers =rootView.findViewById(R.id.trailerRecyclerView);
        reclerViewReviews=rootView.findViewById(R.id.reviewRecyclerView);

        RecyclerView.LayoutManager trailerLayout = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager reviewLayout = new LinearLayoutManager(getActivity());
        recyclerViewTrailers.setLayoutManager(trailerLayout);
        reclerViewReviews.setLayoutManager(reviewLayout);

        adapter = new DetailsAdapter(getActivity(), null);
        recyclerViewTrailers.setAdapter(adapter);
        reclerViewReviews.setAdapter(adapter);

        TextView TextTitle =  rootView.findViewById(R.id.textTitle);
        TextView TextRate =  rootView.findViewById(R.id.textRate);
        TextView TextReleaseDate =  rootView.findViewById(R.id.textRelease);
        TextView TextOverView =  rootView.findViewById(R.id.textOverView);
        ImageView ImagePoster= rootView.findViewById(R.id.imageView);

        new DetailsTask(getActivity(),this,movie).execute(movie.getId());
        //what to do next???????
        //case 0,,1 view ?????
        movie.getTrailers();
        movie.getReviews();
        //get trailers and attach it to adapter in listener
        //get reviews and attach it to adapter in listener
        TextTitle.setText(movie.getTitle());
        TextReleaseDate.setText(movie.getReleaseDate());
        String rate=String.valueOf(movie.getVoteAverage());
        TextRate.setText(rate);
        TextOverView.setText(movie.getOverView());
        Picasso.with(getContext()).load(movie.getPoster()).into(ImagePoster);
        return rootView;
    }


    @Override
    public void onItemReturned(ArrayList<MovieItem> result) {
        adapter=new DetailsAdapter(getActivity(),result);
        recyclerViewTrailers.setAdapter(adapter);
        reclerViewReviews.setAdapter(adapter);
//how to know the position for trailers or reviews?????

    }
}
