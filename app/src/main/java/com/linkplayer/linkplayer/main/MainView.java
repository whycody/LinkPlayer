package com.linkplayer.linkplayer.main;



public interface MainView {

    void playSong(int position);

    void setSettings(boolean random, boolean repeat);

    void showRandomIsChosed();

    void showRandomIsNotChosed();

    void showRepeatIsChosed();

    void showRepeatIsNotChosed();
}
