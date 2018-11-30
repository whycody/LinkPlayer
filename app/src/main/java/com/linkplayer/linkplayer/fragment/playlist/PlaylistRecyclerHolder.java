package com.linkplayer.linkplayer.fragment.playlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linkplayer.linkplayer.R;

public class PlaylistRecyclerHolder extends RecyclerView.ViewHolder implements PlaylistRowView {

    private TextView titleText, songsNumberText;
    private ImageButton dotesButton;
    private View itemView;

    public PlaylistRecyclerHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        titleText = itemView.findViewById(R.id.playlist_text);
        songsNumberText = itemView.findViewById(R.id.songs_number_text);
        dotesButton = itemView.findViewById(R.id.dotes_button);
    }

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setSongsNumber(String songsNumber) {
        songsNumberText.setText(songsNumber);
    }

    @Override
    public void setOnItemViewClick(View.OnClickListener onClick) {
        itemView.setOnClickListener(onClick);
    }

    @Override
    public void setOnThreeDotesClick(View.OnClickListener onThreeDotesClick) {
        dotesButton.setOnClickListener(onThreeDotesClick);
    }
}
