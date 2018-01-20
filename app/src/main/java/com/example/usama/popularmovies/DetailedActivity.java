package com.example.usama.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usama.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {

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

        Movie currentMovie = getIntent().getParcelableExtra("currentMovie");
        Picasso.with(this).load(currentMovie.getMoviePosterPath()).resize(320, 300).into(moviePosterImageView);
        movieTitleTextView.setText(currentMovie.getMovieTitle());
        movieReleaseDateTextView.setText(currentMovie.getMovieReleaseDate());
        movieOverviewTextView.setText(currentMovie.getPlotSynopsis());
        movieVoteAverageTextView.setText(currentMovie.getMovieVoteAverage()+"/10");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
