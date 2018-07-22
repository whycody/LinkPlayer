package com.linkplayer.linkplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linkplayer.linkplayer.data.SongDao;
import com.linkplayer.linkplayer.fragment.music.MainView;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MainView {

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private MainTabsPagerAdapter tabsPagerAdapter;
    private TextView musicTitleView;
    private ImageButton playSongBtn, backSongBtn, nextSongBtn, randomMusicBtn, repeatMusicBtn;
    private SongDao songDao;
    private MusicListData musicListData;

    private MediaPlayerService musicService;
    private Intent playIntent;
    private ArrayList<Song> songList;
    private ArrayList<Song> shuffledSongList;
    private boolean musicBound = false;
    private boolean repeat, random;
    private final String PREFERENCES = "preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = findViewById(R.id.main_toolbar);
        mainTabLayout = findViewById(R.id.main_tab_layout);
        mainViewPager = findViewById(R.id.main_view_pager);
        musicTitleView = findViewById(R.id.music_title_view);
        playSongBtn = findViewById(R.id.play_song_btn);
        backSongBtn = findViewById(R.id.back_song_btn);
        nextSongBtn = findViewById(R.id.next_song_btn);
        randomMusicBtn = findViewById(R.id.random_music_btn);
        repeatMusicBtn = findViewById(R.id.repeat_music_btn);
        musicListData = new MusicListData(this);
        songList = musicListData.getSongList();
        shuffledSongList = (ArrayList<Song>)songList.clone();
        Collections.shuffle(shuffledSongList);
        songDao = new SongDao(this);
        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        preferencesEditor = sharedPreferences.edit();

        playSongBtn.setOnClickListener(resumeOnClick);
        backSongBtn.setOnClickListener(playLastSongOnClick);
        nextSongBtn.setOnClickListener(playNextSongOnClick);
        randomMusicBtn.setOnClickListener(setRandomMusicModeOnClick);
        repeatMusicBtn.setOnClickListener(setRepeatMusicModeOnClick);

        Glide.with(this).load(R.drawable.back_white).into(backSongBtn);
        Glide.with(this).load(R.drawable.back_white).into(nextSongBtn);
        Glide.with(this).load(R.drawable.play_button_white).into(playSongBtn);
        Glide.with(this).load(R.drawable.replay_white).into(repeatMusicBtn);
        Glide.with(this).load(R.drawable.shuffle_white).into(randomMusicBtn);

        final String MUSIC = getResources().getString(R.string.music);
        final String ARTIST = getResources().getString(R.string.artist);
        final String PLAYLIST = getResources().getString(R.string.playlist);

        mainTabLayout.addTab(mainTabLayout.newTab().setText(MUSIC));
        mainTabLayout.addTab(mainTabLayout.newTab().setText(ARTIST));
        mainTabLayout.addTab(mainTabLayout.newTab().setText(PLAYLIST));

        tabsPagerAdapter = new MainTabsPagerAdapter(getSupportFragmentManager(), mainTabLayout.getTabCount());
        mainViewPager.setAdapter(tabsPagerAdapter);
        mainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTabLayout));
        mainTabLayout.addOnTabSelectedListener(this);

        setSupportActionBar(mainToolbar);
        setTitle();
        startAnimation();
    }

    private void getPreferencesAndSetButtons() {
        random = sharedPreferences.getBoolean("random", false);
        repeat = sharedPreferences.getBoolean("repeat", false);

        saveRandomPreferences(random);
        saveRepeatReferences(repeat);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MediaPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(musicConnection!=null){
            unbindService(musicConnection);
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder musicBinder = (MediaPlayerService.LocalBinder) service;
            musicService = musicBinder.getService();
            if(random) {
                musicService.setList(shuffledSongList);
            }else
                musicService.setList(songList);
            musicBound = true;
            setLatestSong();
            getPreferencesAndSetButtons();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);
        musicTitleView.startAnimation(animation);
    }

    private void setLatestSong(){
        for(int i =0; i<songList.size(); i++){
            if(songList.get(i).getPath().equals(songDao.getLatestMusic().getPath())){
                musicService.setSong(i);
                break;
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mainViewPager.setCurrentItem(tab.getPosition());
        mainToolbar.setTitle(firstCharToUpperCase(String.valueOf(tab.getText())));
    }

    private String firstCharToUpperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void playSong(int position) {
        if(random) {
            playClickedSong(position);
        }else
            musicService.setSong(position);
        musicService.playSong();
        setViewsOnPlaying();
        setTitle();
    }

    private void playClickedSong(int position){
        for (int i = 0; i < shuffledSongList.size(); i++) {
            if (shuffledSongList.get(i).getPath().equals(songList.get(position).getPath())) {
                musicService.setSong(i);
            }
        }
    }

    private void setTitle() {
        SongDao songDao = new SongDao(this);
        String title = songDao.getLatestMusic().getTitle();
        String author = songDao.getLatestMusic().getArtist();
        if (!author.equals("<unknown>")) {
            musicTitleView.setText(author + " - " + title);
        } else {
            musicTitleView.setText(title);
        }
    }

    private void setViewsOnPlaying() {
        playSongBtn.setOnClickListener(pauseOnClick);
        Glide.with(this).load(R.drawable.pause_white).into(playSongBtn);
    }

    private void setViewsOnStopped() {
        playSongBtn.setOnClickListener(resumeOnClick);
        Glide.with(this).load(R.drawable.play_button_white).into(playSongBtn);
    }

    private View.OnClickListener pauseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.pauseSong();
            setViewsOnStopped();
        }
    };

    private View.OnClickListener resumeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.resumeSong();
            setViewsOnPlaying();
        }
    };

    private View.OnClickListener playNextSongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.playNextSong();
            setTitle();
        }
    };

    private View.OnClickListener playLastSongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.playLastSong();
            setTitle();
        }
    };

    private View.OnClickListener setRandomMusicModeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            random = !random;
            saveRandomPreferences(random);
        }
    };

    private void saveRandomPreferences(boolean random){
        if(random) {
            musicService.setList(shuffledSongList);
            showRandomIsChosed();
        }else {
            musicService.setList(songList);
            showRandomIsNotChosed();
        }
        musicService.setOptionsRandomRepeat(random, repeat);
        saveInSharedPreferences(random, repeat);
    }

    private void showRandomIsChosed(){
        randomMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorYellow),
                PorterDuff.Mode.MULTIPLY);
        randomMusicBtn.setAlpha(1f);
    }

    private void showRandomIsNotChosed(){
        randomMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        randomMusicBtn.setAlpha(0.8f);
    }

    private View.OnClickListener setRepeatMusicModeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repeat = !repeat;
            saveRepeatReferences(repeat);
        }
    };

    private void saveRepeatReferences(boolean repeat){
        if(repeat){
            showRepeatIsChosed();
        }else {
            showRepeatIsNotChosed();
        }
        musicService.setOptionsRandomRepeat(random, repeat);
        saveInSharedPreferences(random, repeat);
    }

    private void showRepeatIsChosed(){
        repeatMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorYellow),
                PorterDuff.Mode.MULTIPLY);
        repeatMusicBtn.setAlpha(1f);
    }

    private void showRepeatIsNotChosed(){
        repeatMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        repeatMusicBtn.setAlpha(0.8f);
    }

    private void saveInSharedPreferences(boolean random, boolean repeat){
        preferencesEditor.putBoolean("random", random);
        preferencesEditor.putBoolean("repeat", repeat);
        preferencesEditor.commit();
    }
}
