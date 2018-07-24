package com.linkplayer.linkplayer.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class SongList implements Comparable<SongList>{

    private ArrayList<Song> songList = new ArrayList<>();
    private String title;
    private int key;

    public SongList(){

    }

    public SongList(ArrayList<Song> songList, String title, int key) {
        this.songList = songList;
        this.title = title;
        this.key = key;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void addSong(Song song){
        songList.add(song);
    }

    @Override
    public int compareTo(@NonNull SongList o) {
        int compareSongSize = o.getSongList().size();
        return this.getSongList().size()-compareSongSize;
    }
}
