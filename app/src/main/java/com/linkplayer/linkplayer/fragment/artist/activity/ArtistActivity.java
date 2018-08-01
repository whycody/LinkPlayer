package com.linkplayer.linkplayer.fragment.artist.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linkplayer.linkplayer.MediaPlayerService;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.main.MainActivity;

public class ArtistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        MediaPlayerService musicService = (MediaPlayerService)getIntent().getSerializableExtra("MusicService");
        musicService.playNextSong();
    }
}
