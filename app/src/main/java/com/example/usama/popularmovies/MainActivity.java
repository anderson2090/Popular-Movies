package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.usama.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;
import com.example.usama.popularmovies.utils.MyDBHandler;
import com.pixplicity.easyprefs.library.Prefs;


import java.io.IOException;
import java.util.ArrayList;

import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.POPULAR_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.TOP_RATED_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.page;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MoviesRecyclerViewAdapter moviesAdapter;
    public static String sortBy = null;
    ArrayList<Movie> movies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        MyDBHandler myDBHandler = new MyDBHandler(MainActivity.this, null, null, 1);
        ArrayList<Movie> favouriteMovies = new ArrayList<>();
        if(s.equalsIgnoreCase("Favourite Movies")){
            favouriteMovies = myDBHandler.getFavouriteMovies();

        }
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
                } else if(sortBy.equalsIgnoreCase("Top Rated Movies")){
                    httpResponse = HttpHelper.run(TOP_RATED_MOVIES + page);
                }else if (sortBy.equalsIgnoreCase("Favourite Movies")){
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
