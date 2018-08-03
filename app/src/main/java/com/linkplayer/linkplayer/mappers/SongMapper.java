package com.linkplayer.linkplayer.mappers;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongRealm;

public class SongMapper {

    public Song fromRealm(SongRealm songRealm){
        Song song = new Song();
        song.setArtist(songRealm.getArtist());
        song.setId(songRealm.getId());
        song.setPath(songRealm.getPath());
        song.setTitle(songRealm.getTitle());
        song.setDuration(songRealm.getDuration());
        song.setKey(songRealm.getKey());
        return song;
    }

    public SongRealm toRealm(Song song){
        SongRealm songRealm = new SongRealm();
        songRealm.setArtist(song.getArtist());
        songRealm.setId(song.getId());
        songRealm.setPath(song.getPath());
        songRealm.setTitle(song.getTitle());
        songRealm.setDuration(song.getDuration());
        songRealm.setKey(song.getKey());
        return songRealm;
    }
}
