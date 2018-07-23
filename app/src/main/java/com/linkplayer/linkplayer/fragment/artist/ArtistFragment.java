package com.linkplayer.linkplayer.fragment.artist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.ArtistListData;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;


public class ArtistFragment extends Fragment {

    private ArtistPresenterImpl artistPresenter;
    private ArtistRecyclerAdapter recyclerAdapter;
    private RecyclerView artistRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        artistRecycler = view.findViewById(R.id.artist_recycler);
        artistPresenter = new ArtistPresenterImpl(new ArtistListData(getActivity()).getArtistList(), getActivity());
        recyclerAdapter = new ArtistRecyclerAdapter(getActivity(), artistPresenter);
        artistRecycler.setAdapter(recyclerAdapter);
        artistRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        artistRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

}
