package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linkplayer.linkplayer.R;

public class MusicRecyclerHolder extends RecyclerView.ViewHolder implements MusicRowView{

    private ImageView albumPhoto;
    private TextView titleText, authorText;
    private MusicPresenterImpl musicPresenterImpl;
    private View itemView;
    private Context context;

    public MusicRecyclerHolder(MusicPresenterImpl musicPresenterImpl, View itemView, Context context){
        super(itemView);
        this.musicPresenterImpl = musicPresenterImpl;
        this.itemView = itemView;
        albumPhoto = itemView.findViewById(R.id.album_photo);
        titleText = itemView.findViewById(R.id.title_text);
        authorText = itemView.findViewById(R.id.author_text);
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
    public void setImage(Bitmap image) {
        Glide.with(context).load(image).into(albumPhoto);
    }

    @Override
    public void setImage(Drawable drawable) {
        Glide.with(context).load(drawable).into(albumPhoto);
    }

    @Override
    public void setOnClick(View.OnClickListener onClick) {
        itemView.setOnClickListener(onClick);
    }
}
