package com.linkplayer.linkplayer.fragment.music;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface MusicRowView {

    void setTitle(String title);

    void setAuthor(String author);

    void setImage(Bitmap image);

    void setImage(Drawable drawable);

    void setOnClick(View.OnClickListener onClick);
}
