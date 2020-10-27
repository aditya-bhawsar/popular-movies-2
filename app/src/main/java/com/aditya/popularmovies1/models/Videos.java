package com.aditya.popularmovies1.models;

public class Videos {

    private String Name,Type,Key;

    public Videos(String name, String type, String key) {
        Name = name;
        Type = type;
        Key = key;
    }

    public String getName() { return Name; }

    public void setName(String name) { Name = name; }

    public String getType() { return Type; }

    public void setType(String type) { Type = type; }

    public String getKey() { return Key; }

    public void setKey(String key) { Key = key; }
}
