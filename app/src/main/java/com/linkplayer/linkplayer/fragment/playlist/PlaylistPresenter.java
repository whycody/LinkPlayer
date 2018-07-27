package com.linkplayer.linkplayer.fragment.playlist;

public interface PlaylistPresenter {

    void onBindSongRowViewAtPosition(PlaylistRecyclerHolder playlistRecyclerHolder, final int position);

    int getPlaylistRowCount();
}
