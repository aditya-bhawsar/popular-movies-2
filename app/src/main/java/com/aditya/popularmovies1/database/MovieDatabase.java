package com.aditya.popularmovies1.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.aditya.popularmovies1.models.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase sInstance;
    private static final String DATABASE_NAME = "moviesDb";
    private static final Object LOCK = new Object();

    public static MovieDatabase getInstance(Context ctx){
        if(sInstance==null){ synchronized (LOCK){ sInstance = Room.databaseBuilder(ctx.getApplicationContext(),MovieDatabase.class, MovieDatabase.DATABASE_NAME).allowMainThreadQueries().build(); } }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
