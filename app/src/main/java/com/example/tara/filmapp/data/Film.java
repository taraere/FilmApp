package com.example.tara.filmapp.data;

import org.json.JSONObject;

/**
 * Created by Tara on 12/12/2017.
 */

public class Film {
    public String title;
    public String director;
    public String plot;
    public String imdbVotes;
    public String poster;
    public String imdbRating;

    // default constructor
    public Film() {}

    public Film(String title, String director, String plot, String imdbVotes, String poster) {
        this.title      = title;
        this.director   = director;
        this.plot       = plot;
        this.imdbVotes  = imdbVotes;
        this.poster     = poster;
    }
}
