package com.linkplayer.linkplayer.main.add.song.to.playlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;

public class AddSongToPlaylistAdapter extends RecyclerView.Adapter<AddSongToPlaylistHolder> {

    private AddSongToPlaylistPresenter presenter;
    private Context context;

    public AddSongToPlaylistAdapter(AddSongToPlaylistPresenter presenter, Context context){
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public AddSongToPlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_song_to_playlist_row, parent, false);

        return new AddSongToPlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSongToPlaylistHolder holder, int position) {
        presenter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    public AddSongToPlaylistPresenter getPresenter() {
        return presenter;
    }
}
