package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usama.popularmovies.contracts.MovieDbApi;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.model.Trailer;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {
    public static Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView moviePosterImageView = (ImageView) findViewById(R.id.detailedMoviePosterImageView);
        TextView movieTitleTextView = (TextView) findViewById(R.id.detailedMovieTitleTextView);
        TextView movieReleaseDateTextView = (TextView) findViewById(R.id.detailedReleaseDateTextView);
        TextView movieOverviewTextView = (TextView) findViewById(R.id.detailedMovieOverviewTextView);
        TextView movieVoteAverageTextView = (TextView) findViewById(R.id.detailedVoteAverageTextView);

        currentMovie = getIntent().getParcelableExtra("currentMovie");
        Picasso.with(this).load(currentMovie.getMoviePosterPath()).resize(320, 300)
                .into(moviePosterImageView);
        movieTitleTextView.setText(currentMovie.getMovieTitle());
        movieReleaseDateTextView.setText(currentMovie.getMovieReleaseDate());
        movieOverviewTextView.setText(currentMovie.getPlotSynopsis());
        movieVoteAverageTextView.setText(currentMovie.getMovieVoteAverage() + "/10");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<String>() {
            @Override
            public Loader<String> onCreateLoader(int i, Bundle bundle) {
                return new TrailerLoader(getApplicationContext());
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String s) {
                ArrayList<Trailer> trailers = JsonHelper.json2Trailers(s);
                LinearLayout traielrsVerticalLinearLayout = (LinearLayout) findViewById(R.id.trailersVerticalLinearLayout);

                for (final Trailer trailer : trailers) {
                    LinearLayout trailerLinearLayout = new LinearLayout(DetailedActivity.this);
                    trailerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView trailerNameTextView = new TextView(DetailedActivity.this);
                    trailerNameTextView.setText(trailer.getName());
                    trailerLinearLayout.addView(trailerNameTextView);
                    traielrsVerticalLinearLayout.addView(trailerLinearLayout);
                    trailerNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getKey())));
                        }
                    });
                }
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {

            }
        }).forceLoad();
    }

    private static class TrailerLoader extends AsyncTaskLoader<String> {

        public TrailerLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            String trailersJsonResponse = null;

            String url = "https://api.themoviedb.org/3/movie/" + currentMovie.getMovieId() + "/videos?api_key=690c58456569e376868b792be438e659&language=en-US";
            try {
                trailersJsonResponse = HttpHelper.run(url);
                Log.i("dasd", "dasd");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailersJsonResponse;
        }
    }

}
