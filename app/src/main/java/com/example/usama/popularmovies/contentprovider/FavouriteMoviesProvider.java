package com.example.usama.popularmovies.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.usama.popularmovies.utils.DBAdapter;

public class FavouriteMoviesProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.usama.popularmovies.contentprovider.FavouriteMoviesProvider";
    private static final String TABLE_MOVIES = "table_movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_MOVIES);

    public static final int MOVIES = 1;
    public static final int MOVIES_ID = 2;


    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_MOVIES, MOVIES);

        URI_MATCHER.addURI(AUTHORITY, TABLE_MOVIES + "/#", +MOVIES_ID);
    }




    private DBAdapter dbAdapter;

    public FavouriteMoviesProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount = -1;
        switch (URI_MATCHER.match(uri)) {
            case MOVIES:
                deleteCount = deleteMovie(selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("delete operation not supported");
        }
        return deleteCount;
    }

    private int deleteMovie(String selection, String[] selectionArgs) {
        return dbAdapter.delete(selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnUri = null;
        switch (URI_MATCHER.match(uri)) {
            case MOVIES:
                returnUri = insertMovie(uri, values);
                break;
            default:
                throw new UnsupportedOperationException("insert operation not supported");
        }

        return returnUri;
    }

    private Uri insertMovie(Uri uri, ContentValues values) {
        long id = dbAdapter.insert(values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + TABLE_MOVIES + "/" + id);
    }

    @Override
    public boolean onCreate() {
        dbAdapter = DBAdapter.getDbAdapterInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (URI_MATCHER.match(uri)) {
            case MOVIES:
                cursor = dbAdapter.getCursorsForAllMovies();
                break;
            default:
                cursor = null;
        }
        //throw new UnsupportedOperationException("Not yet implemented");
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
