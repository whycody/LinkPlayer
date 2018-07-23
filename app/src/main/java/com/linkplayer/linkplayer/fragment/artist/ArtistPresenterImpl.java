package com.linkplayer.linkplayer.fragment.artist;

import android.content.Context;

import com.linkplayer.linkplayer.model.ArtistList;

import java.util.ArrayList;

public class ArtistPresenterImpl {

    private ArrayList<ArtistList> artistLists;
    private Context context;

    public ArtistPresenterImpl(ArrayList<ArtistList> artistLists, Context context){
        this.artistLists = artistLists;
        this.context = context;
    }

    public void onBindSongRowViewAtPosition(ArtistRecyclerHolder artistRecyclerHolder, final int position){
        ArtistList artistList = artistLists.get(position);
        artistRecyclerHolder.setArtistTitle(artistList.getArtist());
        artistRecyclerHolder.setSongsNumber("Songs: " + artistList.getSongList().size());
    }

    public int getArtistRowsCount(){
        return artistLists.size();
    }
}
