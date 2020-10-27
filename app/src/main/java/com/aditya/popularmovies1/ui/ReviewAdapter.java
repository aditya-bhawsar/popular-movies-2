package com.aditya.popularmovies1.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.popularmovies1.R;
import com.aditya.popularmovies1.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    List<Review> reviewList;
    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context ctx = viewGroup.getContext();
        View v= LayoutInflater.from(ctx).inflate(R.layout.review_item,viewGroup,false);
        return new ReviewHolder(v);
    }

    @Override public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) { reviewHolder.bind(i); }

    @Override public int getItemCount() { return reviewList.size(); }

    public void refresh(List<Review> list){
        reviewList = list;
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView TvAuthor,TvReview;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            TvAuthor = itemView.findViewById(R.id.tv_author);
            TvReview = itemView.findViewById(R.id.tv_content);
        }
        void bind(int i){
            TvAuthor.setText(reviewList.get(getAdapterPosition()).getAuthor());
            TvReview.setText(reviewList.get(getAdapterPosition()).getContent());
        }
    }
}