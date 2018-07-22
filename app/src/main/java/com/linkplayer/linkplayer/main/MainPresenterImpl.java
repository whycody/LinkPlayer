package com.linkplayer.linkplayer.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SongDao;
import com.linkplayer.linkplayer.model.Song;

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
    private ArrayList<Song> songList;
    private ArrayList<Song> shuffledSongList;


    public MainPresenterImpl(Context context, MainView mainView){
        this.context = context;
        this.mainView = mainView;
        songDao = new SongDao(context);
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        musicListData = new MusicListData(context);
        songList = musicListData.getSongList();
        shuffledSongList = (ArrayList<Song>)songList.clone();
        Collections.shuffle(shuffledSongList);

    }

    @Override
    public void getPreferencesAndSetButtons() {
        random = sharedPreferences.getBoolean("random", false);
        repeat = sharedPreferences.getBoolean("repeat", false);

        saveRandomPreferences(random);
        saveRepeatReferences(repeat);
    }

    @Override
    public void saveRandomPreferences(boolean random){
        if(random) {
            musicService.setList(shuffledSongList);
            mainView.showRandomIsChosed();
        }else {
            musicService.setList(songList);
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
    public void setClickedSongIfRandom(int position) {
        for (int i = 0; i < shuffledSongList.size(); i++) {
            if (shuffledSongList.get(i).getPath().equals(songList.get(position).getPath())) {
                musicService.setSong(i);
            }
        }
    }
}
