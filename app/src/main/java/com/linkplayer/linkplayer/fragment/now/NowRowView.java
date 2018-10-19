package com.linkplayer.linkplayer.fragment.now;

import android.view.View;

public interface NowRowView {

    void setTitle(String title);

    void setTime(String time);

    void setTextColor(int color);

    void setOnClickItemView(View.OnClickListener onClick);

    void setOnClickPopupMenu(View.OnClickListener onClick);
}
