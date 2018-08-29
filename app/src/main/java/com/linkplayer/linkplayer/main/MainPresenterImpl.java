package com.linkplayer.linkplayer.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SongDao;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.artist.ArtistFragment;
import com.linkplayer.linkplayer.fragment.music.MusicFragment;
import com.linkplayer.linkplayer.fragment.now.NowFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistFragment;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainPresenterImpl implements MainPresenter{

    private Context context;
    private MediaPlayerService musicService;
    private MainView mainView;

    private final String PREFERENCES = "preferences";
    private boolean random = false;
    private boolean repeat = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferencesEditor;
    private MusicListData musicListData;
    private SongDao songDao;
    private SongListDao songListDao;
    private ArrayList<Song> songList;
    private ArrayList<Song> shuffledSongList;
    private ArrayList<SongList> artisSongLists;
    private ArrayList<SongList> playlistSongLists;


    public MainPresenterImpl(Context context, MainView mainView){
        this.context = context;
        this.mainView = mainView;
        songDao = new SongDao(context);
        songListDao = new SongListDao(context);
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        musicListData = new MusicListData(context);
        songList = songListDao.getLatestSongList().getSongList();
        shuffledSongList = songListDao.getLatestSongList().getSongList();
        Collections.shuffle(shuffledSongList);
        artisSongLists = musicListData.getArtistList();
        playlistSongLists = songListDao.getAllTheSongLists();

    }

    @Override
    public void getPreferencesAndSetButtons() {
        random = sharedPreferences.getBoolean("random", false);
        repeat = sharedPreferences.getBoolean("repeat", false);

        mainView.setSettings(random, repeat);
        saveRandomPreferences(random);
        saveRepeatReferences(repeat);
    }

    @Override
    public void saveRandomPreferences(boolean random){
        songList = songListDao.getLatestSongList().getSongList();
        if(musicService.getSongList()==null)
            musicService.setLists(songList, true);
        if(random) {
            musicService.setLists(songList, true);
            musicService.setSongPos(musicService.getTargetRandomSongTruePosition(musicService.getSongPos()));
            mainView.showRandomIsChosed();
        }else {
            musicService.setSongPos(musicService.getRandomSongTruePosition(musicService.getSongPos()));
            musicService.setLists(songList, false);
            mainView.showRandomIsNotChosed();
        }
        musicService.setOptionsRandomRepeat(random, repeat);
        saveSettingsInPreferences(random, repeat);
    }

    @Override
    public void saveRepeatReferences(boolean repeat){
        if(repeat){
            mainView.showRepeatIsChosed();
        }else {
            mainView.showRepeatIsNotChosed();
        }
        musicService.setOptionsRandomRepeat(random, repeat);
        saveSettingsInPreferences(random, repeat);
    }

    @Override
    public String getTitle() {
        String title = songDao.getLatestMusic().getTitle();
        String author = songDao.getLatestMusic().getArtist();
        if (!author.equals("<unknown>")) {
            return author + " - " + title;
        } else {
            return title;
        }
    }

    @Override
    public int getLatestSong() {
        for(int i =0; i<songList.size(); i++){
            if(songList.get(i).getId()==songDao.getLatestMusic().getId()){
                return i;
            }
        }
        return 0;
    }


    @Override
    public void saveSettingsInPreferences(boolean random, boolean repeat) {
        preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean("random", random);
        preferencesEditor.putBoolean("repeat", repeat);
        preferencesEditor.apply();
    }

    @Override
    public void setMusicService(MediaPlayerService musicService){
        this.musicService = musicService;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            String type = "none";
            SongList songList = null;
            if(resultCode == Activity.RESULT_OK){
                notifyMusicFragment();
                type = data.getStringExtra("type");
                songList = getSongListByType(type, data);
                songListDao.changeLatestSongList(songList);

                refreshNowFragment(data.getIntExtra("position", 0), songList);
                musicService.setLists(songList.getSongList(), random);

                if(random)
                    musicService.setTargetRandomSong(data.getIntExtra("position", 0));
                else
                    musicService.setSongPosAndNotify(data.getIntExtra("position", 0));
                musicService.playSong();
                mainView.setPagerCurrentItem(3);
                mainView.showIsPlaying();
            }

            notifyFragments(type, songList);
        }
    }

    private void notifyMusicFragment() {
        MusicFragment musicFragment = mainView.getMusicFragment();
        musicFragment.recreateList();
    }

    private void notifyFragments(String type, SongList songList){
        switch (type) {
            case PlaylistViewActivity.ARTIST_TYPE:
                notifyArtistFragment(songList);
                break;
            case PlaylistViewActivity.PLAYLIST_TYPE:
                notifyPlaylistFragment(songList);
                break;
            default:
                notifyAllData();
                break;
        }
    }

    private void notifyArtistFragment(SongList songList){
        int position = 0;
        ArtistFragment artistFragment = mainView.getArtistFragment();
        if(artistFragment!=null) {
            for (int i = 0; i < artisSongLists.size(); i++) {
                if (artisSongLists.get(i).getTitle().equals(songList.getTitle()))
                    position = i;
            }
            artistFragment.notifyItemChanged(position, songList);
        }
    }

    private void notifyPlaylistFragment(SongList songList) {
        int position = 0;
        PlaylistFragment playlistFragment = mainView.getPlaylistFragment();
        if(playlistFragment!=null) {
            for (int i = 0; i < playlistSongLists.size(); i++) {
                if (playlistSongLists.get(i).getKey() == songList.getKey())
                    position = i;
            }
            playlistFragment.notifyItemChanged(songList, position);
        }
    }

    @Override
    public void notifyAllData() {
        notifyArtistFragmentWithoutPosition();
        notifyPlaylistFragmentWithoutPosition();
    }

    private void notifyArtistFragmentWithoutPosition(){
        ArtistFragment artistFragment = mainView.getArtistFragment();
        artisSongLists = musicListData.getArtistList();
        if(artistFragment!=null) {
            for (int i = 0; i < artisSongLists.size(); i++) {
                artistFragment.notifyItemChanged(i, artisSongLists.get(i));
            }
        }
    }

    private void notifyPlaylistFragmentWithoutPosition(){
        PlaylistFragment playlistFragment = mainView.getPlaylistFragment();
        playlistSongLists = songListDao.getAllTheSongLists();
        if(playlistFragment!=null) {
            for (int i = 0; i < playlistSongLists.size(); i++) {
                playlistFragment.notifyItemChanged(playlistSongLists.get(i), i);
            }
        }
    }

    private SongList getSongListByType(String type, Intent data){
        SongList songList = null;
        switch(type){
            case(PlaylistViewActivity.ARTIST_TYPE):
                songList = getArtistSongList(data.getStringExtra("artist"));
                break;
            case(PlaylistViewActivity.PLAYLIST_TYPE):
                songList = getPlaylistSongList(data.getIntExtra("key", 0));
                break;
        }
        return songList;
    }

    private SongList getArtistSongList(String artist){
        return new MusicListData(context).getArtistSongList(artist);
    }

    private SongList getPlaylistSongList(int key){
        return songListDao.getSongListWithKey(key);
    }

    private void refreshNowFragment(int position, SongList songList){
        NowFragment nowFragment = mainView.getNowFragment();
        if(nowFragment!=null) {
            nowFragment.refresh();
        }
    }



}
