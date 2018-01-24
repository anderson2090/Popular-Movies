package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.model.Review;
import com.example.usama.popularmovies.model.Trailer;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;
import com.squareup.picasso.Picasso;
import com.uniquestudio.library.CircleCheckBox;

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
        final TextView markAsFavouriteTextView  = (TextView) findViewById(R.id.markAsFavouriteTextView);
        CircleCheckBox toggleFavouriteCheckBox = (CircleCheckBox) findViewById(R.id.toggleFavouriteCheckBox);
        toggleFavouriteCheckBox.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if(!isChecked){
                    markAsFavouriteTextView.setText(R.string.toggle_button_mark);
                }else {
                    markAsFavouriteTextView.setText(R.string.toggle_button_unmark);
                }
            }
        });
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
                getLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int i, Bundle bundle) {
                        return new ReviewLoader(DetailedActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String s) {

                        LinearLayout reviewsLinearLayout = (LinearLayout) findViewById(R.id.reviewsLinearLayout);
                        ArrayList<Review> reviews = JsonHelper.json2Reviews(s);
                        for (Review review : reviews) {
                            LinearLayout reviewLinearLayout = new LinearLayout(DetailedActivity.this);
                            reviewLinearLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView reviewContentTextView = new TextView(DetailedActivity.this);
                            reviewContentTextView.setText(review.getContent());

                            TextView reviewAuthorTextView = new TextView(DetailedActivity.this);


                            reviewAuthorTextView.setText(review.getAuthor());
                            reviewAuthorTextView.setTypeface(null, Typeface.BOLD);

                            reviewLinearLayout.addView(reviewContentTextView);
                            reviewLinearLayout.addView(reviewAuthorTextView);

                            reviewsLinearLayout.addView(reviewLinearLayout);

                        }

                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {

                    }
                }).forceLoad();
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

            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailersJsonResponse;
        }
    }

    public static class ReviewLoader extends AsyncTaskLoader<String> {

        public ReviewLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            String reviewsJsonResponse = null;
            String url = "https://api.themoviedb.org/3/movie/" + currentMovie.getMovieId() + "/reviews?api_key=690c58456569e376868b792be438e659&language=en-US&page=1";
            try {
                reviewsJsonResponse = HttpHelper.run(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewsJsonResponse;
        }
    }

}
