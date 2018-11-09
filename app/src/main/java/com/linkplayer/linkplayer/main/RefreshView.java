package com.linkplayer.linkplayer.main;

public interface RefreshView {

    void notifyItemChanged(int lastPosition, int position);

    void notifyTheLastSongPlayed();
}
