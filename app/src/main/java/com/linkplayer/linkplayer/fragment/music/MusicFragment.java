package com.linkplayer.linkplayer.fragment.music;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;


public class MusicFragment extends Fragment implements MusicFragmentView{

    private RecyclerView musicListRecycler;
    private MusicPresenterImpl musicPresenter;
    private MusicRecyclerAdapter recyclerAdapter;
    private ArrayList<Song> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        musicListRecycler = view.findViewById(R.id.music_list_recycler);
        songList = new MusicListData(getActivity()).getSongList();
        musicPresenter = new MusicPresenterImpl(songList, this, getActivity());
        recyclerAdapter = new MusicRecyclerAdapter(new MusicPresenterImpl(songList,this,
                getActivity()), getActivity());
        musicListRecycler.setAdapter(recyclerAdapter);
        musicListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicListRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        SongListDao songListDao = new SongListDao(getActivity());
        if(!songListDao.getLatestSongList().getTitle().equals("All music")) {
            SongList songList = new SongList();
            songList.setSongList(new MusicListData(getActivity()).getSongList());
            songList.setTitle("All music");
            songListDao.changeLatestSongList(songList);
            ((MainActivity)getActivity()).refresh();        }
        ((MainActivity)getActivity()).playSong(position);
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position){
//        Song song = songList.get(lastPosition);
//        song.setChoosed(false);
//        Song newSong = songList.get(position);
//        newSong.setChoosed(true);
//        musicPresenter.setSongArrayList(songList);
//        recyclerAdapter.notifyItemChanged(position);
//        recyclerAdapter.notifyItemChanged(lastPosition);
    }

    @Override
    public void notifyItemChanged(Song lastSong, Song song){
        int lastPosition = 0;
        int position = 0;
        for(int i = 0; i<songList.size(); i++){
            Song songFromList = songList.get(i);
            if(songFromList.getPath().equals(lastSong.getPath())) {
                songList.get(i).setChoosed(false);
                lastPosition = i;
            }

            if(songFromList.getPath().equals(song.getPath())){
                songList.get(i).setChoosed(true);
                position = i;
            }
        }
        musicPresenter.setSongArrayList(songList);
        recyclerAdapter.notifyItemChanged(position);
        recyclerAdapter.notifyItemChanged(lastPosition);
    }

    @Override
    public void notifyItemDeleted(int position) {
        songList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }

}
