package com.linkplayer.linkplayer.fragment.playlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistRecyclerHolder> {

    private PlaylistPresenter playlistPresenter;
    private Context context;

    public PlaylistRecyclerAdapter(PlaylistPresenter playlistPresenter, Context context){
        this.playlistPresenter = playlistPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.playlist_row, parent, false);

        return new PlaylistRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistRecyclerHolder holder, int position) {
        playlistPresenter.onBindSongRowViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return playlistPresenter.getPlaylistRowCount();
    }

    public void setSongListArrayList(ArrayList<SongList> songListArrayList){
        playlistPresenter.setSongListArrayList(songListArrayList);
        this.notifyDataSetChanged();
    }

}
