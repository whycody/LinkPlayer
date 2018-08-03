package com.linkplayer.linkplayer.playlist.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.dialog.fragments.DownloadMusicYoutubeDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenterImpl;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsRecyclerAdapter;
import com.linkplayer.linkplayer.model.AddSongItem;

import java.util.ArrayList;

public class PlaylistViewPresenterImpl implements PlaylistViewPresenter{

    private Activity activity;
    private PlaylistView playlistView;
    private SharedPreferences sharedPreferences;
    private AddSongsInformator addSongsInformator;

    public PlaylistViewPresenterImpl(Activity activity, PlaylistView playlistView, AddSongsInformator addSongsInformator){
        this.activity = activity;
        this.playlistView = playlistView;
        this.addSongsInformator = addSongsInformator;
        sharedPreferences = activity.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate() {
        if(getType().equals(PlaylistViewActivity.PLAYLIST_TYPE)){
            playlistView.setTopButtonText("Add songs");
            playlistView.setTopButtonOnClickListener(addSongsOnClick);
            playlistView.setDeletePlaylistButtonVisiblity(View.VISIBLE);
        }else if(getType().equals(PlaylistViewActivity.ARTIST_TYPE)){
            playlistView.setTopButtonText("Download more music from Youtube");
            playlistView.setTopButtonOnClickListener(downloadSongsOnClick);
            playlistView.setDeletePlaylistButtonVisiblity(View.GONE);
        }
    }

    private View.OnClickListener addSongsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddSongsDialogFragment dialogFragment = new AddSongsDialogFragment();
            ArrayList<AddSongItem> addSongItems = new MusicListData(activity).getAddSongItems();
            AddSongsPresenter addSongsPresenter = new AddSongsPresenterImpl(addSongItems, activity);
            dialogFragment.setAddSongsPresenter(addSongsPresenter);
            dialogFragment.setRecyclerAdapter(new AddSongsRecyclerAdapter(addSongsPresenter, activity));
            dialogFragment.setSongList(playlistView.getSongList());
            dialogFragment.setAddSongInformator(addSongsInformator);
            dialogFragment.show(activity.getFragmentManager(), "AddSongsDialogFragment");
        }
    };

    private View.OnClickListener downloadSongsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DownloadMusicYoutubeDialogFragment dialogFragment = new DownloadMusicYoutubeDialogFragment();
            dialogFragment.show(activity.getFragmentManager(), "DownloadMusicYoutubeDialogFragment");
        }
    };

    private String getType(){
        return activity.getIntent().getStringExtra("type");
    }



}
