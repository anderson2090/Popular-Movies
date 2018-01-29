package com.example.usama.popularmovies.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.usama.popularmovies.model.Movie;

import java.util.ArrayList;

public class DBAdapter {

    private static final String DB_NAME = "favouritemovies.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_MOVIES = "table_movies";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MOVIE_ID = "movie_id";
    private static final String COLUMN_MOVIE_NAME = "movie_name";
    private static final String COLUMN_USER_RATING = "user_rating";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_OVERVIEW = "overview";
    private static final String COLUMN_LOCAL_POSTER_PATH = "local_poster_path";

    private static String CREATE_TABLE_QUERY =
            "CREATE TABLE" + TABLE_MOVIES + "(" + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                    + COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                    + COLUMN_USER_RATING + " TEXT NOT NULL, "
                    + COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                    + COLUMN_OVERVIEW + " TEXT NOT NULL, "
                    + COLUMN_LOCAL_POSTER_PATH + " TEXT NOT NULL)";

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static DBAdapter dbAdapterInstance;

    private DBAdapter(Context context) {
        this.context = context;
        sqLiteDatabase = new DBHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }


    public static DBAdapter getDbAdapterInstance(Context context) {
        if (dbAdapterInstance == null) {
            dbAdapterInstance = new DBAdapter(context);
        }
        return dbAdapterInstance;
    }

    public void addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(COLUMN_MOVIE_NAME, movie.getMovieTitle());
        values.put(COLUMN_USER_RATING, movie.getMovieVoteAverage());
        values.put(COLUMN_RELEASE_DATE, movie.getMovieReleaseDate());
        values.put(COLUMN_OVERVIEW, movie.getPlotSynopsis());
        values.put(COLUMN_LOCAL_POSTER_PATH, movie.getMoviePosterPath());

        //SQLiteDatabase database = this.getWritableDatabase();
        sqLiteDatabase.insert(TABLE_MOVIES, null, values);
        sqLiteDatabase.close();
    }

    public void deleteMovie(Movie movie) {
        //String query = "DELETE FROM movies WHERE movieid = " + movie.getMovieId();
        // sqLiteDatabase.execSQL(query);
        sqLiteDatabase.delete(TABLE_MOVIES,
                COLUMN_MOVIE_ID + " = " + movie.getMovieId(),
                null);
        sqLiteDatabase.close();
    }

    public boolean checkIfRowExists(Movie movie) {

        //SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * from " + TABLE_MOVIES + " where " + COLUMN_MOVIE_ID + " = " + movie.getMovieId();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
        cursor.close();
        sqLiteDatabase.close();
        return true;

    }

    public ArrayList<Movie> getFavouriteMovies() {
        ArrayList<Movie> movies = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_MOVIES, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String movieId = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_ID));
                String movieName = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME));
                String userRating = cursor.getString(cursor.getColumnIndex(COLUMN_USER_RATING));
                String releaseDate = cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE));
                String overview = cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW));
                String posterPath = cursor.getString(cursor.getColumnIndex(COLUMN_LOCAL_POSTER_PATH));

                Movie movie = new Movie(movieId, movieName, releaseDate, posterPath, userRating, overview);

                movies.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        return movies;
    }


    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
