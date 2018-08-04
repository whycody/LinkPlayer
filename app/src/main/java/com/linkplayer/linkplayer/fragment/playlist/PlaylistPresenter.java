package com.linkplayer.linkplayer.fragment.playlist;

import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;

public interface PlaylistPresenter {

    void onBindSongRowViewAtPosition(PlaylistRecyclerHolder playlistRecyclerHolder, final int position);

    int getPlaylistRowCount();

    void setAddSongsInformator(AddSongsInformator addSongsInformator);
}
