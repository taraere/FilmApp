package com.example.tara.filmapp.service;

import android.os.AsyncTask;

/**
 * Created by Tara on 12/12/2017.
 */

public class OMDbService{
    private OMDbServiceCallback callback;
    private String film;
    private Exception error;

    public OMDbService(OMDbServiceCallback callback) {
        this.callback = callback;
    }

    public String getFilm() {
        return film;
    }

    // inputting service method
    public void refreshOMDb(String title) {
        // input type, progress type, and output type
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                // Java query lang
                String title = String.format("Amadeus");

                String endpoint = String.format("https://www.omdbapi.com/?t=%s&apikey=f28e2960", title);

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(title);
    }
}
