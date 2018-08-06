package com.linkplayer.linkplayer.fragment.artist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

import java.util.ArrayList;

public class ArtistPresenterImpl {

    private ArrayList<SongList> artistSongList;
    private ArtistFragmentView fragmentView;
    private Activity activity;
    private SongListDao songListDao;
    private int lastSong;

    public ArtistPresenterImpl(ArrayList<SongList> artistSongList, ArtistFragmentView fragmentView, Activity activity){
        this.artistSongList = artistSongList;
        this.fragmentView = fragmentView;
        this.activity = activity;
        songListDao = new SongListDao(activity);
    }

    public void onBindSongRowViewAtPosition(ArtistRecyclerHolder artistRecyclerHolder, final int position){
        final SongList songList = artistSongList.get(position);
        artistRecyclerHolder.setArtistTitle(songList.getTitle());
        artistRecyclerHolder.setSongsNumber("Songs: " + songList.getSongList().size());
        artistRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNextActivity(position);
            }
        });
    }

    private void sendToNextActivity(int position){
        Intent intent = new Intent(activity, PlaylistViewActivity.class);
        intent.putExtra("type", PlaylistViewActivity.ARTIST_TYPE);
        intent.putExtra("artist", artistSongList.get(position).getTitle());
        activity.startActivityForResult(intent, 1);

    }

    public int getArtistRowsCount(){
        return artistSongList.size();
    }

    public ArrayList<SongList> getArtistSongList() {
        return artistSongList;
    }
}
