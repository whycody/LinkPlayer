package com.linkplayer.linkplayer.fragment.artist;

public interface ArtistFragmentView {

    void notifyItemChanged(int lastSong, int position);

    void setLatestSong(int latestSong);
}
