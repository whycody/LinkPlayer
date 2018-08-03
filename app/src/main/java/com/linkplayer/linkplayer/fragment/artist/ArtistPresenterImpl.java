package com.linkplayer.linkplayer.fragment.artist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

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
        artistRecyclerHolder.setArtistTitle(songList.getTitle());
        artistRecyclerHolder.setSongsNumber("Songs: " + songList.getSongList().size());
        artistRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songListDao.changeLatestSongList(songList);
                sendToNextActivity();
            }
        });
    }

    private void sendToNextActivity(){
        Intent intent = new Intent(activity, PlaylistViewActivity.class);
        intent.putExtra("type", PlaylistViewActivity.ARTIST_TYPE);
        activity.startActivityForResult(intent, 1);

    }

    public int getArtistRowsCount(){
        return songListArrayList.size();
    }
}
