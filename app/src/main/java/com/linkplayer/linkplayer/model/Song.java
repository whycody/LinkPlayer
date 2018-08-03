package com.linkplayer.linkplayer.model;


public class Song {

    private String title, artist, path, duration;
    private boolean choosed = false;
    private long id;
    private int key;

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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", choosed=" + choosed +
                ", id=" + id +
                '}';
    }
}
