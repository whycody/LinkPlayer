package com.linkplayer.linkplayer.playlist.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.DownloadMusicYoutubeDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistPresenterImpl;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenterImpl;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsRecyclerAdapter;
import com.linkplayer.linkplayer.main.MainPresenterImpl;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class PlaylistViewPresenterImpl implements PlaylistViewPresenter{

    private Activity activity;
    private PlaylistView playlistView;
    private AddSongsInformator addSongsInformator;

    public PlaylistViewPresenterImpl(Activity activity, PlaylistView playlistView, AddSongsInformator addSongsInformator){
        this.activity = activity;
        this.playlistView = playlistView;
        this.addSongsInformator = addSongsInformator;
    }

    @Override
    public void onCreate() {
        if(getType().equals(PlaylistViewActivity.PLAYLIST_TYPE)){
            String addSongs = activity.getString(R.string.add_songs);
            playlistView.setTopButtonText(addSongs);
            playlistView.setTopButtonOnClickListener(addSongsOnClick);
            playlistView.setDeletePlaylistButtonVisiblity(View.VISIBLE);
        }else if(getType().equals(PlaylistViewActivity.ARTIST_TYPE)){
            String downloadMore = activity.getString(R.string.download_music_from_youtube);
            playlistView.setTopButtonText(downloadMore);
            playlistView.setTopButtonOnClickListener(downloadSongsOnClick);
            playlistView.setDeletePlaylistButtonVisiblity(View.GONE);
        }
    }

    @Override
    public void returnOKResult(int position) {
        Intent intent = new Intent();
        intent.putExtra(MainPresenterImpl.TYPE, getType());
        intent.putExtra(MainPresenterImpl.KEY, getKey());
        intent.putExtra(MainPresenterImpl.ARTIST, getArtist());
        intent.putExtra(MainPresenterImpl.POSITION, position);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    public void returnCanceledResult() {
        activity.setResult(Activity.RESULT_CANCELED);
    }

    private View.OnClickListener addSongsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AddSongsDialogFragment dialogFragment = new AddSongsDialogFragment();
            ArrayList<Song> songItems = new MusicListData(activity).getSongsAvailableToAdd(getSongList()).getSongList();
            AddSongsPresenter addSongsPresenter = new AddSongsPresenterImpl(songItems, activity);
            dialogFragment.setAddSongsPresenter(addSongsPresenter);
            dialogFragment.setRecyclerAdapter(new AddSongsRecyclerAdapter(addSongsPresenter, activity));
            dialogFragment.setSongList(getSongList());
            dialogFragment.setAddSongInformator(addSongsInformator);
            dialogFragment.show(activity.getFragmentManager(), "AddSongsDialogFragment");
        }
    };

    @Override
    public SongList getSongList() {
        int key = getKey();
        String artist = getArtist();
        if(getType().equals(PlaylistViewActivity.PLAYLIST_TYPE))
            return new SongListDao(activity).getSongListWithKey(key);
        else
            return new MusicListData(activity).getArtistSongList(artist);
    }

    private View.OnClickListener downloadSongsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DownloadMusicYoutubeDialogFragment dialogFragment = new DownloadMusicYoutubeDialogFragment();
            dialogFragment.show(activity.getFragmentManager(), "DownloadMusicYoutubeDialogFragment");
        }
    };

    @Override
    public String getType(){
        return activity.getIntent().getStringExtra(MainPresenterImpl.TYPE);
    }

    @Override
    public int getKey() {
        return activity.getIntent().getIntExtra(PlaylistPresenterImpl.SONG_LIST, 1);
    }

    @Override
    public String getArtist() {
        return activity.getIntent().getStringExtra(MainPresenterImpl.ARTIST);
    }


}
