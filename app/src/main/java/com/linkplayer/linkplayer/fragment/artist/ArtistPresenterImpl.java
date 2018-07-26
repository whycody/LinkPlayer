package com.linkplayer.linkplayer.fragment.artist;

import android.content.Context;
import android.view.View;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class ArtistPresenterImpl {

    private ArrayList<SongList> songListArrayList;
    private Context context;
    private SongListDao songListDao;

    public ArtistPresenterImpl(ArrayList<SongList> songListArrayList, Context context){
        this.songListArrayList = songListArrayList;
        this.context = context;
        songListDao = new SongListDao(context);
    }

    public void onBindSongRowViewAtPosition(ArtistRecyclerHolder artistRecyclerHolder, final int position){
        final SongList songList = songListArrayList.get(position);
        artistRecyclerHolder.setArtistTitle(songList.getTitle());
        artistRecyclerHolder.setSongsNumber("Songs: " + songList.getSongList().size());
        artistRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songListDao.changeLatestSongList(songList);
            }
        });
    }

    public int getArtistRowsCount(){
        return songListArrayList.size();
    }
}
