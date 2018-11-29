package com.linkplayer.linkplayer.fragment.now;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkplayer.linkplayer.R;

public class NowRecyclerHolder extends RecyclerView.ViewHolder implements NowRowView{

    private TextView titleText, timeText;
    private View itemView, nowDotesButton;
    public NowRecyclerHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        titleText = itemView.findViewById(R.id.title_text);
        timeText = itemView.findViewById(R.id.time_text);
        nowDotesButton = itemView.findViewById(R.id.dotes_button);
    }

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setTime(String time) {
        timeText.setText(time);
    }

    @Override
    public void setTextColor(int color) {
        titleText.setTextColor(color);
        timeText.setTextColor(color);
    }

    @Override
    public void setOnClickItemView(View.OnClickListener onClick) {
        itemView.setOnClickListener(onClick);
    }

    @Override
    public void setOnClickPopupMenu(View.OnClickListener onClick) {
        nowDotesButton.setOnClickListener(onClick);
    }
}
