//package com.example.android.topmovies;
//
//import android.content.Intent;
//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//
//public class DetailActivityFragment extends Fragment {
//
//    public DetailActivityFragment() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//       View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//        Intent intent = getActivity().getIntent();
//        MovieItem movie= (MovieItem) intent.getSerializableExtra("myobj");
//        TextView TextTitle = (TextView) rootView.findViewById(R.id.textTitle);
//        TextView TextRate = (TextView) rootView.findViewById(R.id.textRate);
//        TextView TextReleaseDate = (TextView) rootView.findViewById(R.id.textRelease);
//        TextView TextOverView = (TextView) rootView.findViewById(R.id.textOverView);
//        ImageView ImagePoster=(ImageView)rootView.findViewById(R.id.imageView);
//        TextTitle.setText(movie.getTitle());
//        TextReleaseDate.setText(movie.getReleaseDate());
//        String rate=String.valueOf(movie.getVoteAverage());
//        TextRate.setText(rate);
//        TextOverView.setText(movie.getOverView());
//        Picasso.with(getContext()).load(movie.getPoster()).into(ImagePoster);
//
//        return rootView;
//    }
//}
