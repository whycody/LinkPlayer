package com.linkplayer.linkplayer.fragment.playlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;


public class PlaylistFragment extends Fragment implements PlaylistView, AddSongsInformator{

    private RecyclerView playlistRecycler;
    private PlaylistPresenter playlistPresenter;
    private PlaylistRecyclerAdapter recyclerAdapter;
    private ArrayList<SongList> songListArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlistRecycler = view.findViewById(R.id.playlist_recycler);

        songListArrayList = new SongListDao(getActivity()).getAllTheSongLists();
        playlistPresenter = new PlaylistPresenterImpl(songListArrayList, this, getActivity());
        playlistPresenter.setAddSongsInformator(this);
        recyclerAdapter = new PlaylistRecyclerAdapter(playlistPresenter, getActivity());
        playlistRecycler.setAdapter(recyclerAdapter);
        playlistRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        playlistRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    @Override
    public void notifyItemDeleted(int position) {
        songListArrayList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void refresh() {
        recyclerAdapter.setSongListArrayList(new SongListDao(getActivity()).getAllTheSongLists());
    }

    @Override
    public void notifyItemChanged(SongList songList, int position) {
        songListArrayList.set(position, songList);
        recyclerAdapter.notifyItemChanged(position);
    }
}
