package com.aditya.popularmovies1.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aditya.popularmovies1.database.MovieDao;
import com.aditya.popularmovies1.database.MovieDatabase;
import com.aditya.popularmovies1.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    MovieDao movieDao;

    MutableLiveData<List<Movie>> popularMovies= new MutableLiveData<>();
    MutableLiveData<List<Movie>> ratedMovies = new MutableLiveData<>();
    LiveData<List<Movie>> favMovies ;

    @Nullable
    public MediatorLiveData<List<Movie>> mediatorLiveData;

    public TypeOfPref typeOfPref = TypeOfPref.POPULAR;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movieDao = movieDatabase.movieDao();

        favMovies = movieDao.loadAllMovies();

        mediatorLiveData = new MediatorLiveData<>();

        mediatorLiveData.addSource(popularMovies, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(typeOfPref == TypeOfPref.POPULAR ){
                    if(movies==null){mediatorLiveData.postValue(new ArrayList<Movie>());}
                    else mediatorLiveData.postValue(movies);
                }
            }
        });

        mediatorLiveData.addSource(ratedMovies, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(typeOfPref == TypeOfPref.RATED ){
                    if(movies==null){mediatorLiveData.postValue(new ArrayList<Movie>());}
                    else mediatorLiveData.postValue(movies);
                }
            }
        });

        mediatorLiveData.addSource(favMovies, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(typeOfPref == TypeOfPref.FAV ){
                    if(movies==null){mediatorLiveData.postValue(new ArrayList<Movie>());}
                    else mediatorLiveData.postValue(movies);
                }
            }
        });
    }

    public void sortChange(TypeOfPref type){
         switch (type){
             case FAV:
                 typeOfPref = TypeOfPref.FAV;
                 if(favMovies.getValue()==null){mediatorLiveData.postValue(new ArrayList<Movie>());}
                 else mediatorLiveData.postValue(favMovies.getValue());
                 break;
             case RATED:
                 typeOfPref = TypeOfPref.RATED;
                 if(ratedMovies==null){mediatorLiveData.postValue(new ArrayList<Movie>());}
                 else mediatorLiveData.postValue(ratedMovies.getValue());
                 break;
             case POPULAR:
                 typeOfPref = TypeOfPref.POPULAR;
                 if(popularMovies==null){mediatorLiveData.postValue(new ArrayList<Movie>());}
                 else mediatorLiveData.postValue(popularMovies.getValue());
                 break;
         }
    }

    public void setPopularMovies(List<Movie> movies){
        popularMovies.postValue(movies);
    }

    public void setRatedMovies(List<Movie> movies){
        ratedMovies.postValue(movies);
    }
}
