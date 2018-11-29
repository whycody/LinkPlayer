package com.linkplayer.linkplayer.fragment.artist;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.main.MainPresenterImpl;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

import java.util.ArrayList;

public class ArtistPresenterImpl {

    private ArrayList<SongList> artistSongList;
    private Activity activity;

    public ArtistPresenterImpl(ArrayList<SongList> artistSongList, ArtistFragmentView fragmentView, Activity activity){
        this.artistSongList = artistSongList;
        this.activity = activity;
    }

    public void onBindSongRowViewAtPosition(ArtistRecyclerHolder artistRecyclerHolder, final int position){
        final SongList songList = artistSongList.get(position);
        String songsNumber = activity.getString(R.string.songs) + ": " + songList.getSongList().size();
        artistRecyclerHolder.setArtistTitle(songList.getTitle());
        artistRecyclerHolder.setSongsNumber(songsNumber);
        artistRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNextActivity(position);
            }
        });
    }

    private void sendToNextActivity(int position){
        Intent intent = new Intent(activity, PlaylistViewActivity.class);
        intent.putExtra(MainPresenterImpl.TYPE, PlaylistViewActivity.ARTIST_TYPE);
        intent.putExtra(MainPresenterImpl.ARTIST, artistSongList.get(position).getTitle());
        activity.startActivityForResult(intent, 1);
    }

    public int getArtistRowsCount(){
        return artistSongList.size();
    }

    public void setArtistSongList(ArrayList<SongList> artistSongList){
        this.artistSongList = artistSongList;
    }

    public ArrayList<SongList> getArtistSongList() {
        return artistSongList;
    }
}
