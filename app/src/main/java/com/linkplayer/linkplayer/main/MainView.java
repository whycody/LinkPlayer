package com.linkplayer.linkplayer.main;

import com.linkplayer.linkplayer.fragment.artist.ArtistFragment;
import com.linkplayer.linkplayer.fragment.music.MusicFragment;
import com.linkplayer.linkplayer.fragment.now.NowFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistFragment;

public interface MainView {

    void playSong(int position);

    void setSettings(boolean random, boolean repeat);

    void showRandomIsActive(boolean active);

    void showRepeatIsActive(boolean active);

    void showIsPlaying();

    void showIsStopped();

    void setTitleAndSong();

    void setPagerCurrentItem(int position);

    NowFragment getNowFragment();

    PlaylistFragment getPlaylistFragment();

    ArtistFragment getArtistFragment();

    MusicFragment getMusicFragment();
}
