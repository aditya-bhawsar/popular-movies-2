package com.aditya.popularmovies1;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aditya.popularmovies1.database.MoviesContract;
import com.aditya.popularmovies1.models.Movie;
import com.aditya.popularmovies1.network.JsonUtil;
import com.aditya.popularmovies1.network.NetworkUtil;
import com.aditya.popularmovies1.ui.MovieListAdapter;
import com.aditya.popularmovies1.viewModel.MainViewModel;
import com.aditya.popularmovies1.viewModel.TypeOfPref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.ClickListener ,
        SharedPreferences.OnSharedPreferenceChangeListener , LoaderManager.LoaderCallbacks<List<Movie>>{

    MainViewModel mainViewModel;

    List<Movie> movieList, mPopularityList, mTopRatedList;
    MovieListAdapter adapter;
    RecyclerView rvMovie;
    public static final int LOADER_ID = 101;

    TextView mSortedByTv;
    LinearLayout mDataLay, mErrorLay, mLoadingLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataLay = findViewById(R.id.data_lay);
        mErrorLay = findViewById(R.id.error_lay);
        mLoadingLay = findViewById(R.id.loading_lay);
        mSortedByTv = findViewById(R.id.sorted_by_tv);

        rvMovie = findViewById(R.id.rv_movie_list);

        int numberOfColumns;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){ numberOfColumns = 2; }
        else{ numberOfColumns = 3; }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        adapter = new MovieListAdapter(movieList, this);

        rvMovie.setLayoutManager(gridLayoutManager);
        rvMovie.setAdapter(adapter);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.mediatorLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(movies!=null && !movies.isEmpty()){
                    showData(movies);
                    System.out.println("2");
                }
                else{
                    System.out.println("1");
                    if(sharedPreferences.getString(getString(R.string.sortByKey),getString(R.string.popularity_value)).equals(getString(R.string.favorite_value))){
                        System.out.println("11111");
                        showNetworkError(false);
                    }else {
                        showNetworkError(true);
                        System.out.println("122222222");
                    }
                }
            }
        });

        System.out.println("Yaha chuda");
        loadData(sharedPreferences.getString(getString(R.string.sortByKey),getString(R.string.popularity_value)));
        Log.e("Data", sharedPreferences.getString(getString(R.string.sortByKey),getString(R.string.popularity_value)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    public void loadData(String sortBy){
        System.out.println("happened");

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.sortByKey),sortBy);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(LOADER_ID,bundle,this);
    }

    @Override
    public void posterCLicked(Movie mov) {
        Intent i =new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra("movie", mov);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sortByKey))){
            System.out.println("Yaha chuda 2");
            loadData(sharedPreferences.getString(getString(R.string.sortByKey),getString(R.string.popularity_value)));
        }
    }

    public void showLoading(){
        mErrorLay.setVisibility(View.GONE);
        mDataLay.setVisibility(View.GONE);
        mLoadingLay.setVisibility(View.VISIBLE);
    }

    public void showData(List<Movie> list){
        movieList = list;

        adapter.refresh(movieList);

        mErrorLay.setVisibility(View.GONE);
        mDataLay.setVisibility(View.VISIBLE);
        mLoadingLay.setVisibility(View.GONE);
    }

    public void showNetworkError(boolean network){
        mErrorLay.setVisibility(View.VISIBLE);
        mDataLay.setVisibility(View.GONE);
        mLoadingLay.setVisibility(View.GONE);
        ImageView ivError = findViewById(R.id.error_img);
        TextView tvError = findViewById(R.id.error_tv);
        if(network){
            ivError.setImageResource(R.drawable.ic_signal);
            tvError.setText(R.string.network_error);
        }
        else {
            ivError.setImageResource(R.drawable.ic_star);
            tvError.setText(R.string.database_error);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_settings){
            Intent i= new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            @Override
            public void deliverResult(@Nullable List<Movie> data) {
                super.deliverResult(data);
                if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.popularity_value))){
                    mPopularityList = data;
                    mSortedByTv.setText(getString(R.string.popularity));
                    System.out.println("loaded");
                    mainViewModel.setPopularMovies(data);
                    mainViewModel.sortChange(TypeOfPref.POPULAR);
                    /*if(data==null){ showNetworkError(true); }
                    else{ showData(data); }*/
                }
                else if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.top_rated_value))){
                    mTopRatedList = data;
                    mSortedByTv.setText(getString(R.string.top_rated));
                    System.out.println("loaded");
                    mainViewModel.setRatedMovies(data);
                    mainViewModel.sortChange(TypeOfPref.RATED);
                    /*if(data==null){ showNetworkError(true); }
                    else{ showData(data); }*/
                }else {
                    mSortedByTv.setText(getString(R.string.favorite));
                    mainViewModel.sortChange(TypeOfPref.FAV);
                }
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                showLoading();
                if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.popularity_value))){
                    if(mPopularityList!=null){
                        System.out.println("11");
                        deliverResult(mPopularityList);
                        return;
                    }
                }
                else if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.top_rated_value))){
                    if(mTopRatedList!=null){
                        System.out.println("11");
                        deliverResult(mTopRatedList);
                        return;
                    }
                }else if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.favorite_value))){
                    deliverResult(null);
                    return;
                }
                forceLoad();
            }


            @Override
            public List<Movie> loadInBackground() {
                if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.popularity_value))){
                    try {
                        return JsonUtil.parseMovies(NetworkUtil.getResponseFromHttpUrl(NetworkUtil.buildUrl("popular")));
                    }catch (IOException e){
                        e.printStackTrace();
                        return null;
                    }
                }
                else if(args.getString(getString(R.string.sortByKey)).equals(getString(R.string.top_rated_value))){
                    try {
                        return JsonUtil.parseMovies(NetworkUtil.getResponseFromHttpUrl(NetworkUtil.buildUrl("top_rated")));
                    }catch (IOException e){
                        e.printStackTrace();
                        return null;
                    }
                }
                else {return null;}
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) { }

    @Override public void onLoaderReset(Loader<List<Movie>> loader) { }
}