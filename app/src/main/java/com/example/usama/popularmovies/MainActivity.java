package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.usama.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.POPULAR_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.TOP_RATED_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.page;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MoviesRecyclerViewAdapter moviesAdapter;
    private SharedPreferences preferences;
    public static String sortBy = "Popular Movies";
    ArrayList<Movie> movies;
    //static String url = POPULAR_MOVIES + page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sortBy = preferences.getString("prefSortBy", "Popular Movies");

//        if (sortBy.equalsIgnoreCase("Popular Movies")) {
//            getSupportActionBar().setTitle("Popular Movies Page " + page);
//
//
//        } else if (sortBy.equalsIgnoreCase("Top Rated Movies")) {
//            getSupportActionBar().setTitle("Top Rated Movies Page " + page);
//
//
//        }


        getLoaderManager().initLoader(0, null, this).forceLoad();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        this.finish();
//        this.startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();

        // sortBy = preferences.getString("pref_sort", "Popular Movies");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                    reloadActivity();
                }
                // Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(this, SettingActivity.class));

                return true;
            case R.id.top_rated_menu_item:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                    sortBy = "Top Rated Movies";
                    reloadActivity();
                }
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        moviesAdapter = new MoviesRecyclerViewAdapter(this);

        recyclerView.setAdapter(moviesAdapter);
        movies = JsonHelper.json2Movies(s);
        moviesAdapter.setMovies(movies);


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
                } else {
                    httpResponse = HttpHelper.run(TOP_RATED_MOVIES + page);
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
