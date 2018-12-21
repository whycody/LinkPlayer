package com.linkplayer.linkplayer.fragment.music;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongInformator;
import com.linkplayer.linkplayer.dialog.fragments.NewPlaylistInformator;
import com.linkplayer.linkplayer.fragment.now.NowFragment;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;


public class MusicFragment extends Fragment implements MusicFragmentView, DeleteSongInformator, NewPlaylistInformator{

    private RecyclerView musicListRecycler;
    private MusicPresenterImpl musicPresenter;
    private MusicRecyclerAdapter recyclerAdapter;
    private SongListDao songListDao;
    private ArrayList<Song> songList;
    private MusicListData musicListData;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        musicListRecycler = view.findViewById(R.id.music_list_recycler);
        musicListData = new MusicListData(getActivity());
        songList = musicListData.getSongList();
        musicPresenter = new MusicPresenterImpl(songList, this, getActivity());
        musicPresenter.setInformators(this, this);
        songListDao = new SongListDao(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerAdapter = new MusicRecyclerAdapter(musicPresenter, getActivity());
        musicListRecycler.setAdapter(recyclerAdapter);
        musicListRecycler.setLayoutManager(linearLayoutManager);
        musicListRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        if(!songListDao.getLatestSongList().getTitle().equals(MusicListData.ALL_MUSIC_SONGLIST)) {
            uncheckItem();
            SongList songList = musicListData.getAllMusicSongList();
            songListDao.changeLatestSongList(songList);
            ((MainActivity)getActivity()).refreshService(songList);
        }
        ((MainActivity)getActivity()).playSong(position);
    }

    private void uncheckItem(){
        int choosed = getSongChoosed();
        recyclerAdapter.getSongArrayList().get(choosed).setChoosed(false);
        recyclerAdapter.notifyItemChanged(choosed);
    }

    private int getSongChoosed(){
        for(int i =0; i<songList.size(); i++){
            if(songList.get(i).isChoosed())
                return i;
        }
        return 0;
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position){

    }

    @Override
    public void notifyItemChanged(Song lastSong, Song song){
        int lastPosition =0, position = 0;
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
    }

    @Override
    public void notifySongAddedToPlaylist() {
        ((MainActivity)getActivity()).notifySongAddedToPlaylist();
    }

    @Override
    public void notifyItemDeleted(int position) {
        NowFragment nowFragment = ((MainActivity)getActivity()).getNowFragment();
        nowFragment.refreshData();
        ((MainActivity)getActivity()).notifyAllData(nowFragment.getPosition(), songList.get(position));
        songList.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
        recyclerAdapter.notifyItemRangeChanged(position, songList.size());
    }

    public void refreshData(){
        songList = musicListData.getSongList();
        recyclerAdapter.setSongArrayList(songList);
        recyclerAdapter.notifyDataSetChanged();
    }

    public void refreshData(ArrayList<Song> songList){
        this.songList = songList;
        recyclerAdapter.setSongArrayList(songList);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifySongDeleted(int position, boolean deleted) {
        if(deleted)
            notifyItemDeleted(position);
    }

    @Override
    public void notifyNewPlaylistAdded(boolean added) {
        if(added)
            ((MainActivity)getActivity()).notifySongAddedToPlaylist();
    }
}
