package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;

public class MusicRecyclerAdapter extends RecyclerView.Adapter<MusicRecyclerHolder> {

    private MusicPresenterImpl musicPresenterImpl;
    private Context context;

    public MusicRecyclerAdapter(MusicPresenterImpl musicPresenterImpl, Context context){
        this.musicPresenterImpl = musicPresenterImpl;
        this.context = context;
    }

    @NonNull
    @Override
    public MusicRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.music_row, parent, false);

        return new MusicRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicRecyclerHolder holder, int position) {
        musicPresenterImpl.onBindSongRowViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return musicPresenterImpl.getSongRowsCount();
    }

    public void setSongArrayList(ArrayList<Song> songList){
        musicPresenterImpl.setSongArrayList(songList);
        this.notifyDataSetChanged();
    }

    public ArrayList<Song> getSongArrayList(){
        return musicPresenterImpl.getSongArrayList();
    }
}
