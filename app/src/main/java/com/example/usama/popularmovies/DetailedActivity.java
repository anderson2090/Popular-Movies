package com.example.usama.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.usama.popularmovies.contentprovider.FavouriteMoviesProvider;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.model.Review;
import com.example.usama.popularmovies.model.Trailer;
import com.example.usama.popularmovies.utils.DBAdapter;
import com.example.usama.popularmovies.utils.HttpHelper;
import com.example.usama.popularmovies.utils.JsonHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uniquestudio.library.CircleCheckBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {
    public static Movie currentMovie;
    private DBAdapter dbAdapter;
    ScrollView detailedActivityRootScrollView;

    private ContentResolver contentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        detailedActivityRootScrollView = (ScrollView) findViewById(R.id.detailedActivityScrollView);
        dbAdapter = DBAdapter.getDbAdapterInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contentResolver = getContentResolver();
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
        final TextView markAsFavouriteTextView = (TextView) findViewById(R.id.markAsFavouriteTextView);
        CircleCheckBox toggleFavouriteCheckBox = (CircleCheckBox) findViewById(R.id.toggleFavouriteCheckBox);

        if (dbAdapter.checkIfRowExists(currentMovie)) {
            //Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();
            toggleFavouriteCheckBox.setChecked(true);
            markAsFavouriteTextView.setText(R.string.toggle_button_unmark);

        } else {
            //Toast.makeText(getApplicationContext(), "Flase", Toast.LENGTH_SHORT).show();
            toggleFavouriteCheckBox.setChecked(false);
            markAsFavouriteTextView.setText(R.string.toggle_button_mark);
        }


        toggleFavouriteCheckBox.setListener(new CircleCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (!isChecked) {
                    markAsFavouriteTextView.setText(R.string.toggle_button_mark);
                    //dbAdapter.deleteMovie(currentMovie);
                    String currentMovieId = currentMovie.getMovieId();
                    contentResolver.delete(FavouriteMoviesProvider.CONTENT_URI,
                            DBAdapter.COLUMN_MOVIE_ID + " = ?", new String[]{currentMovieId});


                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("PopularMoviesFavImagesDir", Context.MODE_PRIVATE);
                    File myImageFile = new File(directory, currentMovie.getMovieId() + ".jpeg");
                    if (myImageFile.delete())
                        Log.i("Delete message", "image on the disk deleted successfully!");

                } else {
                    markAsFavouriteTextView.setText(R.string.toggle_button_unmark);

                    //dbAdapter.addMovie(currentMovie);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBAdapter.COLUMN_MOVIE_ID, currentMovie.getMovieId());
                    contentValues.put(DBAdapter.COLUMN_LOCAL_POSTER_PATH, currentMovie.getMoviePosterPath());
                    contentValues.put(DBAdapter.COLUMN_OVERVIEW, currentMovie.getPlotSynopsis());
                    contentValues.put(DBAdapter.COLUMN_RELEASE_DATE, currentMovie.getMovieReleaseDate());
                    contentValues.put(DBAdapter.COLUMN_USER_RATING, currentMovie.getMovieVoteAverage());
                    contentValues.put(DBAdapter.COLUMN_MOVIE_NAME, currentMovie.getMovieTitle());

                    contentResolver.insert(FavouriteMoviesProvider.CONTENT_URI, contentValues);


                    Picasso.with(getApplicationContext()).load(currentMovie.getMoviePosterPath())
                            .into(picassoImageTarget(getApplicationContext(), "PopularMoviesFavImagesDir", currentMovie.getMovieId() + ".jpeg"));
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
                if (trailers.isEmpty()) {
                    TextView trailersTextView = (TextView) findViewById(R.id.trailersTextView);
                    trailersTextView.setText(R.string.no_trailers);
                } else {
                    for (final Trailer trailer : trailers) {
                        LinearLayout trailerLinearLayout = new LinearLayout(DetailedActivity.this);
                        trailerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        TextView trailerNameTextView = new TextView(DetailedActivity.this);
                        trailerNameTextView.setText(trailer.getName());
                        trailerNameTextView.setTextSize(20);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(50, 25, 0, 25);
                        trailerNameTextView.setLayoutParams(params);

                        ImageView playIconImageView = new ImageView(DetailedActivity.this);
                        playIconImageView.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        playIconImageView.setMinimumHeight(100);
                        playIconImageView.setMinimumWidth(100);


                        trailerLinearLayout.addView(playIconImageView);
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
                getLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int i, Bundle bundle) {
                        return new ReviewLoader(DetailedActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String s) {

                        LinearLayout reviewsLinearLayout = (LinearLayout) findViewById(R.id.reviewsLinearLayout);
                        ArrayList<Review> reviews = JsonHelper.json2Reviews(s);
                        if (reviews.isEmpty()) {
                            TextView reviewsTextView = (TextView) findViewById(R.id.reviewsTextView);
                            reviewsTextView.setText(R.string.no_reviews);
                        } else {
                            for (Review review : reviews) {
                                LinearLayout reviewLinearLayout = new LinearLayout(DetailedActivity.this);
                                reviewLinearLayout.setOrientation(LinearLayout.VERTICAL);
                                TextView reviewContentTextView = new TextView(DetailedActivity.this);
                                reviewContentTextView.setText(review.getContent());
                                reviewContentTextView.setTextSize(16);

                                TextView reviewAuthorTextView = new TextView(DetailedActivity.this);


                                reviewAuthorTextView.setText(review.getAuthor());
                                reviewAuthorTextView.setTypeface(null, Typeface.BOLD);

                                reviewLinearLayout.addView(reviewContentTextView);
                                reviewLinearLayout.addView(reviewAuthorTextView);

                                reviewsLinearLayout.addView(reviewLinearLayout);

                            }
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

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {

                                fos.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ detailedActivityRootScrollView.getScrollX(), detailedActivityRootScrollView.getScrollY()});
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            detailedActivityRootScrollView.post(new Runnable() {
                public void run() {
                    detailedActivityRootScrollView.scrollTo(position[0], position[1]);
                }
            });
    }

}
