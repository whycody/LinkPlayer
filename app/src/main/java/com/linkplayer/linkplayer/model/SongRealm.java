package com.linkplayer.linkplayer.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SongRealm extends RealmObject{

    private String title, artist, path;
    private long id;
    @PrimaryKey
    private int key;

    public SongRealm(){

    }

    public SongRealm(String title, String artist, String path, long id) {
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKey(){
        return key;
    }
}
