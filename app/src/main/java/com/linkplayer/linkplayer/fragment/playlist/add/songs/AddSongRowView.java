package com.linkplayer.linkplayer.fragment.playlist.add.songs;


import android.widget.CompoundButton;

public interface AddSongRowView {

    void setTitle(String title);

    void setArtist(String artist);

    void setOnCheckedListener(CompoundButton.OnCheckedChangeListener onCheckedListener);
}
