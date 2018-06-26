package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.widget.Toast;

import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;

public class MusicPresenter {

    private ArrayList<Song> songList;
    private Context context;

    public MusicPresenter(ArrayList<Song> songList, Context context){
        this.songList = songList;
        this.context = context;
    }

    public void onBindSongRowViewAtPosition(MusicRecyclerHolder musicRecyclerHolder, int position){
        Song song = songList.get(position);
        musicRecyclerHolder.setTitle(song.getTitle());
        musicRecyclerHolder.setAuthor(song.getArtist());
        if(song.isAlbumPhotoAvailable()) {
            musicRecyclerHolder.setImage(song.getArt());
        }
        Toast.makeText(context, position + " " + song.isAlbumPhotoAvailable(), Toast.LENGTH_SHORT).show();
    }

    public int getSongRowsCount(){
        return songList.size();
    }
}
