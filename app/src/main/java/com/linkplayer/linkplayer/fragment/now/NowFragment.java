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
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.fragment.music.MusicFragmentView;
import com.linkplayer.linkplayer.fragment.music.MusicPresenterImpl;
import com.linkplayer.linkplayer.fragment.music.MusicRecyclerAdapter;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;


public class NowFragment extends Fragment implements MusicFragmentView, NowView{

    private RecyclerView nowRecycler;
    private MusicPresenterImpl presenter;
    private MusicRecyclerAdapter recyclerAdapter;
    private SongList songList;
    private SongListDao songListDao;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now, container, false);
        nowRecycler = view.findViewById(R.id.now_recycler);
        songListDao = new SongListDao(getContext());
        songList = songListDao.getLatestSongList();
        presenter = new MusicPresenterImpl(songList.getSongList(), this, getActivity());
        recyclerAdapter = new MusicRecyclerAdapter(presenter, getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        nowRecycler.setLayoutManager(linearLayoutManager);
        nowRecycler.setAdapter(recyclerAdapter);
        nowRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    public void refresh(){
        recyclerAdapter.setSongArrayList(new SongListDao(getActivity()).getLatestSongList().getSongList());
    }

    @Override
    public void onItemClick(int position) {
        ((MainActivity)getActivity()).playSong(position);
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position) {
//        recyclerAdapter.getSongArrayList().get(lastPosition).setChoosed(false);
//        recyclerAdapter.getSongArrayList().get(position).setChoosed(true);
//        recyclerAdapter.notifyItemChanged(position);
//        recyclerAdapter.notifyItemChanged(lastPosition);
//        linearLayoutManager.scrollToPosition(position);
    }

    @Override
    public void notifyItemDeleted(int position) {
        songList.getSongList().remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemChanged(Song lastSong, Song song) {
        ArrayList<Song> songList = recyclerAdapter.getSongArrayList();

        int lastPosition = 0;
        int position = 0;
        for(int i = 0; i<songList.size(); i++){
            Song songFromList = songList.get(i);
            if(songFromList.getPath().equals(lastSong.getPath())) {
                recyclerAdapter.getSongArrayList().get(i).setChoosed(false);
                lastPosition = i;
            }

            if(songFromList.getPath().equals(song.getPath())){
                recyclerAdapter.getSongArrayList().get(i).setChoosed(true);
                position = i;
            }
        }
        recyclerAdapter.notifyItemChanged(position);
        recyclerAdapter.notifyItemChanged(lastPosition);
        linearLayoutManager.scrollToPosition(position);

    }

    @Override
    public void setSongList(SongList songList) {
        this.songList = songList;
    }
}
