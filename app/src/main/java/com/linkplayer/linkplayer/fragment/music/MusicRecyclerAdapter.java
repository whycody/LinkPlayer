package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;

public class MusicRecyclerAdapter extends RecyclerView.Adapter<MusicRecyclerHolder> {

    private MusicPresenter musicPresenter;
    private Context context;

    public MusicRecyclerAdapter(MusicPresenter musicPresenter, Context context){
        this.musicPresenter = musicPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.music_row, parent, false);

        return new MusicRecyclerHolder(musicPresenter, view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicRecyclerHolder holder, int position) {
        musicPresenter.onBindSongRowViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return musicPresenter.getSongRowsCount();
    }
}
