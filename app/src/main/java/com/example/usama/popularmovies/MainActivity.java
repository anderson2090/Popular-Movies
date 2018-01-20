package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.usama.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.POPULAR_MOVIES;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.page;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MoviesRecyclerViewAdapter moviesAdapter;
    ArrayList<Movie> movies;
    static String URL = POPULAR_MOVIES + page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        layoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(layoutManager);
//        moviesAdapter = new MoviesRecyclerViewAdapter(this);
//
//        recyclerView.setAdapter(moviesAdapter);
        getLoaderManager().initLoader(0, null, this).forceLoad();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (this.getSupportActionBar().getTitle().toString()
                    .equalsIgnoreCase("popular movies")) {
                this.getSupportActionBar().setTitle("Top Rated Movies");
                item.setTitle("Popular Movies");
            }else {
                this.getSupportActionBar().setTitle("Popular Movies");
                item.setTitle("Top Rated Movies");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                httpResponse = HttpHelper.run(URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }
    }


}
