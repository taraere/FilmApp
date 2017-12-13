package com.example.tara.filmapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by Tara on 20/11/2017.
 */

public class FilmDatabase extends SQLiteOpenHelper {

    private static final String TABLE_NAME  =   "films";
    private static final String KEY_ID      =   "_id";
    private static final String COL_1_TITLE =   "title";
    private static final String COL_2_DIRECT=   "director";
    private static final String COL_3_PLOT  =   "plot";
    private static final String COL_4_IMDBV =   "imdbVotes";
    private static final String COL_5_POSTER=   "poster";
    private static final String[] COLUMNS = {KEY_ID, COL_1_TITLE, COL_2_DIRECT,
            COL_3_PLOT, COL_4_IMDBV, COL_5_POSTER};
    private static final String TAG = "FilmDatabase";
    private static FilmDatabase instance;

    public static FilmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new FilmDatabase(context);
        }
        return instance;
    }

    private FilmDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    /**
     * SQL statement to create table
     * called films with columns title and completed
     * is autoincrement necessary?
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FILMS_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_1_TITLE + " TEXT, " +
                COL_2_DIRECT + " TEXT, " +
                COL_3_PLOT + " TEXT, " +
                COL_4_IMDBV + " TEXT, " +
                COL_5_POSTER + " TEXT )";
        db.execSQL(CREATE_FILMS_TABLE);
    }

    /**
     * drop older films table if applicable
     * create fresh table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);

        this.onCreate(db);
    }

    /**
     * return all data from db
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addData(String title, String director,
                           String plot, String imdbVotes, String poster) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1_TITLE,  title);
        contentValues.put(COL_2_DIRECT, director);
        contentValues.put(COL_3_PLOT,   plot);
        contentValues.put(COL_4_IMDBV,  imdbVotes);
        contentValues.put(COL_5_POSTER, poster);

        Log.d(TAG, "addData: Adding " + title + " to " + TABLE_NAME);

        // long var for if data is correctly or incorrectly inserted
        long result = db.insert(TABLE_NAME, null, contentValues);

        // result = -1 if incorrect
        return (result != -1);
    }

    /**
     * Return ID that matches the name passed to it
     */
    public Cursor getItemID(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_NAME +
                " WHERE " + COL_1_TITLE + " = '" + task + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * updated checkboxes
     */
    public void update(int newCheckbox, long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_DIRECT, newCheckbox);
        db.update(TABLE_NAME, contentValues, KEY_ID + " = " + id, null);
        Log.d(TAG, "update: setting checkbox value to " + newCheckbox);
    }

    /**
     * Deletes info from database
     */
    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + KEY_ID + " = '" + id + "'";

        Log.d(TAG, "delete: query: " + query);
        Log.d(TAG, "delete: Deleting " + id + " from database.");
        db.execSQL(query);
    }
}
