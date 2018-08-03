package com.linkplayer.linkplayer.playlist.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.fragment.music.MusicFragmentView;
import com.linkplayer.linkplayer.fragment.music.MusicPresenterImpl;
import com.linkplayer.linkplayer.fragment.music.MusicRecyclerAdapter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class PlaylistViewActivity extends AppCompatActivity implements PlaylistView, MusicFragmentView, AddSongsInformator{

    public static final String ARTIST_TYPE = "artist";
    public static final String PLAYLIST_TYPE = "playlist";

    private Toolbar playlistToolbar;
    private Button playlistTopBtn;
    private RecyclerView playlistSongsRecycler;
    private Button deletePlaylistBtn;

    private PlaylistViewPresenter viewPresenter;
    private MusicRecyclerAdapter musicRecyclerAdapter;
    private ArrayList<Song> songList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);

        playlistToolbar = findViewById(R.id.playlist_toolbar);
        playlistTopBtn = findViewById(R.id.playlist_top_btn);
        playlistSongsRecycler = findViewById(R.id.playlist_songs_recycler);
        deletePlaylistBtn = findViewById(R.id.delete_playlist_btn);
        viewPresenter = new PlaylistViewPresenterImpl(this, this, this);
        songList = getSongList().getSongList();
        musicRecyclerAdapter = new MusicRecyclerAdapter(new MusicPresenterImpl(songList, this, this), this);
        playlistSongsRecycler.setAdapter(musicRecyclerAdapter);
        playlistSongsRecycler.setLayoutManager(new LinearLayoutManager(this));
        playlistSongsRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        setSupportActionBar(playlistToolbar);
        playlistToolbar.setTitle(getSongList().getTitle());
        viewPresenter.onCreate();
    }

    @Override
    public void setDeletePlaylistButtonVisiblity(int visiblity) {
        deletePlaylistBtn.setVisibility(visiblity);
    }

    @Override
    public void setTopButtonOnClickListener(View.OnClickListener onClick) {
        playlistTopBtn.setOnClickListener(onClick);
    }

    @Override
    public void setTopButtonText(String text) {
        playlistTopBtn.setText(text);
    }

    @Override
    public SongList getSongList() {
        return new SongListDao(this).getLatestSongList();
    }

    @Override
    public void onItemClick(int position) {
        returnResult(position);
    }

    private void returnResult(int position){
        Intent intent = new Intent();
        intent.putExtra("songPath", getSongList().getSongList().get(position).getPath());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position) {

    }

    @Override
    public void notifyItemDeleted(int position) {
        getSongList().getSongList().remove(position);
        musicRecyclerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemsAdded(SongList songList) {
        this.songList = songList.getSongList();
//        for(Song song: songList.getSongList()) {
//            Toast.makeText(this, song.getTitle(), Toast.LENGTH_SHORT).show();
//        }
        musicRecyclerAdapter.notifyDataSetChanged();
    }
}
