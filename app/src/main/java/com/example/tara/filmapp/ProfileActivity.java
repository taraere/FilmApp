package com.example.tara.filmapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.tara.filmapp.data.Film;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth        firebaseAuth;
    private FirebaseDatabase    database;
    private DatabaseReference   watchListDb;

    private TextView    textViewWatchAgain;
    private TextView    textViewHipster;
    private TextView    textViewEmail;
    private Button      buttonLogOut;
    private EditText    editTextSearch;
    private Button      buttonSearch;

    // for Volley/ JSON requests
    private String      filmTitle;
    private RequestQueue queue;
    private String      title;
    private String      director;
    private String      plot;
    private String      imdbVotes;
    private String      poster;
    private String      successfulGet = "no";

    private TextView    textViewTitle;
    private TextView    textViewDirector;
    private TextView    textViewPlot;
    private ImageView   imageViewPoster;
    private TextView    textViewImdbVotes;
    private Button      buttonWatchItAgain;
    private LinearLayout linearLayoutSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userName = user.getEmail().replace("@", "").replace(".", "")
                .replace("mail", "").replace("com", "");

        // layout
        editTextSearch  = (EditText)    findViewById(R.id.editTextSearch);
        buttonSearch    = (Button)      findViewById(R.id.buttonSearch);
        textViewEmail   = (TextView)    findViewById(R.id.textViewUsersEmail);
        buttonLogOut    = (Button)      findViewById(R.id.buttonLogOut);
        textViewWatchAgain = (TextView) findViewById(R.id.textViewWatchAgain);
        textViewHipster = (TextView)    findViewById(R.id.textViewHipster);
        textViewTitle   = (TextView)    findViewById(R.id.textViewTitle);
        textViewDirector= (TextView)    findViewById(R.id.textViewDirector);
        textViewPlot    = (TextView)    findViewById(R.id.textViewPlot);
        imageViewPoster  =(ImageView)   findViewById(R.id.imageViewPoster);
        textViewImdbVotes=(TextView)    findViewById(R.id.textViewImdbVotes);
        buttonWatchItAgain=(Button)     findViewById(R.id.buttonWatchItAgain);
        linearLayoutSearch=(LinearLayout)findViewById(R.id.linearLayoutSearch);

        // clear layout at beginning. Set parameters
        linearLayoutSearch.setVisibility(View.INVISIBLE);
        buttonWatchItAgain.setVisibility(View.INVISIBLE);

        textViewEmail.setText("Welcome, " + userName);
        buttonSearch.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
        textViewWatchAgain.setOnClickListener(this);
        textViewHipster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SharedActivity.class));
            }
        });
        // add to Firebase database
        buttonWatchItAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successfulGet == "yes") {
                    // add info to database
                    database    = FirebaseDatabase.getInstance();
                    watchListDb = database.getReference(userName);

                    // add film object to db
                    Film aFilm = new Film(title, director, plot, imdbVotes, poster);
                    watchListDb.child(title).setValue(aFilm,
                            new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Film added, go have a look",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No film to add",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // logout
        if (v == buttonLogOut) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        // direct to watch again list activity
        if (v == textViewWatchAgain) {
            Toast.makeText(getApplicationContext(), "See the films you want to watch over again",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), WatchActivity.class));
        }
        // direct to searching for other lists
        if (v == textViewHipster) {
            Toast.makeText(getApplicationContext(), "See your most liked and unknown films",
                    Toast.LENGTH_SHORT).show();
        }
        if (v == buttonSearch) {
            // make search suitable for url
            filmTitle = editTextSearch.getText().toString().trim().toLowerCase().replace(" ", "%20");

            // exceptions
            if (filmTitle.length() == 0) {
                Toast.makeText(getApplicationContext(), "To search for films, write a title above",
                        Toast.LENGTH_SHORT).show();
            } else {
                // query json of the API
                queue = Volley.newRequestQueue(this);

                String endpoint = String.format("https://www.omdbapi.com/?t=%s" +
                        "&apikey=f28e2960", filmTitle);
                Log.d(TAG, "onClick: endpoint " + endpoint);

                JsonObjectRequest stringRequest = new JsonObjectRequest(
                        Request.Method.GET, endpoint, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    title = response.getString("Title");
                                    director = response.getString("Director");
                                    plot = response.getString("Plot");
                                    imdbVotes = response.getString("imdbVotes");
                                    poster = response.getString("Poster");

                                    imageGet(poster, imageViewPoster);

                                    textViewTitle.setText("Title: " + title);
                                    textViewDirector.setText("Director: " + director);
                                    textViewPlot.setText("Plot: " + plot);
                                    textViewImdbVotes.setText("IMDB votes: " + imdbVotes);

                                    editTextSearch.setText("");
                                    linearLayoutSearch.setVisibility(View.VISIBLE);
                                    buttonWatchItAgain.setVisibility(View.VISIBLE);

                                    successfulGet = "yes";
                                    Toast.makeText(getApplicationContext(), "Found it! ",
                                            Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    if (e.getMessage() == "Bad URL N/A") {
                                        imageViewPoster.setVisibility(View.INVISIBLE);
                                        String poster = "http://s2.quickmeme.com/img/c4/c41f23766" +
                                                "af0c558fcef4e22772b0ef2ce68c0e7294b6926d857349ec" +
                                                "7ebe8d1.jpg";
                                        imageGet(poster, imageViewPoster);
                                    }
                                    Toast.makeText(getApplicationContext(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onErrorResponse: " + e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        successfulGet = "no";
                    }
                });

                // add request to RequestQueue
                queue.add(stringRequest);
            }
        }
    }

    // Rendering image function
    public void imageGet(String img, final ImageView imgV) {
        ImageRequest imageRequest = new ImageRequest(img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imgV.setImageBitmap(bitmap);
            }
        }, 0, 0, null, Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    }
                }
        );
        queue.add(imageRequest);
    }
}
