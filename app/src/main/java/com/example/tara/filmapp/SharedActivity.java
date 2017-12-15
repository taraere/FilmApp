package com.example.tara.filmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tara.filmapp.data.Film;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SharedActivity extends AppCompatActivity {

    // layout
    private EditText    editText;
    private ListView    listView;
    private Button      button;

    // Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase    database;
    private DatabaseReference   aDatabase;

    // var
    String otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        // list rendering
        final ArrayList<String>     otherFilms  = new ArrayList<>();
        final ArrayAdapter<String>  adapter     = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, otherFilms);

        // instantiate layout
        editText    = (EditText)    findViewById(R.id.editeTextOthers);
        listView    = (ListView)    findViewById(R.id.listViewOthers);
        button      = (Button)      findViewById(R.id.buttonOthers);

        // Firebase related
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get search query and query Firebase
                otherUser   = editText.getText().toString().trim();
                database    = FirebaseDatabase.getInstance();
                aDatabase   = database.getReference(otherUser);

                listView.setAdapter(adapter);

                aDatabase.addValueEventListener(new ValueEventListener() {
                    /**
                     * method will invoke as soon as data is changed
                     * @param dataSnapshot
                     */
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // reference to each film in list
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        // save each of them
                        for (DataSnapshot child: children) {
                            Film film = child.getValue(Film.class);
                            String title = film.title;
                            otherFilms.add(title);
                            // render it
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
