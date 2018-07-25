package com.linkplayer.linkplayer.fragment.music;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public interface MusicRowView {

    void setTitle(String title);

    void setAuthor(String author);

    void setMinutes(String minutes);

    void setSeconds(String seconds);

    void setBackground(Drawable drawable);

    void setOnClickItemView(View.OnClickListener onClick);

    void setOnClickPopupMenu(View.OnClickListener onClick);
}
