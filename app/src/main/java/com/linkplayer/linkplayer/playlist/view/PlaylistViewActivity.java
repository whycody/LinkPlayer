package com.linkplayer.linkplayer.playlist.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongInformator;
import com.linkplayer.linkplayer.fragment.LinearVerticalSpacing;
import com.linkplayer.linkplayer.fragment.music.MusicFragmentView;
import com.linkplayer.linkplayer.fragment.music.MusicPresenterImpl;
import com.linkplayer.linkplayer.fragment.music.MusicRecyclerAdapter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class PlaylistViewActivity extends AppCompatActivity implements PlaylistView,
        MusicFragmentView, AddSongsInformator, DeleteSongInformator{

    public static final String ARTIST_TYPE = "artist";
    public static final String PLAYLIST_TYPE = "playlist";
    public static final String ALL_SONGS_TYPE = "all";
    private boolean playlistChanged = false;

    private Toolbar playlistToolbar;
    private Button playlistTopBtn, deletePlaylistBtn;
    private RecyclerView playlistSongsRecycler;
    private MusicPresenterImpl musicPresenter;

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
        songList = viewPresenter.getSongList().getSongList();
        musicPresenter = new MusicPresenterImpl(songList, this, this);
        musicPresenter.setDeleteSongInformator(this);
        musicRecyclerAdapter = new MusicRecyclerAdapter(musicPresenter, this);
        playlistSongsRecycler.setAdapter(musicRecyclerAdapter);
        playlistSongsRecycler.setLayoutManager(new LinearLayoutManager(this));
        playlistSongsRecycler.addItemDecoration(new LinearVerticalSpacing(6));

        setSupportActionBar(playlistToolbar);
        playlistToolbar.setTitle(viewPresenter.getSongList().getTitle());
        viewPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        if(playlistChanged)
            viewPresenter.returnCanceledResult();
        super.onDestroy();
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
    public void onItemClick(int position) {
        viewPresenter.returnOKResult(position);
    }

    @Override
    public void notifyItemChanged(int lastPosition, int position) {

    }

    @Override
    public void notifyItemDeleted(int position, boolean deleted) {
        playlistChanged = true;
        viewPresenter.getSongList().getSongList().remove(position);
        musicRecyclerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemChanged(Song lastSong, Song song) {

    }

    @Override
    public void notifySongAddedToPlaylist() {

    }

    @Override
    public void notifyItemChanged(SongList songList, int position) {
        playlistChanged = true;
        musicPresenter.setSongArrayList(songList.getSongList());
        musicRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifySongDeleted(int position, boolean deleted) {
        if(deleted)
            notifyItemDeleted(position, deleted);
    }
}
