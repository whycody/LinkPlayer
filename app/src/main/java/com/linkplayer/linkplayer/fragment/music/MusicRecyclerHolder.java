package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linkplayer.linkplayer.R;

public class MusicRecyclerHolder extends RecyclerView.ViewHolder implements MusicRowView{

    private TextView titleText, authorText, durationText, letterText;
    private View albumPhoto;
    private MusicPresenterImpl musicPresenterImpl;
    private View itemView;
    private Context context;

    public MusicRecyclerHolder(MusicPresenterImpl musicPresenterImpl, View itemView, Context context){
        super(itemView);
        this.musicPresenterImpl = musicPresenterImpl;
        this.itemView = itemView;
        titleText = itemView.findViewById(R.id.title_text);
        authorText = itemView.findViewById(R.id.author_text);
        durationText = itemView.findViewById(R.id.duration_text);
        letterText = itemView.findViewById(R.id.first_letter);
        albumPhoto = itemView.findViewById(R.id.album_photo);
        this.context= context;
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
    public void setDuration(String duration) {
        durationText.setText(duration);
    }

    @Override
    public void setFirstLetter(char letter) {
        letterText.setText(String.valueOf(letter));
    }

    @Override
    public void setGradientDrawable(GradientDrawable gradientDrawable) {
        albumPhoto.setBackgroundDrawable(gradientDrawable);
    }

    @Override
    public void setOnClick(View.OnClickListener onClick) {
        itemView.setOnClickListener(onClick);
    }
}
