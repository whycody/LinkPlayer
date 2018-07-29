package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import com.linkplayer.linkplayer.model.SongList;

public interface AddSongsPresenter {

    void onBindSongRowViewAtPosition(AddSongsRecyclerHolder holder, final int position);

    void addSongsToPlaylist(SongList songList);

    int getAddSongsRowsCount();
}
