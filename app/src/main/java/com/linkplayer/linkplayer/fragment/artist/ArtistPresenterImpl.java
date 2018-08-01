package com.linkplayer.linkplayer.fragment.artist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.artist.activity.ArtistActivity;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class ArtistPresenterImpl {

    private ArrayList<SongList> songListArrayList;
    private ArtistFragmentView fragmentView;
    private Activity activity;
    private SongListDao songListDao;
    private int lastSong;

    public ArtistPresenterImpl(ArrayList<SongList> songListArrayList, ArtistFragmentView fragmentView, Activity activity){
        this.songListArrayList = songListArrayList;
        this.fragmentView = fragmentView;
        this.activity = activity;
        songListDao = new SongListDao(activity);
    }

    public void onBindSongRowViewAtPosition(ArtistRecyclerHolder artistRecyclerHolder, final int position){
        final SongList songList = songListArrayList.get(position);
        if(songListDao.getLatestSongList().getTitle().equals(songList.getTitle())
                && songListDao.getLatestSongList().getSongList().size() == songList.getSongList().size()) {
            artistRecyclerHolder.setBackground(activity.getResources().getDrawable(R.drawable.gray_color));
            this.lastSong = position;
        } else {
            artistRecyclerHolder.setBackground(activity.getResources().getDrawable(R.drawable.gray_row_color));
        }
        artistRecyclerHolder.setArtistTitle(songList.getTitle());
        artistRecyclerHolder.setSongsNumber("Songs: " + songList.getSongList().size());
        artistRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentView.notifyItemChanged(lastSong, position);
                songListDao.changeLatestSongList(songList);
            }
        });
    }

    public int getArtistRowsCount(){
        return songListArrayList.size();
    }
}
