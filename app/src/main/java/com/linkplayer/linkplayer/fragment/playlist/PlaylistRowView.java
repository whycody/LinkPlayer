package com.linkplayer.linkplayer.fragment.playlist;

import android.view.View;

public interface PlaylistRowView {

    void setTitle(String title);

    void setSongsNumber(String songsNumber);

    void setOnItemViewClick(View.OnClickListener onClick);

    void setOnThreeDotesClick(View.OnClickListener onThreeDotesClick);
}
