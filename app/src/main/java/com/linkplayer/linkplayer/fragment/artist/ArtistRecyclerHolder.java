package com.linkplayer.linkplayer.fragment.artist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linkplayer.linkplayer.R;

public class ArtistRecyclerHolder extends RecyclerView.ViewHolder implements ArtistRowView {

    private TextView artistTitleText, songsNumberText;
    private ImageView singerPhoto;
    private Context context;
    private View itemView;

    public ArtistRecyclerHolder(View itemView, Context context) {
        super(itemView);
        this.itemView = itemView;
        this.context = context;
        artistTitleText = itemView.findViewById(R.id.playlist_text);
        songsNumberText = itemView.findViewById(R.id.songs_number_text);
        singerPhoto = itemView.findViewById(R.id.singer_photo);
        Glide.with(context).load(R.drawable.singer_white).into(singerPhoto);
    }

    @Override
    public void setArtistTitle(String artist) {
        artistTitleText.setText(artist);
    }

    @Override
    public void setSongsNumber(String number) {
        songsNumberText.setText(number);
    }

    @Override
    public void setOnClick(View.OnClickListener onClick) {
        itemView.setOnClickListener(onClick);
    }
}
