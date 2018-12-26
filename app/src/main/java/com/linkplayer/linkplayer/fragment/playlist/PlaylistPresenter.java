package com.linkplayer.linkplayer.fragment.playlist;

import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public interface PlaylistPresenter {

    void onBindSongRowViewAtPosition(PlaylistRecyclerHolder playlistRecyclerHolder, final int position);

    int getPlaylistRowCount();

    void setAddSongsInformator(AddSongsInformator addSongsInformator);

    void setSongListArrayList(ArrayList<SongList> songListArrayList);

    ArrayList<SongList> getSongListArrayList();
}
