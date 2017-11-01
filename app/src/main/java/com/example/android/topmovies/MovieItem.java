package com.example.android.topmovies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noga on 9/25/2017.
 */

public class MovieItem implements Serializable {
    private String title;
    private String poster;
    private String overView;
    private double voteAverage;
    private String releaseDate;
    private String id;
    private ArrayList<TrailerItem> trailers = new ArrayList<>();
    private ArrayList<ReviewItem> reviews = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<TrailerItem> getTrailers() {
        return trailers;
    }

    public ArrayList<ReviewItem> getReviews() {
        return reviews;
    }
    public void addTrailers(TrailerItem trailer){

        trailers.add(trailer);
    }
    public void addReviews(ReviewItem review){
        reviews.add(review);
    }

}


