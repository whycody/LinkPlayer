package com.linkplayer.linkplayer.fragment.artist;

import android.content.Context;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class ArtistPresenterImpl {

    private ArrayList<SongList> songListArrayList;
    private ArtistFragmentView fragmentView;
    private Context context;
    private SongListDao songListDao;
    private int lastSong;

    public ArtistPresenterImpl(ArrayList<SongList> songListArrayList, ArtistFragmentView fragmentView, Context context){
        this.songListArrayList = songListArrayList;
        this.fragmentView = fragmentView;
        this.context = context;
        songListDao = new SongListDao(context);
    }

    public void onBindSongRowViewAtPosition(ArtistRecyclerHolder artistRecyclerHolder, final int position){
        final SongList songList = songListArrayList.get(position);
        if(songListDao.getLatestSongList().getTitle().equals(songList.getTitle())
                && songListDao.getLatestSongList().getSongList().size() == songList.getSongList().size()) {
            artistRecyclerHolder.setBackground(context.getResources().getDrawable(R.drawable.gray_color));
            this.lastSong = position;
        } else {
            artistRecyclerHolder.setBackground(context.getResources().getDrawable(R.drawable.gray_row_color));
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
