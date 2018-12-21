package com.linkplayer.linkplayer.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SongRealm extends RealmObject{

    private String title, artist, path, duration;
    private long id, dateModified;
    @PrimaryKey
    private int key;

    public SongRealm(){

    }

    public SongRealm(String title, String artist, String path, String duration, long id, long dateModified) {
        this.title = title;
        this.dateModified = dateModified;
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

    public void setKey(int key){
        this.key = key;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public long getDateModified() {
        return dateModified;
    }

    @Override
    public String toString() {
        return "SongRealm{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", id=" + id +
                ", key=" + key +
                '}';
    }
}
