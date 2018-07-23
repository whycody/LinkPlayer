package com.linkplayer.linkplayer.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ArtistList implements Comparable<ArtistList>{

    private String artist;
    private ArrayList<Song> songList = new ArrayList<>();

    public ArtistList(){

    }

    public ArtistList(String artist, ArrayList<Song> songList) {
        this.artist = artist;
        this.songList = songList;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void addSong(Song song){
        songList.add(song);
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    @Override
    public int compareTo(@NonNull ArtistList artistList) {
        int compareSongSize = artistList.getSongList().size();
        return this.getSongList().size()-compareSongSize;
    }
}
