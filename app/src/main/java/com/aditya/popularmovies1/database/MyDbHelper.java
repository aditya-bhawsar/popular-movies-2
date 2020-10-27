package com.aditya.popularmovies1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies_data.db";
    private static final int DATABASE_VERSION = 1;


    public MyDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_DATABASE = "CREATE TABLE "+
                MoviesContract.MovieEntry.TABLE_NAME+ " (" +
                MoviesContract.MovieEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MoviesContract.MovieEntry.MOVIE_ID+" INTEGER NOT NULL,"+
                MoviesContract.MovieEntry.MOVIE_NAME+ " TEXT NOT NULL,"+
                MoviesContract.MovieEntry.MOVIE_OVERVIEW+ " TEXT NOT NULL,"+
                MoviesContract.MovieEntry.MOVIE_POPULARITY+ " TEXT NOT NULL,"+
                MoviesContract.MovieEntry.MOVIE_Poster+ " TEXT NOT NULL,"+
                MoviesContract.MovieEntry.MOVIE_RATED+ " TEXT NOT NULL,"+
                MoviesContract.MovieEntry.MOVIE_Date+ " TEXT NOT NULL"+
                ");";

        db.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}