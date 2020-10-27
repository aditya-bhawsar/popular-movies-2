package com.aditya.popularmovies1.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movie_table")
public class Movie implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int movie_id;
    String name, overview, poster, rated, popularity, date;

    public Movie(int movie_id, String name, String overview, String poster, String rated, String popularity, String date) {
        this.movie_id = movie_id;
        this.name = name;
        this.overview = overview;
        this.poster = poster;
        this.rated = rated;
        this.popularity = popularity;
        this.date = date;
    }

    @Ignore
    protected Movie(Parcel in) {
        movie_id = in.readInt();
        name = in.readString();
        overview = in.readString();
        poster = in.readString();
        rated = in.readString();
        popularity = in.readString();
        date = in.readString();
    }

    @Ignore
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movie_id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(rated);
        dest.writeString(popularity);
        dest.writeString(date);
    }
}
