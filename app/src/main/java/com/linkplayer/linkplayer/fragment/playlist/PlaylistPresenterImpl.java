package com.linkplayer.linkplayer.fragment.playlist;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.DeletePlaylistDialogFragment;
import com.linkplayer.linkplayer.dialog.fragments.AddSongsDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenterImpl;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsRecyclerAdapter;
import com.linkplayer.linkplayer.main.MainPresenterImpl;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

import java.util.ArrayList;

public class PlaylistPresenterImpl implements PlaylistPresenter {

    public static final String SONG_LIST = "songList";
    private ArrayList<SongList> songListArrayList;
    private Activity activity;
    private SongListDao songListDao;
    private PlaylistView playlistView;
    private AddSongsInformator addSongsInformator;
    private MusicListData musicListData;

    public PlaylistPresenterImpl(ArrayList<SongList> songListArrayList, PlaylistView playlistView, Activity activity){
        this.songListArrayList = songListArrayList;
        this.activity = activity;
        this.playlistView = playlistView;
        songListDao = new SongListDao(activity);
        musicListData = new MusicListData(activity);
    }


    @Override
    public void onBindSongRowViewAtPosition(PlaylistRecyclerHolder playlistRecyclerHolder, final int position) {
        SongList songList = songListArrayList.get(position);
        playlistRecyclerHolder.setTitle(songList.getTitle());
        playlistRecyclerHolder.setSongsNumber(("Songs: " + songList.getSongList().size()));
        playlistRecyclerHolder.setOnItemViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToNextActivity(position);
            }
        });
        playlistRecyclerHolder.setOnThreeDotesClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });
    }

    private void sendToNextActivity(int position){
        Intent intent = new Intent(activity, PlaylistViewActivity.class);
        intent.putExtra(MainPresenterImpl.TYPE, PlaylistViewActivity.PLAYLIST_TYPE);
        intent.putExtra(SONG_LIST, songListArrayList.get(position).getKey());
        activity.startActivityForResult(intent, 1);
    }

    private void showPopupMenu(View view, final int position){
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.getMenuInflater().inflate(R.menu.playlist_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete_playlits_item:
                        DeletePlaylistDialogFragment dialogFragment = new DeletePlaylistDialogFragment();
                        dialogFragment.setPosition(position);
                        dialogFragment.setSongList(new SongListDao(activity).getAllTheSongLists().get(position));
                        dialogFragment.setPlaylistView(playlistView);
                        dialogFragment.show(activity.getFragmentManager(), "DeletePlaylistDialogFragment");
                        return true;
                    case R.id.add_song_to_playlist_item:
                        AddSongsPresenter addSongsPresenter = new AddSongsPresenterImpl
                                (musicListData.getSongsAvailableToAdd(songListArrayList.get(position)).getSongList(), activity);
                        AddSongsDialogFragment addSongsDialogFragment = new AddSongsDialogFragment();
                        addSongsDialogFragment.setAddSongsPresenter(addSongsPresenter);
                        addSongsDialogFragment.setSongList(songListArrayList.get(position));
                        addSongsDialogFragment.setPosition(position);
                        addSongsDialogFragment.setRecyclerAdapter(new AddSongsRecyclerAdapter(addSongsPresenter, activity));
                        addSongsDialogFragment.setAddSongInformator(addSongsInformator);
                        addSongsDialogFragment.show(activity.getFragmentManager(), "AddSongsDialogFragment");
                        return true;
                        default:
                            return false;
                }
            }
        });


        popupMenu.show();
    }

    @Override
    public int getPlaylistRowCount() {
        return songListArrayList.size();
    }

    @Override
    public void setAddSongsInformator(AddSongsInformator addSongsInformator) {
        this.addSongsInformator = addSongsInformator;
    }

    @Override
    public void setSongListArrayList(ArrayList<SongList> songListArrayList) {
        this.songListArrayList = songListArrayList;
    }
}
