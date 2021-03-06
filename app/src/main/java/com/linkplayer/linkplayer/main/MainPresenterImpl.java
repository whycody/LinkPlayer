package com.linkplayer.linkplayer.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SharedPrefDao;
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

public class MainPresenterImpl implements MainPresenter{

    private boolean random, repeat = false;
    private final String RANDOM = "random";
    private final String REPEAT = "repeat";
    public static final String TYPE = "type";
    public static final String KEY = "key";
    public static final String ARTIST = "artist";
    public static final String POSITION = "position";

    private Activity activity;
    private MediaPlayerService musicService;
    private MainView mainView;
    private SharedPrefDao sharedPrefDao;
    private ArrayList<Song> songList;
    private ArrayList<SongList> artistSongLists, playlistSongLists;
    private SongListDao songListDao;
    private SongDao songDao;

    private MusicFragment musicFragment;
    private ArtistFragment artistFragment;
    private PlaylistFragment playlistFragment;
    private NowFragment nowFragment;


    public MainPresenterImpl(Activity activity, MainView mainView){
        this.activity = activity;
        this.mainView = mainView;
        songDao = new SongDao(activity);
        sharedPrefDao = new SharedPrefDao(activity);
        songListDao = new SongListDao(activity);
        songList = songListDao.getLatestSongList().getSongList();
        artistSongLists = new MusicListData(activity).getArtistList();
        playlistSongLists = songListDao.getAllTheSongLists();
    }

    @Override
    public void getPreferencesAndSetButtons() {
        random = sharedPrefDao.getBooleanValue(RANDOM);
        repeat = sharedPrefDao.getBooleanValue(REPEAT);

        mainView.setSettings(random, repeat);
        saveRandomPreferences(random);
        saveRepeatReferences(repeat);
    }

    @Override
    public void saveRandomPreferences(boolean random){
        this.random = random;
        songList = songListDao.getLatestSongList().getSongList();
        if(musicService.getSongList()==null)
            musicService.setLists(songList, true);
        if(random) {
            musicService.setLists(songList, true);
            musicService.setSongPos(musicService.getTargetRandomSongTruePosition(musicService.getSongPos()));
        }else {
            musicService.setSongPos(musicService.getRandomSongTruePosition(musicService.getSongPos()));
            musicService.setLists(songList, false);
        }
        mainView.setTitleAndSong();
        musicService.setRandom(random);
        mainView.showRandomIsActive(random);
        saveRandomSettingInPreferences(random);
    }

    @Override
    public void saveRepeatReferences(boolean repeat){
        this.repeat = repeat;
        mainView.showRepeatIsActive(repeat);
        musicService.setRepeat(repeat);
        saveRepeatSettingInPreferences(repeat);
    }

    @Override
    public String getTitle() {
        String title = songDao.getLatestSong().getTitle();
        String author = songDao.getLatestSong().getArtist();
        return author.equals("<unkown>") ? author + " - " + title : title;
    }

    @Override
    public int getLatestSong() {
        for(int i =0; i<songList.size(); i++){
            if(songList.get(i).getId()==songDao.getLatestSong().getId())
                return i;
        }
        return 0;
    }

    @Override
    public void saveSettingsInPreferences(boolean random, boolean repeat) {
        saveRandomSettingInPreferences(random);
        saveRepeatSettingInPreferences(repeat);
    }

    @Override
    public void initializeFragments() {
        musicFragment = mainView.getMusicFragment();
        artistFragment = mainView.getArtistFragment();
        playlistFragment = mainView.getPlaylistFragment();
        nowFragment = mainView.getNowFragment();
    }

    private void saveRandomSettingInPreferences(boolean random){
        sharedPrefDao.saveBooleanValue(RANDOM, random);
    }

    private void saveRepeatSettingInPreferences(boolean repeat){
        sharedPrefDao.saveBooleanValue(REPEAT, repeat);
    }

    @Override
    public void setMusicService(MediaPlayerService musicService){
        this.musicService = musicService;
    }

    private String typeOfPlaylist = "";
    private SongList selectedSongList;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK)
                prepareAndPlaySongList(data);
            notifyFragments(typeOfPlaylist, selectedSongList);
        }
    }

    private void prepareAndPlaySongList(Intent data){
        int position = data.getIntExtra(POSITION, 0);
        typeOfPlaylist = data.getStringExtra(TYPE);
        selectedSongList = getSongListByType(typeOfPlaylist, data);
        songListDao.changeLatestSongList(selectedSongList);
        musicService.setLists(selectedSongList.getSongList(), random);
        refreshMusicFragment();
        refreshNowFragment();
        setSongPos(random, position);
        musicService.playSong();
        mainView.setPagerCurrentItem(3);
        mainView.showIsPlaying();
    }

    private SongList getSongListByType(String type, Intent data){
        SongList songList = null;
        switch(type){
            case(PlaylistViewActivity.ARTIST_TYPE):
                songList = getArtistSongList(data.getStringExtra(ARTIST));
                break;
            case(PlaylistViewActivity.PLAYLIST_TYPE):
                songList = getPlaylistSongList(data.getIntExtra(KEY, 0));
                break;
        }
        return songList;
    }

    private SongList getArtistSongList(String artist){
        return new MusicListData(activity).getArtistSongList(artist);
    }

    private SongList getPlaylistSongList(int key){
        return songListDao.getSongListWithKey(key);
    }

    private void refreshMusicFragment() {
        musicFragment.refreshData();
    }

    private void refreshNowFragment(){
        nowFragment.refreshData();
    }

    private void setSongPos(boolean random, int position){
        if(random)
            musicService.setTargetRandomSongTruePosition(position);
        else
            musicService.setSongPosAndNotifyActivity(position);
    }

    private void notifyFragments(String type, SongList songList){
        switch (type) {
            case PlaylistViewActivity.ARTIST_TYPE:
                refreshArtistFragment(songList);
                break;
            case PlaylistViewActivity.PLAYLIST_TYPE:
                refreshPlaylistFragment(songList);
                break;
            default:
                refreshAllData();
                break;
        }
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.notifySongAddedToPlaylist();
    }

    private void refreshArtistFragment(SongList songList){
        int position = 0;
        for (int i = 0; i < artistSongLists.size(); i++) {
            if (artistSongLists.get(i).getTitle().equals(songList.getTitle())) {
                position = i;
                break;
            }
        }
        artistFragment.notifyItemChanged(position, songList);
    }

    private void refreshPlaylistFragment(SongList songList) {
        playlistFragment.refreshData();
    }

    @Override
    public void refreshAllData() {
        artistFragment.refreshData();
        playlistFragment.refreshData();
    }

}
