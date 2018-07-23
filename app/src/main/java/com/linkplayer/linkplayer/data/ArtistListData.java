package com.linkplayer.linkplayer.data;

import android.content.Context;

import com.linkplayer.linkplayer.model.ArtistList;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArtistListData {

    private Context context;

    public ArtistListData(Context context){
        this.context = context;
    }

    public ArrayList<ArtistList> getArtistList(){
        ArrayList<ArtistList> artistListArrayList = new ArrayList<>();
        ArrayList<Song> songList = new MusicListData(context).getSongList();
        boolean added = false;

        for(Song song: songList){
            added = false;
            if(artistListArrayList.size()!=0){
                for(ArtistList artistList: artistListArrayList){
                    if(artistList.getArtist().equals(song.getArtist())){
                        artistList.addSong(song);
                        added = true;
                    }
                }
                if(!added){
                    ArtistList artistList = new ArtistList();
                    artistList.setArtist(song.getArtist());
                    artistList.addSong(song);
                    artistListArrayList.add(artistList);
                }
            }else{
                ArtistList artistList = new ArtistList();
                artistList.setArtist(song.getArtist());
                artistList.addSong(song);
                artistListArrayList.add(artistList);
            }
        }
        Collections.sort(artistListArrayList);
        Collections.reverse(artistListArrayList);
        return artistListArrayList;
    }
}
