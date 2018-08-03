package com.linkplayer.linkplayer.playlist.view;

import android.view.View;

import com.linkplayer.linkplayer.model.SongList;

public interface PlaylistView {

    void setDeletePlaylistButtonVisiblity(int visiblity);

    void setTopButtonOnClickListener(View.OnClickListener onClick);

    void setTopButtonText(String text);

    SongList getSongList();
}
