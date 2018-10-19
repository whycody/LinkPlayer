package com.linkplayer.linkplayer.fragment.now;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongInformator;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.fragment.music.MusicFragmentView;
import com.linkplayer.linkplayer.fragment.music.MusicPresenterImpl;
import com.linkplayer.linkplayer.fragment.music.MusicRecyclerAdapter;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;


public class NowFragment extends Fragment implements MusicFragmentView, NowView, DeleteSongInformator{

    private RecyclerView nowRecycler;
    private NowPresenterImpl nowPresenter;
    private NowRecyclerAdapter nowRecyclerAdapter;
    private SongList songList;
    private SongListDao songListDao;
    private LinearLayoutManager linearLayoutManager;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now, container, false);
        nowRecycler = view.findViewById(R.id.now_recycler);
        songListDao = new SongListDao(getContext());
        songList = songListDao.getLatestSongList();
        nowPresenter = new NowPresenterImpl(songList, getActivity(), this, this);
        nowRecyclerAdapter = new NowRecyclerAdapter(nowPresenter, getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        nowRecycler.setLayoutManager(linearLayoutManager);
        nowRecycler.setAdapter(nowRecyclerAdapter);
        nowRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    public void refresh(){
        nowRecyclerAdapter.setSongList(songListDao.getLatestSongList());
    }

    @Override
    public void onItemClick(int position) {
        ((MainActivity)getActivity()).playSong(position);
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position) {

    }

    @Override
    public void notifyItemDeleted(int position) {
        nowPresenter.getSongList().getSongList().remove(position);
        nowRecyclerAdapter.notifyItemRemoved(position);
        nowRecyclerAdapter.notifyItemRangeChanged(position, nowPresenter.getSongList().getSongList().size());
        ((MainActivity)getActivity()).notifyAllData(position, songList.getSongList().get(position));
    }

    public int getPosition(){
        return position;
    }

    @Override
    public void notifyItemChanged(Song lastSong, Song song) {
        ArrayList<Song> songList = nowRecyclerAdapter.getSongList().getSongList();

        int lastPosition = 0;
        int position = 0;
        for(int i = 0; i<songList.size(); i++){
            Song songFromList = songList.get(i);
            if(songFromList.getPath().equals(lastSong.getPath())) {
                nowRecyclerAdapter.getSongList().getSongList().get(i).setChoosed(false);
                lastPosition = i;
            }

            if(songFromList.getPath().equals(song.getPath())){
                nowRecyclerAdapter.getSongList().getSongList().get(i).setChoosed(true);
                position = i;
            }
        }
        this.position = position;
        nowRecyclerAdapter.notifyItemChanged(position);
        nowRecyclerAdapter.notifyItemChanged(lastPosition);
        linearLayoutManager.scrollToPosition(position);

    }

    @Override
    public void notifySongAddedToPlaylist() {
        ((MainActivity)getActivity()).notifySongAddedToPlaylist();
    }

    @Override
    public void setSongList(SongList songList) {
        this.songList = songList;
    }

    @Override
    public void notifySongDeleted(int position, boolean deleted) {
        if(deleted)
            notifyItemDeleted(position);
    }
}
