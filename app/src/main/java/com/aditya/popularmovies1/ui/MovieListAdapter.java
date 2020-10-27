package com.aditya.popularmovies1.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aditya.popularmovies1.models.Movie;
import com.aditya.popularmovies1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    List<Movie> list;
    ClickListener listener;

    public interface ClickListener{ void posterCLicked(Movie mov);}

    public MovieListAdapter(List<Movie> list, ClickListener listener){
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context ctx = viewGroup.getContext();
        View v= LayoutInflater.from(ctx).inflate(R.layout.movie_item,viewGroup,false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) { movieViewHolder.bind(i); }

    public void refresh(List<Movie> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivPoster;
        MovieViewHolder(View itemView){
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
        }
        void bind(int i){
            Picasso.get().load(list.get(i).getPoster()).into(ivPoster);
            ivPoster.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listener.posterCLicked(list.get(getAdapterPosition()));
        }
    }
}