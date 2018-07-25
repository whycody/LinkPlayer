package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkplayer.linkplayer.R;

public class MusicRecyclerHolder extends RecyclerView.ViewHolder implements MusicRowView{

    private TextView titleText, authorText, minutesNumber, secondsNumber;
    private View albumPhoto, musicRowBackground;
    private View itemView;

    public MusicRecyclerHolder(View itemView){
        super(itemView);
        this.itemView = itemView;
        titleText = itemView.findViewById(R.id.title_text);
        musicRowBackground = itemView.findViewById(R.id.music_row_background);
        authorText = itemView.findViewById(R.id.author_text);
        minutesNumber = itemView.findViewById(R.id.minutes_number);
        secondsNumber = itemView.findViewById(R.id.seconds_number);
        albumPhoto = itemView.findViewById(R.id.album_photo);
    }

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setAuthor(String author) {
        authorText.setText(author);
    }

    @Override
    public void setMinutes(String minutes) {
        minutesNumber.setText(minutes);
    }

    @Override
    public void setSeconds(String seconds) {
        secondsNumber.setText(seconds);
    }

    @Override
    public void setBackground(Drawable drawable) {
        musicRowBackground.setBackgroundDrawable(drawable);
    }

    @Override
    public void setOnClick(View.OnClickListener onClick) {
        itemView.setOnClickListener(onClick);
    }
}
