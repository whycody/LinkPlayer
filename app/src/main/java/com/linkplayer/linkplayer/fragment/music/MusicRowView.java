package com.linkplayer.linkplayer.fragment.music;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public interface MusicRowView {

    void setTitle(String title);

    void setAuthor(String author);

    void setImage(Bitmap image);

    void setImage(Drawable drawable);
}
