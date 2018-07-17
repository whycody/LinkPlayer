package com.linkplayer.linkplayer.model;

import java.util.ArrayList;

public class SongList {

    private ArrayList<Song> songArrayList;
    private String name;
    private int key;

    public SongList(){

    }

    public SongList(ArrayList<Song> songArrayList, String name, int key) {
        this.songArrayList = songArrayList;
        this.name = name;
        this.key = key;
    }

    public ArrayList<Song> getSongArrayList() {
        return songArrayList;
    }

    public void setSongArrayList(ArrayList<Song> songArrayList) {
        this.songArrayList = songArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
