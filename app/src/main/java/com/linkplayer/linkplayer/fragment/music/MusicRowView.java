package com.linkplayer.linkplayer.fragment.music;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

public interface MusicRowView {

    void setTitle(String title);

    void setAuthor(String author);

    void setDuration(String duration);

    void setFirstLetter(char letter);

    void setGradientDrawable(GradientDrawable gradientDrawable);

    void setOnClick(View.OnClickListener onClick);
}
