package com.linkplayer.linkplayer.playlist.view;

import android.view.View;

public interface PlaylistView {

    void setDeletePlaylistButtonVisiblity(int visiblity);

    void setTopButtonOnClickListener(View.OnClickListener onClick);

    void setTopButtonText(String text);;
}
