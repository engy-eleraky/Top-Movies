package com.example.android.topmovies;

import java.io.Serializable;

/**
 * Created by Noga on 10/28/2017.
 */

public class TrailerItem implements Serializable {
    private String url;
    private String name;
    private String trailerImage;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrailerImage() {
        return trailerImage;
    }

    public void setTrailerImage(String trailerImage) {
        this.trailerImage = trailerImage;
    }
}
