package com.linkplayer.linkplayer.fragment.music;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

public interface MusicRowView {

    void setTitle(String title);

    void setAuthor(String author);

    void setMinutes(String minutes);

    void setSeconds(String seconds);

    void setOnClick(View.OnClickListener onClick);
}
