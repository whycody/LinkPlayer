package com.linkplayer.linkplayer;

import android.app.Notification;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.linkplayer.linkplayer.data.SongDao;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.main.RefreshView;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;
import java.util.Collections;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener{

    private final IBinder iBinder = new LocalBinder();
    private MediaPlayer player;
    private ArrayList<Song> songList;
    private ArrayList<Song> notShuffled;
    private SongDao songDao = new SongDao(getBaseContext());
    boolean repeat = false;
    boolean random = false;
    private RefreshView refreshView;

    private int songPos;
    private int currentSong;
    private static final int NOTIFY_ID=1;

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
    public void onDestroy() {
        stopForeground(true);
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
            setSongPosAndNotify(0);
        }else
            setSongPosAndNotify(songPos + 1);
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification myNotification  = new Notification.Builder(getApplicationContext())
                .setContentTitle(songList.get(songPos).getTitle())
                .setContentText(getNextSong().getTitle())
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.play_icon_white)
                .setContentIntent(pendInt)
                .setAutoCancel(false)
                .build();

        startForeground(NOTIFY_ID, myNotification);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    private int lastSongPoisition;

    public void setSongPosAndNotify(int songPos){
        this.songPos = songPos;
        if(songList!=null && refreshView !=null) {
            notifyItemChanged(songPos);
        }
    }

    public void setSongPos(int songPos){
        this.songPos = songPos;
        this.lastSongPoisition = songPos;
    }

    public void notifyItemChanged(int position){
        if (lastSongPoisition < getSongList().size())
            refreshView.notifyItemChanged(lastSongPoisition, position);
        else
            refreshView.notifyItemChanged(0, position);

        lastSongPoisition = songPos;
    }

    public void setTargetRandomSong(int songPos){
        setSongPosAndNotify(getTargetRandomSongTruePosition(songPos));
    }

    public int getRandomSongTruePosition(int position) {
        for(int i =0; i<songList.size(); i++){
            if(songList.get(position).getId()==notShuffled.get(i).getId()){
                return i;
            }
        }
        return 0;
    }

    public int getTargetRandomSongTruePosition(int position) {
        for(int i =0; i<songList.size(); i++){
            if(songList.get(i).getId()==notShuffled.get(position).getId()){
                return i;
            }
        }
        return 0;
    }

    public int getSongPos(){
        return songPos;
    }

    public void playSong(){
        try{
            player.reset();
            Song playSong = songList.get(songPos);
            currentSong = songPos;
            setLastSong();
            long currSong = playSong.getId();
            Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
            player.setDataSource(getApplicationContext(), trackUri);
        }catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public int getLatestSongPosition(){
        return lastSongPoisition;
    }

    public void setRefreshView(RefreshView refreshView){
        this.refreshView = refreshView;
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

    public Song getNextSong(){
        if(songPos < songList.size()-1)
            return songList.get(songPos+1);
        else
            return songList.get(0);
    }

    public void playNextSong(){
        if(songPos < songList.size()-1) {
            setSongPosAndNotify(songPos+1);
            setLastSong();
        }else if(songPos == songList.size()-1){
            setSongPosAndNotify(0);
            if(random) {
                Song song = songList.get(0);
                Collections.shuffle(songList);
                int position = getPositionOfSong(song);
                if (position > 1)
                    Collections.swap(songList, getPositionOfSong(song), 0);
            }
            setLastSong();
        }
        if(player.isPlaying())
            playSong();
    }

    private int getPositionOfSong(Song song){
        for(int i =0; i<songList.size(); i++){
            Song songFromList = songList.get(i);
            if(songFromList.getPath().equals(song.getPath()))
                return i;
        }
        return 0;
    }

    public void playPreviousSong(){
        if(songPos!=0) {
            setSongPosAndNotify(songPos-1);
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
        setSongPosAndNotify(0);
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

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public Song getSong(){
        return songList.get(songPos);
    }

    public void setList(ArrayList<Song> songList){
        this.songList = songList;
    }

    public void setNotShuffledList(ArrayList<Song> notShuffled){
        this.notShuffled = notShuffled;
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
