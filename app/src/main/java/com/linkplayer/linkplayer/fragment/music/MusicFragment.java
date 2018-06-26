package com.linkplayer.linkplayer.fragment.music;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;


public class MusicFragment extends Fragment {

    private RecyclerView musicListRecycler;
    private MusicRecyclerAdapter recyclerAdapter;
    private ArrayList<Song> songList;
    private MusicPresenter musicPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        musicListRecycler = view.findViewById(R.id.music_list_recycler);
        songList = new MusicListData(getActivity()).getSongList();
        recyclerAdapter = new MusicRecyclerAdapter(new MusicPresenterImpl(songList,
                getActivity()), getActivity());
        musicPresenter = new MusicPresenterImpl(songList, getActivity());
        musicListRecycler.setAdapter(recyclerAdapter);
        musicListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicListRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        musicPresenter.onStart();
    }

}
