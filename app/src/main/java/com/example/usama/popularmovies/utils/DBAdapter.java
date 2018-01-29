package com.example.usama.popularmovies.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
