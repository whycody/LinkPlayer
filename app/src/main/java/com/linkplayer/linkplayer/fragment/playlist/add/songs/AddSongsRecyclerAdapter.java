package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;

public class AddSongsRecyclerAdapter extends RecyclerView.Adapter<AddSongsRecyclerHolder> {

    private AddSongsPresenter addSongsPresenter;
    private Context context;

    public AddSongsRecyclerAdapter(AddSongsPresenter addSongsPresenter, Context context){
        this.addSongsPresenter = addSongsPresenter;
        this.context = context;
    }

    @NonNull
    @Override
    public AddSongsRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_songs_row, parent, false);

        return new AddSongsRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSongsRecyclerHolder holder, int position) {
        addSongsPresenter.onBindSongRowViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return addSongsPresenter.getAddSongsRowsCount();
    }
}
