package com.example.usama.popularmovies.utils;


import android.util.Log;

import com.example.usama.popularmovies.contracts.MovieDbApi;
import com.example.usama.popularmovies.model.Movie;
import com.example.usama.popularmovies.model.Review;
import com.example.usama.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.POSTER_BASE_URL;
import static com.example.usama.popularmovies.contracts.MovieDbApi.MovieAPiConstants.POSTER_SIZE;

public class JsonHelper {

    public static ArrayList<Movie> json2Movies(String url) {

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(url);
            JSONArray moviesJsonArray = rootObject.getJSONArray("results");
            for (int i = 0; i <= moviesJsonArray.length(); i++) {
                JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);
                String movieId = movieJsonObject.getString("id");
                String posterPath = POSTER_BASE_URL + POSTER_SIZE + movieJsonObject.getString("poster_path");
                String movieTitle = movieJsonObject.getString("title");
                String movieReleaseDate = movieJsonObject.getString("release_date");
                String movieVoteAverage = movieJsonObject.getString("vote_average");
                String plotSynopsis = movieJsonObject.getString("overview");
                Movie movie = new Movie(movieId, movieTitle, movieReleaseDate,
                        posterPath, movieVoteAverage, plotSynopsis);
                movies.add(movie);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static ArrayList<Trailer> json2Trailers(String json) {
        ArrayList<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject rootTrailerJsonObject = new JSONObject(json);
            JSONArray resultsArray = rootTrailerJsonObject.getJSONArray("results");
            for (int i = 0; i <= resultsArray.length(); i++) {
                JSONObject trailerObject = resultsArray.getJSONObject(i);
                String trailerName = trailerObject.getString("name");
                String trailerKey = "https://www.youtube.com/watch?v=" + trailerObject.getString("key");
                Trailer trailer = new Trailer(trailerName, trailerKey);
                trailers.add(trailer);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<Review> json2Reviews(String json) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject reviewsRootJsonObject = new JSONObject(json);
            JSONArray reviewsJsonArray = reviewsRootJsonObject.getJSONArray("results");
            for (int i = 0; i <= reviewsJsonArray.length(); i++) {
                JSONObject reviewJsonObject = reviewsJsonArray.getJSONObject(i);
                String reviewAuthor = reviewJsonObject.getString("author");
                String content = reviewJsonObject.getString("content");
                Review review = new Review(reviewAuthor, content);
                reviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }


}
