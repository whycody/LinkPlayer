package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.widget.CompoundButton;

public interface AddSongRowView {

    void setTitle(String title);

    void setArtist(String artist);

    void setChoosed(boolean checked);

    void setOnCheckedListener(CompoundButton.OnCheckedChangeListener onCheckedListener);
}
