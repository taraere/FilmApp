package com.example.tara.filmapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.tara.filmapp.data.Film;
import com.example.tara.filmapp.data.FilmDatabase;

public class WatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        ListView        listViewWatchAgain = (ListView) findViewById(R.id.listViewWatchAgain);
        FilmDatabase    filmDatabase = FilmDatabase.getInstance(this);

        final Cursor    data = filmDatabase.getData();
        FilmAdapter     filmAdapter = new FilmAdapter(this, data);

        listViewWatchAgain.setAdapter(filmAdapter);
    }
}
