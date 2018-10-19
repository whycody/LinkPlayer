package com.linkplayer.linkplayer.fragment.now;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.SongList;

public class NowRecyclerAdapter extends RecyclerView.Adapter<NowRecyclerHolder> {

    private NowPresenterImpl nowPresenter;
    private Activity activity;

    public NowRecyclerAdapter(NowPresenterImpl nowPresenter, Activity activity){
        this.nowPresenter = nowPresenter;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NowRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.now_music_row, parent, false);

        return new NowRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NowRecyclerHolder holder, int position) {
        nowPresenter.onBindSongRowViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return nowPresenter.getSongRowsCount();
    }

    public void setSongList(SongList songList){
        nowPresenter.setSongList(songList);
        this.notifyDataSetChanged();
    }

    public SongList getSongList(){
        return nowPresenter.getSongList();
    }
}
