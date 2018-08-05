package com.linkplayer.linkplayer.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.music.MusicFragment;
import com.linkplayer.linkplayer.fragment.now.NowFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistFragment;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

import java.util.Collections;


public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MainView, RefreshView{

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private MainTabsPagerAdapter tabsPagerAdapter;
    private TextView musicTitleView;
    private ImageButton playSongBtn, backSongBtn, nextSongBtn, randomMusicBtn, repeatMusicBtn;
    private MainPresenter mainPresenter;

    private MediaPlayerService musicService;

    private Intent playIntent;
    private boolean musicBound = false;
    private boolean repeat = false;
    private boolean random = false;
    private MusicFragment musicFragment;
    private NowFragment nowFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("tag", "oncreate");

        mainToolbar = findViewById(R.id.main_toolbar);
        mainTabLayout = findViewById(R.id.main_tab_layout);
        mainViewPager = findViewById(R.id.main_view_pager);
        musicTitleView = findViewById(R.id.music_title_view);
        playSongBtn = findViewById(R.id.play_song_btn);
        backSongBtn = findViewById(R.id.back_song_btn);
        nextSongBtn = findViewById(R.id.next_song_btn);
        randomMusicBtn = findViewById(R.id.random_music_btn);
        repeatMusicBtn = findViewById(R.id.repeat_music_btn);
        mainPresenter = new MainPresenterImpl(MainActivity.this, MainActivity.this);

        playSongBtn.setOnClickListener(playLatestSongOnClick);
        backSongBtn.setOnClickListener(playPreviousSongOnClick);
        nextSongBtn.setOnClickListener(playNextSongOnClick);
        randomMusicBtn.setOnClickListener(setRandomMusicModeOnClick);
        repeatMusicBtn.setOnClickListener(setRepeatMusicModeOnClick);

        Glide.with(this).load(R.drawable.back_white).into(backSongBtn);
        Glide.with(this).load(R.drawable.back_white).into(nextSongBtn);
        Glide.with(this).load(R.drawable.play_button_white).into(playSongBtn);
        Glide.with(this).load(R.drawable.replay_white).into(repeatMusicBtn);
        Glide.with(this).load(R.drawable.shuffle_white).into(randomMusicBtn);

        final String MUSIC = getString(R.string.music);
        final String ARTIST = getString(R.string.artist);
        final String PLAYLIST = getString(R.string.playlist);
        final String NOW = getString(R.string.now);

        mainTabLayout.addTab(mainTabLayout.newTab().setText(MUSIC));
        mainTabLayout.addTab(mainTabLayout.newTab().setText(ARTIST));
        mainTabLayout.addTab(mainTabLayout.newTab().setText(PLAYLIST));
        mainTabLayout.addTab(mainTabLayout.newTab().setText(NOW));

        tabsPagerAdapter = new MainTabsPagerAdapter(getSupportFragmentManager(), mainTabLayout.getTabCount());
        mainViewPager.setAdapter(tabsPagerAdapter);
        mainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTabLayout));
        mainTabLayout.addOnTabSelectedListener(this);

        setSupportActionBar(mainToolbar);
        startAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playIntent = new Intent(this, MediaPlayerService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK){
                String type = data.getStringExtra("type");
                SongList songList = null;
                SongList notShuffledList = new SongList();
                switch(type){
                    case(PlaylistViewActivity.ARTIST_TYPE):
                        songList = new MusicListData(this)
                                .getArtistSongList(data.getStringExtra("artist"));
                        break;
                    case(PlaylistViewActivity.PLAYLIST_TYPE):
                        songList = new SongListDao(this)
                                .getSongListWithKey(data.getIntExtra("key", 0));
                        break;
                }
                notShuffledList.setTitle(songList.getTitle());
                notShuffledList.setSongList(songList.getSongList());
                notShuffledList.setKey(songList.getKey());
                if(random) {
                    Collections.shuffle(songList.getSongList());
                    musicService.setList(songList.getSongList());
                }else
                    musicService.setList(songList.getSongList());
                musicService.setNotShuffledList(new SongListDao(this).getLatestSongList().getSongList());
                nowFragment = tabsPagerAdapter.getNowFragment();
                if(nowFragment!=null) {
                    nowFragment.refresh(notShuffledList);
                    nowFragment.notifyItemChanged(0, data.getIntExtra("position", 0));
                    nowFragment.notifyItemChanged(songList.getSongList().get(0),
                            songList.getSongList().get(data.getIntExtra("position", 0)));
                }
                for(Song song: musicService.getSongList())
                    Log.d("SongTag", data.getIntExtra("position", 0) + "");
                if(random)
                    musicService.setTargetRandomSong(data.getIntExtra("position", 0));
                else
                    musicService.setSongPosAndNotify(data.getIntExtra("position", 0));
                musicService.playSong();
                mainViewPager.setCurrentItem(3);
                setViewsOnPlaying();
            }

            PlaylistFragment playlistFragment = tabsPagerAdapter.getPlaylistFragment();
            if(playlistFragment!=null) {
                for (SongList songList : new SongListDao(this).getAllTheSongLists()) {
                    playlistFragment.notifyItemsAdded(songList);
                }
            }
        }
    }

    public void setSongList(SongList songList){
        musicService.setList(songList.getSongList());
        musicService.setNotShuffledList(songList.getSongList());
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder musicBinder = (MediaPlayerService.LocalBinder) service;
            musicService = musicBinder.getService();
            musicService.setRefreshView(MainActivity.this);
            mainPresenter.setMusicService(musicService);
            mainPresenter.getPreferencesAndSetButtons();
            if(!random)
                musicService.setSongPosAndNotify(mainPresenter.getLatestSong());
            else
                musicService.setTargetRandomSong(mainPresenter.getLatestSong());
            musicBound = true;
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

    @Override
    public void setSettings(boolean random, boolean repeat){
        this.random = random;
        this.repeat = repeat;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mainViewPager.setCurrentItem(tab.getPosition());
        mainToolbar.setTitle(firstCharToUpperCase(String.valueOf(tab.getText())));
        if(tab.getPosition()==0 || tab.getPosition()==3)
            musicService.setSongPosAndNotify(musicService.getSong());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private String firstCharToUpperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public void playSong(int position) {
        if(random)
            musicService.setTargetRandomSong(position);
        else
            musicService.setSongPosAndNotify(position);
        musicService.playSong();
        setViewsOnPlaying();
        setTitle();
    }

    public void refresh(){
        SongList songList = new SongListDao(this).getLatestSongList();
        musicService.setNotShuffledList(songList.getSongList());
        if(random)
            Collections.shuffle(songList.getSongList());
        musicService.setList(songList.getSongList());
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position) {
        musicFragment = tabsPagerAdapter.getMusicFragment();
        nowFragment = tabsPagerAdapter.getNowFragment();
        if(musicFragment!=null) {
            musicFragment.notifyItemChanged(lastPosition, position);
            musicFragment.notifyItemChanged(musicService.getSongList().get(lastPosition),
                    musicService.getSongList().get(position));
        }
        if(nowFragment!=null) {
            nowFragment.notifyItemChanged(lastPosition, position);
            nowFragment.notifyItemChanged(musicService.getSongList().get(lastPosition),
                    musicService.getSongList().get(position));
        }
    }

    private void setTitle() {
        musicTitleView.setText(mainPresenter.getTitle());
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

    private View.OnClickListener playLatestSongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.playSong();
            setViewsOnPlaying();
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
            playNextSong();
        }
    };

    private void playNextSong(){
        musicService.playNextSong();
        setTitle();

    }

    private View.OnClickListener playPreviousSongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playPreviousSong();
        }
    };

    private void playPreviousSong(){
        musicService.playPreviousSong();
        setTitle();
    }

    private View.OnClickListener setRandomMusicModeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            random = !random;
            mainPresenter.saveRandomPreferences(random);
        }
    };

    private View.OnClickListener setRepeatMusicModeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            repeat = !repeat;
            mainPresenter.saveRepeatReferences(repeat);
        }
    };

    @Override
    public void showRandomIsChosed(){
        randomMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorYellow),
                PorterDuff.Mode.MULTIPLY);
        randomMusicBtn.setAlpha(1f);
    }

    @Override
    public void showRandomIsNotChosed(){
        randomMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        randomMusicBtn.setAlpha(0.8f);
    }

    @Override
    public void showRepeatIsChosed(){
        repeatMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorYellow),
                PorterDuff.Mode.MULTIPLY);
        repeatMusicBtn.setAlpha(1f);
    }

    @Override
    public void showRepeatIsNotChosed(){
        repeatMusicBtn.setColorFilter(ContextCompat.getColor(MainActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        repeatMusicBtn.setAlpha(0.8f);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
