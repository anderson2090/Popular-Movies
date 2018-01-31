package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.usama.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.usama.popularmovies.contentprovider.FavouriteMoviesProvider;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.utils.DBAdapter;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;
import com.pixplicity.easyprefs.library.Prefs;


import java.io.IOException;
import java.util.ArrayList;

import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.POPULAR_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.TOP_RATED_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.page;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {


    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    MoviesRecyclerViewAdapter moviesAdapter;
    public static String sortBy = null;
    ArrayList<Movie> movies;

    private DBAdapter dbAdapter;
    private ContentResolver contentResolver;
    //This variable saves the current scroll state of the recyclerView
    Parcelable listState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        dbAdapter = DBAdapter.getDbAdapterInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        contentResolver = getContentResolver();


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        sortBy = Prefs.getString("sortBy", "Popular Movies");


        getLoaderManager().initLoader(0, null, this).forceLoad();

        FloatingActionButton floatingActionButtonNext = (FloatingActionButton) findViewById(R.id.rightFloatingActionButton);
        floatingActionButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page >= 989) {
                    Toast.makeText(getApplicationContext(), "There are no more pages", Toast.LENGTH_SHORT).show();
                } else {
                    page++;
                    getLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
                }
            }
        });

        FloatingActionButton floatingActionButtonPrevious = (FloatingActionButton) findViewById(R.id.leftFloatingActionButton);
        floatingActionButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page == 1) {
                    Toast.makeText(getApplicationContext(), "This are no more pages", Toast.LENGTH_SHORT).show();
                } else {
                    page--;
                    getLoaderManager().initLoader(0, null, MainActivity.this).forceLoad();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        listState = layoutManager.onSaveInstanceState();
        state.putParcelable("State", listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if (state != null)
            listState = state.getParcelable("State");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.popular_menu_item:
                if (item.isChecked()) {
                    item.setChecked(false);


                } else {
                    item.setChecked(true);

                    sortBy = "Popular Movies";
                    Prefs.putString("sortBy", sortBy);

                }
                getLoaderManager().initLoader(0, null, this).forceLoad();

                return true;
            case R.id.top_rated_menu_item:
                if (item.isChecked()) {
                    item.setChecked(false);

                } else {
                    item.setChecked(true);

                    sortBy = "Top Rated Movies";
                    Prefs.putString("sortBy", sortBy);

                }
                getLoaderManager().initLoader(0, null, this).forceLoad();
                return true;
            case R.id.favourite_movies_item:
                if (item.isChecked()) {
                    item.setChecked(false);

                } else {
                    item.setChecked(true);

                    sortBy = "Favourite Movies";
                    Prefs.putString("sortBy", sortBy);

                }
                getLoaderManager().initLoader(0, null, this).forceLoad();
                //Toast.makeText(getApplicationContext(), "Display favourite movies", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new MovieTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        //MyDBHandler myDBHandler = new MyDBHandler(MainActivity.this, null, null, 1);

        if (s.equalsIgnoreCase("Favourite Movies")) {
            if (dbAdapter.getFavouriteMovies().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Favourite Movies list is empty", Toast.LENGTH_SHORT).show();
            } else {
                //movies = dbAdapter.getFavouriteMovies();
                Cursor cursor = contentResolver.query(FavouriteMoviesProvider.CONTENT_URI,
                        null, null, null, null, null);
                movies = cursor2movies(cursor);
            }

        } else {
            movies = JsonHelper.json2Movies(s);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        moviesAdapter = new MoviesRecyclerViewAdapter(this);


        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setMovies(movies);
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }


    }

    private ArrayList<Movie> cursor2movies(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String movieId = cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_MOVIE_ID));
                String movieName = cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_MOVIE_NAME));
                String userRating = cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_USER_RATING));
                String releaseDate = cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_RELEASE_DATE));
                String overview = cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_OVERVIEW));
                String posterPath = cursor.getString(cursor.getColumnIndex(DBAdapter.COLUMN_LOCAL_POSTER_PATH));

                Movie movie = new Movie(movieId, movieName, releaseDate, posterPath, userRating, overview);

                movies.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
            // sqLiteDatabase.close();
        }
        return movies;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    private static class MovieTaskLoader extends AsyncTaskLoader<String> {


        public MovieTaskLoader(Context context) {
            super(context);

        }

        @Override
        public String loadInBackground() {
            String httpResponse = null;
            try {
                if (sortBy.equalsIgnoreCase("Popular Movies")) {
                    httpResponse = HttpHelper.run(POPULAR_MOVIES + page);
                } else if (sortBy.equalsIgnoreCase("Top Rated Movies")) {
                    httpResponse = HttpHelper.run(TOP_RATED_MOVIES + page);
                } else if (sortBy.equalsIgnoreCase("Favourite Movies")) {
                    httpResponse = "Favourite Movies";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }


    }

    public void reloadActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
        overridePendingTransition(0, 0);
    }


}
