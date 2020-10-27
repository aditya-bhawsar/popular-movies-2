package com.aditya.popularmovies1.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.aditya.popularmovies1.database.MovieDao;
import com.aditya.popularmovies1.database.MovieDatabase;
import com.aditya.popularmovies1.models.Movie;

public class DetailsViewModel extends AndroidViewModel {

    MovieDao movieDao;

    public DetailsViewModel(Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movieDao = movieDatabase.movieDao();
    }

    public boolean isSaved(int id){ return movieDao.loadByID(id) == null; }

    public void insert(Movie movie){ movieDao.insertMovie(movie); }
    public void delete(Movie movie){ movieDao.deleteMovie(movie); }

}
