package com.linkplayer.linkplayer.model;


public class Song {

    private String title, artist, path, duration;
    private boolean choosed = false;
    private long id;

    public Song() {
    }

    public Song(long id, String title, String artist, String path, String duration) {
        this.title = title;
        this.id = id;
        this.artist = artist;
        this.path = path;
        this.duration = duration;
    }

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }
}
