package com.linkplayer.linkplayer.main;

import com.linkplayer.linkplayer.MediaPlayerService;

public interface MainPresenter {

    void saveSettingsInPreferences(boolean random, boolean repeat);

    void getPreferencesAndSetButtons();

    void saveRandomPreferences(boolean random);

    void saveRepeatReferences(boolean repeat);

    void setMusicService(MediaPlayerService musicService);

    void setClickedSongIfRandom(int position);

    String getTitle();

    int getLatestSong();
}
