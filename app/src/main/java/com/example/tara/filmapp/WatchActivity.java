package com.example.tara.filmapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WatchActivity extends AppCompatActivity {

    private static final String TAG = "WatchActivity " ;
    private FirebaseDatabase    database;
    private DatabaseReference   aDatabase;
    private ListView            listViewFilm;
    private Button              buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

//        // find the retained fragment as activity
//        FragmentManager fm = getSupportFragmentManager();
//
//        WatchFragment fragment = new WatchFragment();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragment_container, fragment, "WatchFragment");
//        ft.commit();

        database    = FirebaseDatabase.getInstance();
        aDatabase   = database.getReference();
        // hold collection of films
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, list);

        listViewFilm= (ListView) findViewById(R.id.listViewFilm);
        buttonShare = (Button) findViewById(R.id.buttonShare);

        listViewFilm.setAdapter(adapter);

        aDatabase.child("WatchAgainList").addValueEventListener(new ValueEventListener() {
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
                    list.add(title);
                    // render it
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // share info
                Intent intent = new Intent();
                String msg = "visit my awesome website: " + buildDynamicLink();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("text/plain");
                startActivity(intent);
            }

            private String buildDynamicLink() {
//                String path = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                        .setDynamicLinkDomain("m9guj.app.goo.gl");
                return "https://m9guj.app.goo.gl/?" +
                        "link=" + /*link*/
                        "https://youtube.com/c/Melardev" +
                        "&apn=" + /*package name*/
                        "com.example.tara.filmapp" +
                        "&st=" + /*title social*/
                        "Share+this+app" +
                        "&sd=" +
                        "looking+for+the+films+you+love." +
                        "&utm_source=" + /*source*/
                        "AndroidApp";
            }
        });
    }
}
