package com.linkplayer.linkplayer.playlist.view;

import com.linkplayer.linkplayer.model.SongList;

public interface PlaylistViewPresenter {

    void onCreate();

    SongList getSongList();

    String getType();

    int getKey();

    String getArtist();
}
