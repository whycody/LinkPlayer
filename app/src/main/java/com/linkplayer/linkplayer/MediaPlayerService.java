package com.linkplayer.linkplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.linkplayer.linkplayer.data.SongDao;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;
import java.util.Collections;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener{

    private final IBinder iBinder = new LocalBinder();
    private MediaPlayer player;
    private ArrayList<Song> songList;
    private SongDao songDao = new SongDao(getBaseContext());
    boolean repeat = false;
    boolean random = false;

    private int songPos;
    private int currentSong;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.reset();
        player.release();
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(songPos < songList.size()-1 || repeat) {
            playNextSongAutomatically();
        }else if(songPos == songList.size()-1 && repeat && !random){
            playNextSongAutomatically();
        }else if(songPos == songList.size()-1 && repeat && random){
            Collections.shuffle(songList);
            playNextSongAutomatically();
        }
    }

    private void playNextSongAutomatically(){
        if(songPos == songList.size()-1 && repeat){
            songPos = 0;
        }else
            songPos++;
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public void setSong(int songPos){
        this.songPos = songPos;
    }

    public void playSong(){
        player.reset();
        Song playSong = songList.get(songPos);
        currentSong = songPos;
        setLastSong();
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void pauseSong(){
        player.pause();
    }

    public void resumeSong(){
        if(currentSong == songPos) {
            player.start();
        }else
            playSong();
    }

    public void playNextSong(){
        if(songPos < songList.size()-1) {
            songPos++;
            setLastSong();
        }
        if(player.isPlaying())
            playSong();
    }

    public void playLastSong(){
        if(songPos!=0) {
            songPos--;
            setLastSong();
        }
        if(player.isPlaying())
            playSong();
    }

    private void setLastSong(){
        Song playSong = songList.get(songPos);
        songDao.changeLatestMusic(playSong);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPos = 0;
        player = new MediaPlayer();
        initMediaPlayer();
    }

    public void initMediaPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songList){
        this.songList = songList;
    }

    public void setOptionsRandomRepeat(boolean random, boolean repeat){
        this.random = random;
        this.repeat = repeat;
    }

    public class LocalBinder extends Binder{
        public MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }
}
