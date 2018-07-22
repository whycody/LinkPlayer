package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;

public class MusicPresenterImpl{

    private ArrayList<Song> songList;
    private Context context;
    private MusicFragmentView fragmentView;

    public MusicPresenterImpl(ArrayList<Song> songList, MusicFragmentView fragmentView, Context context){
        this.songList = songList;
        this.context = context;
        this.fragmentView = fragmentView;
    }

    public void onBindSongRowViewAtPosition(MusicRecyclerHolder musicRecyclerHolder, final int position){
        Song song = songList.get(position);
        musicRecyclerHolder.setTitle(song.getTitle());
        musicRecyclerHolder.setAuthor(song.getArtist());
        if(song.isAlbumPhotoAvailable()) {
            musicRecyclerHolder.setImage(song.getArt());
        }else
            musicRecyclerHolder.setImage(context.getResources().getDrawable(R.drawable.gray_color));
        musicRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragmentView.playMusic(position);
            }
        });
    }

    public int getSongRowsCount(){
        return songList.size();
    }

}
