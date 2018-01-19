package com.example.usama.popularmovies.model;


public class Movie {

    private String movieId;
    private String movieTitle;
    private String movieReleaseDate;
    private String moviePosterPath;
    private String movieVoteAverage;
    private String plotSynopsis;

    public Movie() {
    }

    public Movie(String movieId, String moviePosterPath) {
        this.movieId = movieId;
        this.moviePosterPath = moviePosterPath;
    }

    public Movie(String movieId, String movieTitle, String movieReleaseDate,
                 String moviePosterPath, String movieVoteAverage, String plotSynopsis) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.moviePosterPath = moviePosterPath;
        this.movieVoteAverage = movieVoteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }

    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(String movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }
}
