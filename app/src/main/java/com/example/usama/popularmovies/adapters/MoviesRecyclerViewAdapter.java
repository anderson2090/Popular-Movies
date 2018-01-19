package com.example.usama.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usama.popularmovies.DetailedActivity;
import com.example.usama.popularmovies.R;
import com.example.usama.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> movies = new ArrayList<>();

    public MoviesRecyclerViewAdapter() {
    }

    public MoviesRecyclerViewAdapter(Context context) {
        this.context = context;

    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        //notifyDataSetChanged();
    }


    @Override
    public MoviesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesRecyclerViewAdapter.ViewHolder holder, int position) {
        Movie currentMovie = movies.get(position);
        // holder.moviePosterImageView.setImageResource(R.drawable.download);
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.moviePosterImageView);
        Picasso.with(context).load(currentMovie.getMoviePosterPath()).into(holder.moviePosterImageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView moviePosterImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.moviePosterImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(itemView.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), DetailedActivity.class));
                }
            });
        }
    }


}
