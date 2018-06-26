package com.linkplayer.linkplayer.fragment.music;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;


public class MusicFragment extends Fragment {

    private RecyclerView musicListRecycler;
    private MusicRecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        musicListRecycler = view.findViewById(R.id.music_list_recycler);
        recyclerAdapter = new MusicRecyclerAdapter(new MusicPresenter(new MusicListData(getActivity()).getSongList(),
                getActivity()), getActivity());
        musicListRecycler.setAdapter(recyclerAdapter);
        musicListRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        musicListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicListRecycler.setNestedScrollingEnabled(false);

        return view;
    }

}
