package com.example.android.topmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Noga on 10/30/2017.
 */

public class DetailsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<?> details;
    DetailsAdapter(Context context,ArrayList<?> details) {
        this.context=context;
        this.details=details;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
                return new TrailerViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
                return new ReviewViewHolder(view);

    }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                TrailerViewHolder vh1 = (TrailerViewHolder) holder;
                configureTrailerHolder(vh1, position);
                break;
            case 2:
                ReviewViewHolder vh2 = (ReviewViewHolder) holder;
                configureReviewHolder(vh2,position);
                break;

        }
    }

    private void configureTrailerHolder(TrailerViewHolder vh1, int position) {
        ArrayList<TrailerItem> details=new ArrayList<>();
        TrailerItem trailer=details.get(position);
        Picasso.with(context).load(trailer.getTrailerImage()).into(vh1.TrailerImage);


    }

    private void configureReviewHolder(ReviewViewHolder vh2,int position) {
        ArrayList<ReviewItem> details=new ArrayList<>();
        ReviewItem review=details.get(position);
        vh2.AuthorText.setText(review.getAuthor());
       vh2.ContentText.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(null==details)return 0;
        return details.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position % 2 == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView TrailerImage;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            TrailerImage=itemView.findViewById(R.id.trailerId);
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView AuthorText;
        TextView ContentText;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            AuthorText=itemView.findViewById(R.id.authorId);
            ContentText=itemView.findViewById(R.id.contentId);
        }
    }

}
