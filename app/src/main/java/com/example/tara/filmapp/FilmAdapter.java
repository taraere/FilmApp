package com.example.tara.filmapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import static android.content.ContentValues.TAG;

/**
 * Created by Tara on 13/12/2017.
 */

public class FilmAdapter extends ResourceCursorAdapter {
    public FilmAdapter(Context context, Cursor c) {
        super(context, R.layout.row_layout, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title     = cursor.getString(cursor.getColumnIndex("title"       ));
        String director  = cursor.getString(cursor.getColumnIndex("director"    ));
        String plot      = cursor.getString(cursor.getColumnIndex("plot"        ));
        String imdbVotes = cursor.getString(cursor.getColumnIndex("imdbVotes"   ));
        String poster    = cursor.getString(cursor.getColumnIndex("poster"      ));

        TextView  textViewTitle      = view.findViewById(R.id.textViewTitleRL);
        TextView  textViewDirector   = view.findViewById(R.id.textViewDirectorRL);
        TextView  textViewPlot       = view.findViewById(R.id.textViewPlotRL);
        TextView  textViewimdbVotes  = view.findViewById(R.id.textViewImdbVotes);
        ImageView imageView          = view.findViewById(R.id.imageViewPosterRL);

        textViewTitle.setText(title);
        textViewDirector.setText(director);
        textViewPlot.setText(plot);
        textViewimdbVotes.setText(imdbVotes);
//        imageGet(poster, imageView);
    }

//    private void imageGet(String poster, final ImageView imageView) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(poster, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//        }, 0, 0, null, Bitmap.Config.ALPHA_8,
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
//                    }
//                }
//        );
//        queue.add(imageRequest);
//    }
}
