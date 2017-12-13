package com.example.tara.filmapp.data;

import org.json.JSONObject;

/**
 * Created by Tara on 12/12/2017.
 */

public class Film implements JSONPopulator{
    private String title;
    private String poster;
    private String director;
    private String plot;
    private int imdbVotes;
    private int imdbRating;

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public int getImdbVotes() {
        return imdbVotes;
    }

    public int getImdbRating() {
        return imdbRating;
    }

    @Override
    public void populate(JSONObject data) {
        title = data.optString("Title");
        poster = data.optString("Poster");
        director = data.optString("Director");
        plot = data.optString("Plot");
        imdbVotes = data.optInt("imdbVotes");
        imdbRating = data.optInt("imdbRating");
    }
}
