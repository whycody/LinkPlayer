package com.linkplayer.linkplayer.fragment.music;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.MainActivity;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.data.SongDao;

import java.util.ArrayList;


public class MusicFragment extends Fragment implements MusicFragmentView{

    private RecyclerView musicListRecycler;
    private MusicRecyclerAdapter recyclerAdapter;
    private ArrayList<Song> songList;
    private MusicPresenterImpl musicPresenter;

    private SongDao songDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        musicListRecycler = view.findViewById(R.id.music_list_recycler);
        songList = new MusicListData(getActivity()).getSongList();
        recyclerAdapter = new MusicRecyclerAdapter(new MusicPresenterImpl(songList,this,
                getActivity()), getActivity());
        musicPresenter = new MusicPresenterImpl(songList, this, getActivity());
        musicListRecycler.setAdapter(recyclerAdapter);
        musicListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicListRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        songDao = new SongDao(getActivity());
//        songDao.insertSongList(songList.get(0), "musicladasd");
//        songDao.changeLatestMusic(songList.get(18));
//
//        Song latestSong = songDao.getLatestMusic();
//        Toast.makeText(getActivity(), latestSong.getTitle(), Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void playMusic(int position) {
        ((MainActivity)getActivity()).playSong(position);
    }


}
