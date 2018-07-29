package com.linkplayer.linkplayer.fragment.playlist.add.songs;


import android.view.View;
import android.widget.CompoundButton;

public interface AddSongRowView {

    void setTitle(String title);

    void setArtist(String artist);

    void setChecked(boolean checked);

    void setOnCheckedListener(CompoundButton.OnCheckedChangeListener onCheckedListener);
}
