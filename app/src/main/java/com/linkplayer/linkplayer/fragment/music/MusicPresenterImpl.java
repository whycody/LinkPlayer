package com.linkplayer.linkplayer.fragment.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;

public class MusicPresenterImpl implements MusicPresenter{

    private ArrayList<Song> songList;
    private Context context;
    private MediaPlayerService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    public MusicPresenterImpl(ArrayList<Song> songList, Context context){
        this.songList = songList;
        this.context = context;
    }

    public void onBindSongRowViewAtPosition(MusicRecyclerHolder musicRecyclerHolder, final int position){
        Song song = songList.get(position);
        musicRecyclerHolder.setTitle(song.getTitle());
        musicRecyclerHolder.setAuthor(song.getArtist());
        if(song.isAlbumPhotoAvailable()) {
            musicRecyclerHolder.setImage(song.getArt());
        }else {
            musicRecyclerHolder.setImage(context.getResources().getDrawable(R.drawable.gray_color));
        }
        musicRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.setSong(position);
                musicService.playSong();
            }
        });
    }

    public int getSongRowsCount(){
        return songList.size();
    }

    @Override
    public void onStart() {
        if(playIntent==null){
            playIntent = new Intent(context, MediaPlayerService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder musicBinder = (MediaPlayerService.LocalBinder) service;
            musicService = musicBinder.getService();
            musicService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
}
