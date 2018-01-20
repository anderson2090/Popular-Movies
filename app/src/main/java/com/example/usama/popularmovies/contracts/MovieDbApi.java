package com.example.usama.popularmovies.contracts;


public class MovieDbApi {

    private MovieDbApi() {

    }

    public static final class MovieAPiConstants {
        public static int page = 1;
        public static final String KEY = "";
        public static final String POPULAR_MOVIES = "https://api.themoviedb.org/3/movie/popular?api_key=" + KEY + "&language=en-US&page=";
        public static final String TOP_RATED_MOVIES = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + KEY + "&language=en-US&page=";
        public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
        public static final String POSTER_SIZE = "w300";

    }
}
