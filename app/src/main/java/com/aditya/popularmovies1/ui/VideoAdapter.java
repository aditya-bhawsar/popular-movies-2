package com.aditya.popularmovies1.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aditya.popularmovies1.R;
import com.aditya.popularmovies1.models.Videos;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    public interface ClickListener{ void ClickedItemVideos(String s);}

    ClickListener listener;
    List<Videos> list;

    public VideoAdapter(List<Videos> list, ClickListener listener) {
        this.list =list;
        this.listener =listener;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context ctx = viewGroup.getContext();
        View v= LayoutInflater.from(ctx).inflate(R.layout.video_item,viewGroup,false);
        return new VideoHolder(v);
    }

    @Override public void onBindViewHolder(@NonNull VideoHolder videoHolder, int i) { videoHolder.bind(i); }

    public void refresh(List<Videos> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override public int getItemCount() { return list.size(); }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView TvName,TvType;
        LinearLayout VideoLay;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            TvName = itemView.findViewById(R.id.tv_name);
            TvType = itemView.findViewById(R.id.tv_type);
            VideoLay = itemView.findViewById(R.id.lay_video);
        }

        void bind(int i){
            TvType.setText(list.get(i).getType());
            TvName.setText(list.get(i).getName());
            VideoLay.setOnClickListener(this);
        }

        @Override public void onClick(View v) { listener.ClickedItemVideos(list.get(getAdapterPosition()).getKey());}
    }
}