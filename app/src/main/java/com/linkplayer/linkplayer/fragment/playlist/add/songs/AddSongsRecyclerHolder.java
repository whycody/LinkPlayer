package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;

public class AddSongsRecyclerHolder extends RecyclerView.ViewHolder implements AddSongRowView{

    private TextView titleText, artistText;
    private CheckBox addSongBox;

    public AddSongsRecyclerHolder(View itemView) {
        super(itemView);
        titleText = itemView.findViewById(R.id.title_text);
        artistText = itemView.findViewById(R.id.author_text);
        addSongBox = itemView.findViewById(R.id.add_song_box);
        itemView.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = addSongBox.isChecked();
            addSongBox.setChecked(!checked);
        }
    };

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setArtist(String artist) {
        artistText.setText(artist);
    }

    @Override
    public void setChoosed(boolean checked) {
        addSongBox.setChecked(checked);
    }

    @Override
    public void setOnCheckedListener(CompoundButton.OnCheckedChangeListener onCheckedListener) {
        addSongBox.setOnCheckedChangeListener(onCheckedListener);
    }


}
