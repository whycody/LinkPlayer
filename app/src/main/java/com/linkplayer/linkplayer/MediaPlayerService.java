package com.linkplayer.linkplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.linkplayer.linkplayer.data.SongDao;
import com.linkplayer.linkplayer.main.MainActivity;
import com.linkplayer.linkplayer.main.RefreshView;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;
import java.util.Collections;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener{

    private ArrayList<Song> songList, notShuffledList, shuffledList;
    private MediaPlayer mediaPlayer;
    private RefreshView refreshView;
    private SongDao songDao;
    private IBinder iBinder;

    private int songPos, currentSong;
    boolean repeat, random = false;
    private static final int NOTIFY_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        iBinder = new LocalBinder();
        mediaPlayer = new MediaPlayer();
        songDao = new SongDao(getBaseContext());
        setSongPosAndNotifyActivity(0);
        initMediaPlayer();
    }

    public void initMediaPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

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
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
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
        if(songPos == songList.size()-1 && repeat && random)
            Collections.shuffle(songList);
        if(songPos<songList.size()-1 || repeat)
            playNextSongAutomatically();
        else
            refreshView.notifyTheLastSongPlayed();
    }

    private void playNextSongAutomatically(){
        if(songPos == songList.size()-1 && repeat){
            setSongPosAndNotifyActivity(0);
        }else
            setSongPosAndNotifyActivity(songPos + 1);
        playSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            buildNotification();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "my.app";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(getResources().getColor(R.color.colorPrimary));
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.play_icon_white)
                .setContentTitle(songList.get(songPos).getTitle())
                .setContentText(getNextSong().getTitle())
                .setContentIntent(getNotificationPendingIntent())
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(false)
                .build();
        startForeground(2, notification);
    }

    private void buildNotification(){
        PendingIntent notificationIntent = getNotificationPendingIntent();
        Notification musicNotification  = musicNotification(notificationIntent);
        startForeground(NOTIFY_ID, musicNotification);
    }

    private PendingIntent getNotificationPendingIntent(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification musicNotification(PendingIntent notificationIntent){
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(songList.get(songPos).getTitle())
                .setContentText(getNextSong().getTitle())
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.play_icon_white)
                .setContentIntent(notificationIntent)
                .setAutoCancel(false)
                .build();
    }

    public Song getNextSong(){
        if(songPos < songList.size()-1)
            return songList.get(songPos+1);
        else
            return songList.get(0);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            pauseSong();
        }
        else if(focusChange == AudioManager.AUDIOFOCUS_GAIN)
        {
            resumeSong();
        }
        else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)
        {
            pauseSong();
        }
    }

    private int lastSongPoisition;

    public void setSongPosAndNotifyActivity(int songPos){
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
            refreshView.notifySongChanged(lastSongPoisition, position);
        else
            refreshView.notifySongChanged(0, position);
        lastSongPoisition = songPos;
    }

    public void setTargetRandomSongTruePosition(int songPos){
        setSongPosAndNotifyActivity(getTargetRandomSongTruePosition(songPos));
    }

    public int getRandomSongTruePosition(int position) {
        for(int i =0; i<songList.size(); i++){
            if(songList.get(position).getId()== notShuffledList.get(i).getId()){
                return i;
            }
        }
        return 0;
    }

    public int getTargetRandomSongTruePosition(int position) {
        for(int i =0; i<songList.size(); i++){
            if(songList.get(i).getId()== notShuffledList.get(position).getId()){
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
            mediaPlayer.reset();
            currentSong = songPos;
            Song playSong = songList.get(songPos);
            long currentSong = playSong.getId();
            saveSongAsLast();
            Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSong);
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
        }catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        mediaPlayer.prepareAsync();
    }

    public void setRefreshView(RefreshView refreshView){
        this.refreshView = refreshView;
    }

    public void pauseSong(){
        mediaPlayer.pause();
    }

    public void resumeSong(){
        if(currentSong == songPos)
            mediaPlayer.start();
        else
            playSong();
    }

    public void playNextSong(){
        if(songPos < songList.size()-1) {
            setSongPosAndNotifyActivity(songPos+1);
            saveSongAsLast();
        }else if(songPos == songList.size()-1){
            setSongPosAndNotifyActivity(0);
            shuffleListIfRandom();
            saveSongAsLast();
        }
        if(mediaPlayer.isPlaying())
            playSong();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void seekTo(int time){
        mediaPlayer.seekTo(time);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    private void shuffleListIfRandom(){
        if(random) {
            Song song = songList.get(0);
            Collections.shuffle(songList);
            int position = getRealPositionOfSong(song);
            if (position > 1)
                Collections.swap(songList, getRealPositionOfSong(song), 0);
        }
    }

    private int getRealPositionOfSong(Song song){
        for(int i =0; i<songList.size(); i++){
            Song songFromList = songList.get(i);
            if(songFromList.getPath().equals(song.getPath()))
                return i;
        }
        return 0;
    }

    public void playPreviousSong(){
        if(songPos!=0) {
            setSongPosAndNotifyActivity(songPos-1);
            saveSongAsLast();
        }
        if(mediaPlayer.isPlaying())
            playSong();
    }

    private void saveSongAsLast(){
        Song playSong = songList.get(songPos);
        songDao.changeLatestSong(playSong);
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public Song getSong(){
        return songList.get(songPos);
    }

    public void setLists(ArrayList<Song> songList, boolean random){
        notShuffledList = (ArrayList<Song>)songList.clone();
        shuffledList = (ArrayList<Song>)songList.clone();
        Collections.shuffle(shuffledList);
        if(random)
            this.songList = shuffledList;
        else
            this.songList = notShuffledList;
    }

    public void setOptionsRandomRepeat(boolean random, boolean repeat){
        setRandom(random);
        setRepeat(repeat);
    }

    public void setRandom(boolean random){
        this.random = random;
    }

    public void setRepeat(boolean repeat){
        this.repeat = repeat;
    }

    public ArrayList<Song> getNotShuffledList() {
        return notShuffledList;
    }

    public ArrayList<Song> getShuffledList() {
        return shuffledList;
    }

    public class LocalBinder extends Binder{
        public MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }
}
