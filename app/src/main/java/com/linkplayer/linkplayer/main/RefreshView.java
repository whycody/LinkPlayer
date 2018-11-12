package com.linkplayer.linkplayer.main;

public interface RefreshView {

    void notifySongChanged(int lastPosition, int position);

    void notifyTheLastSongPlayed();
}
