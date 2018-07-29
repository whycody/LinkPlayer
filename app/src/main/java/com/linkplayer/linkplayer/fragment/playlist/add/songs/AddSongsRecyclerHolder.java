package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.linkplayer.linkplayer.R;

public class AddSongsRecyclerHolder extends RecyclerView.ViewHolder implements AddSongRowView{

    private TextView titleText, artistText;
    private ToggleButton addSongToggle;

    public AddSongsRecyclerHolder(View itemView) {
        super(itemView);
        titleText = itemView.findViewById(R.id.title_text);
        artistText = itemView.findViewById(R.id.author_text);
        addSongToggle = itemView.findViewById(R.id.add_song_toggle);
    }

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setArtist(String artist) {
        artistText.setText(artist);
    }

    @Override
    public void setOnCheckedListener(CompoundButton.OnCheckedChangeListener onCheckedListener) {
        addSongToggle.setOnCheckedChangeListener(onCheckedListener);
    }


}
