package com.linkplayer.linkplayer.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class PlaylistList implements Comparable<PlaylistList>{

    private String title;
    private ArrayList<Song> songList = new ArrayList<>();

    public PlaylistList(){

    }

    public PlaylistList(String title, ArrayList<Song> songList) {
        this.title = title;
        this.songList = songList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public int compareTo(@NonNull PlaylistList playlistList) {
        int compareSongSize = playlistList.getSongList().size();
        return this.getSongList().size()-compareSongSize;
    }
}
