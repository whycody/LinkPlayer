package com.linkplayer.linkplayer.fragment.music;

public interface MusicFragmentView {

    void playMusic(int position);

    void showMusicIsPlaying(int position);

    void setLatestMusic(int position);
}
