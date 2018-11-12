package com.linkplayer.linkplayer.main;

import android.content.Intent;

import com.linkplayer.linkplayer.MediaPlayerService;

public interface MainPresenter {

    void saveSettingsInPreferences(boolean random, boolean repeat);

    void initializeFragments();

    void getPreferencesAndSetButtons();

    void saveRandomPreferences(boolean random);

    void saveRepeatReferences(boolean repeat);

    void setMusicService(MediaPlayerService musicService);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void refreshAllData();

    String getTitle();

    int getLatestSong();
}
