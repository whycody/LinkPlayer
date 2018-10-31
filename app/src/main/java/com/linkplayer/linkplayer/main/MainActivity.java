package com.linkplayer.linkplayer.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
import com.linkplayer.linkplayer.fragment.artist.ArtistFragment;
import com.linkplayer.linkplayer.fragment.music.MusicFragment;
import com.linkplayer.linkplayer.fragment.now.NowFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistFragment;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MainView, RefreshView{

    private Toolbar mainToolbar;
    private TabLayout mainTabLayout;
    private ViewPager mainViewPager;
    private MainTabsPagerAdapter tabsPagerAdapter;
    private TextView musicTitleView;
    private ImageButton playSongBtn, backSongBtn, nextSongBtn, randomMusicBtn, repeatMusicBtn;
    private CircleImageView playlistCircle, bellCircle, shareCircle, trashCircle;
    private MainPresenter mainPresenter;

    private MediaPlayerService musicService;

    private Intent playIntent;
    private Song showedSong;
    private boolean musicBound = false;
    private boolean repeat = false;
    private boolean random = false;
    private MusicFragment musicFragment;
    private NowFragment nowFragment;
    private PlaylistFragment playlistFragment;
    private SongListDao songListDao;

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
        playlistCircle = findViewById(R.id.playlist_circle);
        bellCircle = findViewById(R.id.bell_circle);
        shareCircle = findViewById(R.id.share_circle);
        trashCircle = findViewById(R.id.trash_circle);
        mainPresenter = new MainPresenterImpl(MainActivity.this, MainActivity.this);
        songListDao = new SongListDao(this);

        playSongBtn.setOnClickListener(playLatestSongOnClick);
        backSongBtn.setOnClickListener(playPreviousSongOnClick);
        nextSongBtn.setOnClickListener(playNextSongOnClick);
        randomMusicBtn.setOnClickListener(setRandomMusicModeOnClick);
        repeatMusicBtn.setOnClickListener(setRepeatMusicModeOnClick);

        Glide.with(this).load(R.drawable.back2_white).into(backSongBtn);
        Glide.with(this).load(R.drawable.back2_white).into(nextSongBtn);
        Glide.with(this).load(R.drawable.play2_button_white).into(playSongBtn);
        Glide.with(this).load(R.drawable.replay2_white).into(repeatMusicBtn);
        Glide.with(this).load(R.drawable.shuffle2_white).into(randomMusicBtn);
        Glide.with(this).load(R.drawable.add_to_playlist_icon).into(playlistCircle);
        Glide.with(this).load(R.drawable.bell_icon).into(bellCircle);
        Glide.with(this).load(R.drawable.share_icon).into(shareCircle);
        Glide.with(this).load(R.drawable.trash_can_icon).into(trashCircle);


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
        mainViewPager.setOffscreenPageLimit(5);

        registerBecomingNoisyReceiver();
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
        setTitleAndSong();
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
        mainPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerBecomingNoisyReceiver(){
        BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                musicService.pauseSong();
                showIsStopped();
            }
        };
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
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
        setSongPosIfRandom(position);
        musicService.playSong();
        showIsPlaying();
        setTitleAndSong();
    }

    private void setSongPosIfRandom(int position){
        if(random)
            musicService.setTargetRandomSong(position);
        else
            musicService.setSongPosAndNotify(position);
    }

    public void refreshService(SongList songList){
        if(nowFragment!=null)
            nowFragment.refresh();
        SongList realSongList = new SongList((ArrayList<Song>)songList.getSongList().clone(),
                songList.getTitle(), songList.getKey());
        musicService.setLists(realSongList.getSongList(), random);
        musicService.setSongPosAndNotify(musicService.getSongPos());
    }

    public void notifyAllData(int position, Song song){
        ArrayList<Song> songList = getSongListAvailable();
        checkIsMusicServiceIsPlayingDeletedSong(position, song);
        musicService.setLists(songList, random);
        mainPresenter.notifyAllData();
    }

    public void notifySongAddedToPlaylist(){
        if(nowFragment!=null && playlistFragment!=null) {
            nowFragment.refresh();
            playlistFragment.refresh();
        }
        refreshService(songListDao.getLatestSongList());
    }

    private ArrayList<Song> getSongListAvailable(){
        SongListDao songListDao = new SongListDao(this);
        ArrayList<Song> songList = songListDao.getLatestSongList().getSongList();
        if(songList.size()>0)
            return songList;
        else
            return new MusicListData(this).getSongList();
    }

    private void checkIsMusicServiceIsPlayingDeletedSong(int position, Song song){
        if(musicService.getSong().getPath().equals(song.getPath())) {
            showIsStopped();
            musicService.pauseSong();
            if (musicService.getSongList().size() > 0) {
                setPositionIfItIsPossible(position);
                setTitleAndSong();
            }
        }else{
            musicService.setSongPosAndNotify(musicService.getSongPos());
        }
    }

    private void setPositionIfItIsPossible(int position){
        if (position > 0) {
            musicService.setSongPosAndNotify(position - 1);
        } else if (musicService.getSongList().size() - 1 > position) {
            musicService.setSongPosAndNotify(position + 1);
        }
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position) {
        musicFragment = tabsPagerAdapter.getMusicFragment();
        nowFragment = tabsPagerAdapter.getNowFragment();
        playlistFragment = tabsPagerAdapter.getPlaylistFragment();
        notifyMusicFragmentIfNotNull(lastPosition, position);
        notifyNowFragmentIfNotNull(lastPosition, position);
    }

    private void notifyMusicFragmentIfNotNull(int lastPosition, int position){
        if(musicFragment!=null) {
            musicFragment.notifyItemChanged(lastPosition, position);
            musicFragment.notifyItemChanged(musicService.getSongList().get(lastPosition),
                    musicService.getSongList().get(position));
        }
    }

    private void notifyNowFragmentIfNotNull(int lastPosition, int position){
        if(nowFragment!=null) {
            nowFragment.notifyItemChanged(lastPosition, position);
            nowFragment.notifyItemChanged(musicService.getSongList().get(lastPosition),
                    musicService.getSongList().get(position));
        }
    }

    private void setTitleAndSong() {
        musicTitleView.setText(mainPresenter.getTitle());
        if(musicService!=null)
            showedSong = musicService.getSong();
    }

    private View.OnClickListener pauseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.pauseSong();
            showIsStopped();
        }
    };

    private View.OnClickListener playLatestSongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.playSong();
            showIsPlaying();
        }
    };

    private View.OnClickListener resumeOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            musicService.resumeSong();
            showIsPlaying();
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
        setTitleAndSong();

    }

    private View.OnClickListener playPreviousSongOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playPreviousSong();
        }
    };

    private void playPreviousSong(){
        musicService.playPreviousSong();
        setTitleAndSong();
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
    public void showIsPlaying() {
        playSongBtn.setOnClickListener(pauseOnClick);
        Glide.with(this).load(R.drawable.pause2_white).into(playSongBtn);
    }

    @Override
    public void showIsStopped() {
        playSongBtn.setOnClickListener(resumeOnClick);
        Glide.with(this).load(R.drawable.play2_button_white).into(playSongBtn);
    }

    @Override
    public void setPagerCurrentItem(int position) {
        mainViewPager.setCurrentItem(position);
    }

    @Override
    public NowFragment getNowFragment(){
        return tabsPagerAdapter.getNowFragment();
    }

    @Override
    public PlaylistFragment getPlaylistFragment() {
        return tabsPagerAdapter.getPlaylistFragment();
    }

    @Override
    public ArtistFragment getArtistFragment() {
        return tabsPagerAdapter.getArtistFragment();
    }

    @Override
    public MusicFragment getMusicFragment() {
        return tabsPagerAdapter.getMusicFragment();
    }
}
