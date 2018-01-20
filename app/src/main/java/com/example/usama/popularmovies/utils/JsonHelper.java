package com.example.usama.popularmovies.utils;


import android.util.Log;

import com.example.usama.popularmovies.contracts.MovieDbApi;
import com.example.usama.popularmovies.model.Movie;

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
}
