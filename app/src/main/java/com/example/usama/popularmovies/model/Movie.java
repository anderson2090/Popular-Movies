package com.example.usama.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieId);
        dest.writeString(this.movieTitle);
        dest.writeString(this.movieReleaseDate);
        dest.writeString(this.moviePosterPath);
        dest.writeString(this.movieVoteAverage);
        dest.writeString(this.plotSynopsis);
    }

    protected Movie(Parcel in) {
        this.movieId = in.readString();
        this.movieTitle = in.readString();
        this.movieReleaseDate = in.readString();
        this.moviePosterPath = in.readString();
        this.movieVoteAverage = in.readString();
        this.plotSynopsis = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
