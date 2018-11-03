package com.linkplayer.linkplayer.fragment.artist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.SongList;


public class ArtistFragment extends Fragment implements ArtistFragmentView{

    private ArtistPresenterImpl artistPresenter;
    private ArtistRecyclerAdapter artistAdapter;
    private MusicListData musicListData;
    private RecyclerView artistRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        musicListData = new MusicListData(getActivity());
        artistRecycler = view.findViewById(R.id.artist_recycler);
        artistPresenter = new ArtistPresenterImpl(new MusicListData(getActivity()).getArtistList(), this, getActivity());
        artistAdapter = new ArtistRecyclerAdapter(getActivity(), artistPresenter);
        artistRecycler.setAdapter(artistAdapter);
        artistRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        artistRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    @Override
    public void notifyItemChanged(int position, SongList songList) {
        artistPresenter.getArtistSongList().set(position, songList);
    }

    public void refreshData(){
        artistAdapter.setArtistSongList(new MusicListData(getActivity()).getArtistList());
    }
}
