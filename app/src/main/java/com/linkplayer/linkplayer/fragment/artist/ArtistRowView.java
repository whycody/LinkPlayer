package com.linkplayer.linkplayer.fragment.artist;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface ArtistRowView {

    void setArtistTitle(String artist);

    void setSongsNumber(String number);

    void setOnClick(View.OnClickListener onClick);

    void setBackground(Drawable drawable);
}
