package com.example.android.topmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }
    public static class DetailFragment extends Fragment {

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            MovieItem movie= (MovieItem) intent.getSerializableExtra(MainActivityFragment.RESULT_KEY);

            TextView TextTitle =  rootView.findViewById(R.id.textTitle);
            TextView TextRate =  rootView.findViewById(R.id.textRate);
            TextView TextReleaseDate = rootView.findViewById(R.id.textRelease);
            TextView TextOverView =  rootView.findViewById(R.id.textOverView);
            ImageView ImagePoster= rootView.findViewById(R.id.imageView);
            TextTitle.setText(movie.getTitle());
            TextReleaseDate.setText(movie.getReleaseDate());
            String rate=String.valueOf(movie.getVoteAverage());
            TextRate.setText(rate);
            TextOverView.setText(movie.getOverView());
            Picasso.with(getContext()).load(movie.getPoster()).fit().into(ImagePoster);

            return rootView;
        }//on create view
    }//fragment

}//class
