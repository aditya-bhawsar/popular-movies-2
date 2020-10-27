package com.aditya.popularmovies1.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String key = "Your Key Here";


    public static URL buildUrl(String sortBy) {

        Uri uri = Uri.parse(BASE_URL+sortBy+"?").buildUpon()
                .appendQueryParameter("api_key", key)
                .build();

        URL url = null;

        try { url = new URL(uri.toString()); } catch (MalformedURLException e) { e.printStackTrace(); }

        Log.v("MESSAGE URL", url.toString());

        return url;
    }

    public static URL buildUrlMovieDetails(String id, String sortBy) {

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(sortBy)
                .appendQueryParameter("api_key", key)
                .build();

        URL url = null;

        try { url = new URL(uri.toString()); } catch (MalformedURLException e) { e.printStackTrace(); }

        Log.v("MESSAGE URL", url.toString());

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
