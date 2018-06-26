package com.linkplayer.linkplayer.model;

import android.graphics.Bitmap;

public class Song {

    private String title, artist, path;
    private boolean albumPhotoAvailable;
    private Bitmap art;
    private long id;

    public Song() {
    }

    public Song(long id, String title, String artist, String path, Bitmap art, boolean albumPhotoAvailable) {
        this.title = title;
        this.id = id;
        this.artist = artist;
        this.art = art;
        this.albumPhotoAvailable = albumPhotoAvailable;
        this.path = path;
    }

    public boolean isAlbumPhotoAvailable() {
        return albumPhotoAvailable;
    }

    public void setAlbumPhotoAvailable(boolean albumPhotoAvailable) {
        this.albumPhotoAvailable = albumPhotoAvailable;
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

    public Bitmap getArt() {
        return art;
    }

    public void setArt(Bitmap art) {
        this.art = art;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }
}
