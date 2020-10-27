package com.aditya.popularmovies1.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MoviesContract {

    private MoviesContract() { }

    public static final String AUTHORITY= "com.aditya.popularmovies1";
    public static final String PATH_MOVIES= "movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies_table";
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_NAME = "name";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_Poster = "poster";
        public static final String MOVIE_RATED = "top_rated";
        public static final String MOVIE_POPULARITY = "popularity";
        public static final String MOVIE_Date = "date";
    }

}
