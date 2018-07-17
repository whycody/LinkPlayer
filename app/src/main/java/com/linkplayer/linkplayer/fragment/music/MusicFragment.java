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
import android.widget.Toast;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.data.SongDao;

import java.util.ArrayList;


public class MusicFragment extends Fragment implements MusicFragmentView{

    private RecyclerView musicListRecycler;
    private MusicRecyclerAdapter recyclerAdapter;
    private ArrayList<Song> songList;
    private MusicPresenterImpl musicPresenter;
    private MediaPlayerService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

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
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MediaPlayerService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder musicBinder = (MediaPlayerService.LocalBinder) service;
            musicService = musicBinder.getService();
            musicService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void playMusic(int position) {
        musicService.setSong(position);
        musicService.playSong();
    }

    @Override
    public void showMusicIsPlaying(int position) {

    }

    @Override
    public void setLatestMusic(int position) {
        songDao.changeLatestMusic(songList.get(position));
    }
}
