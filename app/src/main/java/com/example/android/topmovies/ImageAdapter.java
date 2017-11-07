package com.example.android.topmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Noga on 9/25/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final ImageItemClickListner onClickListner;
    Context context;
    ArrayList<MovieItem> movies;

    ImageAdapter(Context context, ArrayList<MovieItem> movies,ImageItemClickListner listner) {
        this.context = context;
        this.movies = movies;
        onClickListner=listner;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        MovieItem movie = movies.get(position);
        //placeholder
        Uri uri= Uri.parse(movie.getPoster());
        Picasso.with(context).load(
                movie.getPoster()
        ).placeholder(R.drawable.ic1_launcher).into(holder.imageView);
        holder.textView.setText(movie.getTitle());

    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ImageItemClickListner{
        void onImageItemClick(Object movie );
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView =   itemView.findViewById(R.id.posterView);
            textView =   itemView.findViewById(R.id.movieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition= getAdapterPosition();
            MovieItem movie=movies.get(clickedPosition);
            onClickListner.onImageItemClick(movie);

        }

    }//class

}//adapter
