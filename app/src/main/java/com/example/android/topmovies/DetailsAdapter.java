package com.example.android.topmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
