package com.linkplayer.linkplayer.fragment.artist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;

public class ArtistRecyclerAdapter extends RecyclerView.Adapter<ArtistRecyclerHolder> {

    private Context context;
    private ArtistPresenterImpl artistPresenter;

    public ArtistRecyclerAdapter(Context context, ArtistPresenterImpl artistPresenter){
        this.context= context;
        this.artistPresenter = artistPresenter;
    }

    @NonNull
    @Override
    public ArtistRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.artist_row, parent, false);

        return new ArtistRecyclerHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistRecyclerHolder holder, int position) {
        artistPresenter.onBindSongRowViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return artistPresenter.getArtistRowsCount();
    }
}
