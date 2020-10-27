package com.aditya.popularmovies1.network;

import android.database.Cursor;
import android.util.Log;

import com.aditya.popularmovies1.models.Movie;
import com.aditya.popularmovies1.models.Review;
import com.aditya.popularmovies1.models.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static List<Movie> parseMovies(String json){
        List<Movie> movieList = new ArrayList<>();

        try {
            Log.e("String" , json);
            JSONObject mainJson = new JSONObject(json);

            JSONArray arr = mainJson.getJSONArray("results");

            movieList.clear();

            for(int i =0; i<arr.length(); i++){

                JSONObject movJson = new JSONObject(arr.get(i).toString());

                int id = movJson.getInt("id");
                double VoteAverage = movJson.getDouble("vote_average");
                double Popularity = movJson.getDouble("popularity");
                String VoteAverageString = Double.valueOf(VoteAverage).toString();
                String PopularityString = Double.valueOf(Popularity).toString();
                String OverView = movJson.getString("overview");
                String Title = movJson.getString("original_title");
                String Poster = "http://image.tmdb.org/t/p/w185/"+movJson.getString("poster_path");
                String Date =movJson.getString("release_date");

                Movie mov = new Movie(id,Title,OverView,Poster,VoteAverageString,PopularityString,Date);

                movieList.add(mov);
            }
        }
        catch (JSONException e){e.printStackTrace();}

        return movieList;
    }

    public static List<Videos> parseVideos(String json) {
        List<Videos> vidList  = new ArrayList<>();

        try {
            JSONObject mainJson = new JSONObject(json);

            JSONArray arr = mainJson.getJSONArray("results");

            vidList.clear();

            for(int i =0; i<arr.length(); i++){
                JSONObject vidJson = new JSONObject(arr.get(i).toString());

                String key = vidJson.getString("key");
                String name = vidJson.getString("name");
                String type = vidJson.getString("type");

                Videos vid = new Videos(name,type,key);

                vidList.add(vid);
            }

        }
        catch (JSONException e){e.printStackTrace();}

        return vidList;
    }


    public static List<Review> parseReviews(String json) {
        List<Review> revList  = new ArrayList<>();

        try {
            JSONObject mainJson = new JSONObject(json);

            JSONArray arr = mainJson.getJSONArray("results");

            revList.clear();

            for(int i =0; i<arr.length(); i++){
                JSONObject revJson = new JSONObject(arr.get(i).toString());

                String author= revJson.getString("author");
                String content= revJson.getString("content");

                Review rev = new Review(author, content);

                revList.add(rev);
            }
        }
        catch (JSONException e){e.printStackTrace();}

        return revList;
    }

    public static List<Movie> parseDatabase(Cursor cursor){
        List<Movie> movieList = new ArrayList<>();



        return movieList;
    }

}
