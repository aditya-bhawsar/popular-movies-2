package com.aditya.popularmovies1;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.popularmovies1.database.MoviesContract;
import com.aditya.popularmovies1.models.Movie;
import com.aditya.popularmovies1.models.Review;
import com.aditya.popularmovies1.models.Videos;
import com.aditya.popularmovies1.network.JsonUtil;
import com.aditya.popularmovies1.network.NetworkUtil;
import com.aditya.popularmovies1.ui.ReviewAdapter;
import com.aditya.popularmovies1.ui.VideoAdapter;
import com.aditya.popularmovies1.viewModel.DetailsViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.ClickListener, LoaderManager.LoaderCallbacks<String[]> {

    Movie movie;
    TextView mTitleTv,mUserRatingTv,mDateTv,mPlotTv,mTrailerErrorTv, mReviewErrorTv;
    ImageView mPosterIv;
    RecyclerView mRvVideos,mRvReview;
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    List<Videos> videoList = new ArrayList<>();
    List<Review> reviewList = new ArrayList<>();
    URL videoUrl,reviewUrl;
    Button mFavBtn;
    ProgressBar mVideoPb, mReviewPb;

    DetailsViewModel detailsViewModel;

    private static final int VIDEO_DATA_LOADER = 11;
    private static final int REVIEW_DATA_LOADER = 12;
    private static final String VIDEO_URL_EXTRA = "videoUrl";
    private static final String REVIEW_URL_EXTRA = "reviewUrl";
    private static final String FOR_VIDEO = "forVideo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = getIntent().getParcelableExtra("movie");

        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        videoUrl = NetworkUtil.buildUrlMovieDetails(String.valueOf(movie.getMovie_id()), getResources().getString(R.string.video));
        reviewUrl = NetworkUtil.buildUrlMovieDetails(String.valueOf(movie.getMovie_id()), getResources().getString(R.string.review));

        mPosterIv = findViewById(R.id.iv_poster);
        mUserRatingTv = findViewById(R.id.user_rating_tv);
        mTitleTv = findViewById(R.id.title_tv);
        mDateTv = findViewById(R.id.date_tv);
        mPlotTv = findViewById(R.id.plot_tv);
        mRvReview = findViewById(R.id.rv_reviews);
        mRvVideos = findViewById(R.id.rv_videos);
        mTrailerErrorTv = findViewById(R.id.tv_error_trailer);
        mReviewErrorTv = findViewById(R.id.tv_error_review);
        mVideoPb= findViewById(R.id.pb_videos);
        mReviewPb= findViewById(R.id.pb_review);
        mFavBtn = findViewById(R.id.favorite_btn);

        Picasso.get().load(movie.getPoster()).into(mPosterIv);

        mTitleTv.setText(String.valueOf(movie.getName()));
        mPlotTv.setText(String.valueOf(movie.getOverview()));
        mDateTv.setText(String.valueOf(movie.getDate()));
        mUserRatingTv.setText(String.valueOf(movie.getRated()));

        videoAdapter = new VideoAdapter(videoList ,this);
        reviewAdapter = new ReviewAdapter(reviewList);

        mRvReview.setLayoutManager(new LinearLayoutManager(this));
        mRvVideos.setLayoutManager(new LinearLayoutManager(this));

        mRvReview.setAdapter(reviewAdapter);
        mRvVideos.setAdapter(videoAdapter);

        mTrailerErrorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { getTrailers(); }
        });

        mReviewErrorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { getReviews(); }
        });

        mReviewErrorTv.performClick();
        mTrailerErrorTv.performClick();


        if(!detailsViewModel.isSaved(movie.getMovie_id())){
            mFavBtn.setText(getResources().getString(R.string.remove_as_favourite));
        }

        mFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavBtn.getText().toString().equals(getResources().getString(R.string.mark_as_favourite))){
                    detailsViewModel.insert(movie);
                    Toast.makeText(getBaseContext(),"Marked As Favorite",Toast.LENGTH_SHORT).show();
                    mFavBtn.setText(R.string.remove_as_favourite);
                }
                else{
                    detailsViewModel.delete(movie);
                    Toast.makeText(getBaseContext(),"Removed As Favorite",Toast.LENGTH_SHORT).show();
                    mFavBtn.setText(R.string.mark_as_favourite);
                }
            }
        });
    }

    public void getTrailers(){
        Bundle query = new Bundle();
        query.putString(VIDEO_URL_EXTRA, videoUrl.toString());
        query.putBoolean(FOR_VIDEO, true);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.restartLoader(VIDEO_DATA_LOADER, query, this);
    }

    public void getReviews(){
        Bundle query = new Bundle();
        query.putString(REVIEW_URL_EXTRA, reviewUrl.toString());
        query.putBoolean(FOR_VIDEO, false);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.restartLoader(REVIEW_DATA_LOADER, query, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override public void ClickedItemVideos(String s) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.youtube_url)+s))); }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] mVideoCache;
            String[] mReviewCache;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle==null){return;}
                if(bundle.getBoolean(FOR_VIDEO, false)){
                    if(mReviewCache!=null){
                        deliverResult(mReviewCache);
                        return;
                    }
                    mReviewPb.setVisibility(View.VISIBLE);
                    mRvReview.setVisibility(View.GONE);
                    mReviewErrorTv.setVisibility(View.GONE);
                }
                else{
                    if(mVideoCache!=null){
                        deliverResult(mVideoCache);
                        return;
                    }
                    mVideoPb.setVisibility(View.VISIBLE);
                    mRvVideos.setVisibility(View.GONE);
                    mTrailerErrorTv.setVisibility(View.GONE);
                }
                forceLoad();
            }

            @Override
            public void deliverResult(@Nullable String[] data) {
                super.deliverResult(data);
                if(data[0]==null){
                    if(data[1].equals(VIDEO_URL_EXTRA)){ mVideoCache = data; }
                    else { mReviewCache = data; }
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {

                String[]str = new String[2];
                String url;
                if(bundle.getBoolean(FOR_VIDEO, false)){
                    str[1] = VIDEO_URL_EXTRA;
                    url =bundle.getString(VIDEO_URL_EXTRA,null);
                    if(url==null || url.isEmpty() ){ return null; }
                    else{
                        try {
                            URL reviewUrl = new URL(url);
                            str[0] = NetworkUtil.getResponseFromHttpUrl(reviewUrl);
                            return str;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            return str;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return str;
                        }
                    }
                }
                else{
                    url= bundle.getString(REVIEW_URL_EXTRA,null);
                    str[1] = REVIEW_URL_EXTRA;
                    if(url==null || url.isEmpty() ){ return null; }
                    else{
                        try {
                            URL videoUrl = new URL(url);
                            str[0] = NetworkUtil.getResponseFromHttpUrl(videoUrl);
                            return str;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            return str;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return str;
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] strings) {

        if(strings[1].equals(VIDEO_URL_EXTRA)){
            if(strings[0]!=null){
                videoList = JsonUtil.parseVideos(strings[0]);
                videoAdapter.refresh(videoList);
                mVideoPb.setVisibility(View.GONE);
                mRvVideos.setVisibility(View.VISIBLE);
                mTrailerErrorTv.setVisibility(View.GONE);
            }
            else {
                mVideoPb.setVisibility(View.GONE);
                mRvVideos.setVisibility(View.VISIBLE);
                mTrailerErrorTv.setVisibility(View.GONE);
            }
        }
        else{
            if(strings[0]!=null){
                reviewList = JsonUtil.parseReviews(strings[0]);
                reviewAdapter.refresh(reviewList);
                mReviewPb.setVisibility(View.GONE);
                mRvReview.setVisibility(View.VISIBLE);
                mReviewErrorTv.setVisibility(View.GONE);
            }
            else {
                mReviewPb.setVisibility(View.GONE);
                mRvReview.setVisibility(View.GONE);
                mReviewErrorTv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) { }
}