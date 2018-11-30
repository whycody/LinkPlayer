package com.linkplayer.linkplayer.main.add.song.to.playlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkplayer.linkplayer.R;

public class AddSongToPlaylistHolder extends RecyclerView.ViewHolder implements AddSongToPlaylistRowView{

    private TextView playlistText;
    private View itemView;

    AddSongToPlaylistHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        playlistText = itemView.findViewById(R.id.playlist_text);
    }

    @Override
    public void setPlaylistText(String text) {
        playlistText.setText(text);
    }

    @Override
    public void setOnItemViewClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }
}
