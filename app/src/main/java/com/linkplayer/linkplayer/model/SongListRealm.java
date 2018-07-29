package com.linkplayer.linkplayer.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SongListRealm extends RealmObject{

    private RealmList<SongRealm> songList;
    private String name;
    @PrimaryKey
    private int key;

    public SongListRealm() {

    }

    public SongListRealm(RealmList<SongRealm> songList, String name) {
        this.songList = songList;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public RealmList<SongRealm> getSongList() {
        return songList;
    }

    public void setSongList(RealmList<SongRealm> songList) {
        this.songList = songList;
    }

    public int getKey(){
        return key;
    }

    public void addSong(SongRealm songRealm){
        songList.add(songRealm);
    }
}
