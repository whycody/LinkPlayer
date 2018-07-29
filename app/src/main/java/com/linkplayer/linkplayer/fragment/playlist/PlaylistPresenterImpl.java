package com.linkplayer.linkplayer.fragment.playlist;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.MusicListData;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.DeletePlaylistDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsDialogFragment;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenterImpl;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsRecyclerAdapter;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class PlaylistPresenterImpl implements PlaylistPresenter {

    private ArrayList<SongList> songListArrayList;
    private Activity activity;
    private SongListDao songListDao;
    private PlaylistView playlistView;

    public PlaylistPresenterImpl(ArrayList<SongList> songListArrayList, PlaylistView playlistView, Activity activity){
        this.songListArrayList = songListArrayList;
        this.activity = activity;
        this.playlistView = playlistView;
        songListDao = new SongListDao(activity);
    }


    @Override
    public void onBindSongRowViewAtPosition(PlaylistRecyclerHolder playlistRecyclerHolder, final int position) {
        SongList songList = songListArrayList.get(position);
        playlistRecyclerHolder.setTitle(songList.getTitle());
        playlistRecyclerHolder.setSongsNumber(("Songs: " + songList.getSongList().size()));
        playlistRecyclerHolder.setOnItemViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songListDao.changeLatestSongList(songListArrayList.get(position));
            }
        });
        playlistRecyclerHolder.setOnThreeDotesClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });
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
                        AddSongsPresenter addSongsPresenter = new AddSongsPresenterImpl(new MusicListData(activity).getSongList(), activity);
                        AddSongsDialogFragment addSongsDialogFragment = new AddSongsDialogFragment();
                        addSongsDialogFragment.setAddSongsPresenter(addSongsPresenter);
                        addSongsDialogFragment.setSongList(songListArrayList.get(position));
                        addSongsDialogFragment.setRecyclerAdapter(new AddSongsRecyclerAdapter(addSongsPresenter, activity));
                        addSongsDialogFragment.show(activity.getFragmentManager(), "AddSongsDialogFragment");
                        return true;
                        default:
                            return false;
                }
            }
        });


        popupMenu.show();
    }

    private ArrayList<Song> getSongsAvailable(int position){
        SongList songList = songListArrayList.get(position);
        ArrayList<Song> songArrayList = new MusicListData(activity).getSongList();
        ArrayList<Song> songsAvailable = new ArrayList<>();
        boolean available;
        for(Song song: songList.getSongList()){
            available = true;
            for(int i =0; i<songArrayList.size(); i++){
                if(song.getPath().equals(songArrayList.get(i).getPath())){
                    available = false;
                }
            }
            if(available)
                songsAvailable.add(song);
        }
        return songsAvailable;
    }

    @Override
    public int getPlaylistRowCount() {
        return songListArrayList.size();
    }
}
