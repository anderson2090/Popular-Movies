package com.example.usama.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.usama.popularmovies.model.Movie;

public class MyDBHandler extends SQLiteOpenHelper {

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "movies.db", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE movies (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , movieid TEXT NOT NULL, moviename TEXT NOT NULL, userrating TEXT NOT NULL, releasedate TEXT NOT NULL, overview TEXT NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS movies");
        onCreate(sqLiteDatabase);
    }

    public void addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put("movieid", movie.getMovieId());
        values.put("moviename", movie.getMovieTitle());
        values.put("userrating", movie.getMovieVoteAverage());
        values.put("releasedate", movie.getMovieReleaseDate());
        values.put("overview", movie.getPlotSynopsis());

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert("movies", null, values);
        database.close();
    }

    public void deleteMovie(Movie movie) {
        String query = "DELETE FROM movies WHERE movieid = " + movie.getMovieId();
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
        database.close();
    }


    public boolean checkIfRowExists(Movie movie) {

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * from movies where movieid = " + movie.getMovieId();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }
}
