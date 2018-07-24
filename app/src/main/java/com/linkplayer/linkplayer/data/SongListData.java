package com.linkplayer.linkplayer.data;

import android.content.Context;

import com.linkplayer.linkplayer.model.PlaylistList;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;
import java.util.Collections;

public class SongListData {

    private Context context;

    public SongListData(Context context){
        this.context = context;
    }

    public ArrayList<SongList> getArtistList(){
        ArrayList<SongList> songListArrayList = new ArrayList<>();
        ArrayList<Song> songArrayList = new MusicListData(context).getSongList();
        boolean added = false;

        for(Song song: songArrayList){
            added = false;
            if(songListArrayList.size()!=0){
                for(SongList songList : songListArrayList){
                    if(songList.getTitle().equals(song.getArtist())){
                        songList.addSong(song);
                        added = true;
                    }
                }
                if(!added){
                    SongList songList = new SongList();
                    songList.setTitle(song.getArtist());
                    songList.addSong(song);
                    songListArrayList.add(songList);
                }
            }else{
                SongList songList = new SongList();
                songList.setTitle(song.getArtist());
                songList.addSong(song);
                songListArrayList.add(songList);
            }
        }
        Collections.sort(songListArrayList);
        Collections.reverse(songListArrayList);
        return songListArrayList;
    }
}
