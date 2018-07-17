package com.linkplayer.linkplayer.mappers;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.model.SongListRealm;
import com.linkplayer.linkplayer.model.SongRealm;

import java.util.ArrayList;

import io.realm.RealmList;

public class SongListMapper {

    private SongMapper songMapper = new SongMapper();

    public SongListRealm toRealm(SongList songList){
        SongListRealm songListRealm = new SongListRealm();
        RealmList<SongRealm> realmSongList = new RealmList<>();
        ArrayList<Song> songArrayList = songList.getSongArrayList();
        for(Song song : songArrayList){
            realmSongList.add(songMapper.toRealm(song));
        }
        songListRealm.setSongList(realmSongList);
        songListRealm.setTitle(songList.getName());
        return songListRealm;
    }

    public SongList fromRealm(SongListRealm songListRealm){
        SongList songList = new SongList();
        ArrayList<Song> songArrayList = new ArrayList<>();
        RealmList<SongRealm> realmSongList = songListRealm.getSongList();
        for(SongRealm songRealm: realmSongList){
            songArrayList.add(songMapper.fromRealm(songRealm));
        }
        songList.setSongArrayList(songArrayList);
        songList.setName(songListRealm.getName());
        return songList;
    }
}
