package com.linkplayer.linkplayer.mappers;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongRealm;

public class SongMapper {

    public Song fromRealm(SongRealm songRealm){
        Song song = new Song();
        song.setAlbumPhotoAvailable(songRealm.isAlbumPhotoAvailable());
        song.setArtist(songRealm.getArtist());
        song.setId(songRealm.getId());
        song.setPath(songRealm.getPath());
        song.setTitle(songRealm.getTitle());
        return song;
    }

    public SongRealm toRealm(Song song){
        SongRealm songRealm = new SongRealm();
        songRealm.setAlbumPhotoAvailable(song.isAlbumPhotoAvailable());
        songRealm.setArtist(song.getArtist());
        songRealm.setId(song.getId());
        songRealm.setPath(song.getPath());
        songRealm.setTitle(song.getTitle());
        return songRealm;
    }
}
