package com.linkplayer.linkplayer.fragment.artist;

import android.view.View;

public interface ArtistRowView {

    void setArtistTitle(String artist);

    void setSongsNumber(String number);

    void setOnClick(View.OnClickListener onClick);
}
