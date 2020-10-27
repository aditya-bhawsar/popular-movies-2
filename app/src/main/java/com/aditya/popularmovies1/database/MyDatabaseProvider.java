package com.aditya.popularmovies1.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyDatabaseProvider extends ContentProvider {

    MyDbHelper mDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    public static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES+ "/#",MOVIES_WITH_ID);

        return uriMatcher;
    }

    public MyDatabaseProvider() {}

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);

        final SQLiteDatabase db= mDbHelper.getWritableDatabase();

        int returnInt;

        switch (match){
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "movie_id=?";
                String[] mSelectionArgs =new String[]{id};

                returnInt = db.delete(MoviesContract.MovieEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);

                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        getContext().getContentResolver().notifyChange(uri , null);

        return returnInt;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES:

                break;
            case MOVIES_WITH_ID:

                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);

        final SQLiteDatabase db= mDbHelper.getWritableDatabase();

        Uri returnUri;

        switch (match){
            case MOVIES:
                long id = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null,values);
                returnUri = ContentUris.withAppendedId(MoviesContract.MovieEntry.CONTENT_URI,id);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MyDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);

        final SQLiteDatabase db= mDbHelper.getReadableDatabase();

        Cursor retCursor;

        switch (match){
            case MOVIES:
                retCursor = db.query(MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "movie_id=?";
                String[] mSelectionArgs =new String[]{id};
                retCursor = db.query(MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);

        final SQLiteDatabase db= mDbHelper.getWritableDatabase();

        switch (match){
            case MOVIES_WITH_ID:
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}