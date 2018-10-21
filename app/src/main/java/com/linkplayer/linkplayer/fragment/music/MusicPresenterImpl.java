package com.linkplayer.linkplayer.fragment.music;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;

import com.linkplayer.linkplayer.dialog.fragments.AddNewPlaylistDialogFragment;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongDialogFragment;
import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongInformator;
import com.linkplayer.linkplayer.dialog.fragments.NewPlaylistInformator;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class MusicPresenterImpl{

    private ArrayList<Song> songArrayList;
    private Activity activity;
    private MusicFragmentView fragmentView;
    private DeleteSongInformator deleteSongInformator;
    private NewPlaylistInformator newPlaylistInformator;
    private SongListDao songListDao;

    public MusicPresenterImpl(ArrayList<Song> songArrayList, MusicFragmentView fragmentView, Activity activity){
        this.songArrayList = songArrayList;
        this.activity = activity;
        this.fragmentView = fragmentView;
        this.songListDao = new SongListDao(activity);
    }

    public void setInformators(DeleteSongInformator deleteSongInformator,
                               NewPlaylistInformator newPlaylistInformator){
        setDeleteSongInformator(deleteSongInformator);
        setNewPlaylistInformator(newPlaylistInformator);
    }

    public void setDeleteSongInformator(DeleteSongInformator deleteSongInformator){
        this.deleteSongInformator = deleteSongInformator;
    }

    public void setNewPlaylistInformator(NewPlaylistInformator newPlaylistInformator){
        this.newPlaylistInformator = newPlaylistInformator;
    }

    public void onBindSongRowViewAtPosition(MusicRecyclerHolder musicRecyclerHolder, final int position){
        Song song = songArrayList.get(position);
        if(song.isChoosed()){
            musicRecyclerHolder.setBackground(activity.getResources().getDrawable(R.drawable.gray_color));
        }else
            musicRecyclerHolder.setBackground(activity.getResources().getDrawable(R.drawable.gray_row_color));
        musicRecyclerHolder.setTitle(song.getTitle());
        musicRecyclerHolder.setAuthor(song.getArtist());
        musicRecyclerHolder.setMinutes(getMinutes(Integer.parseInt(song.getDuration())));
        musicRecyclerHolder.setSeconds(getSeconds(Integer.parseInt(song.getDuration())));
        musicRecyclerHolder.setOnClickItemView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragmentView.onItemClick(position);
            }
        });
        musicRecyclerHolder.setOnClickPopupMenu(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });
    }

    private String getMinutes(int duration){
        return String.valueOf(duration/60000);
    }

    private String getSeconds(int duration){
        int minutes = duration/60000;
        int seconds = (duration/1000) - minutes*60;
        if(seconds<10) {
            return ("0" +seconds);
        }else
            return String.valueOf(seconds);
    }

    private void showPopupMenu(View view, final int position){
        PopupMenu popupMenu = new PopupMenu(activity, view);
        popupMenu.getMenuInflater().inflate(R.menu.music_popup_menu, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        SubMenu subMenu = menu.getItem(1).getSubMenu();

        for(final SongList songList: songListDao.getAllTheSongLists()){
            if(!songListDao.songListContainsSong(songList.getKey(), songArrayList.get(position))) {
                subMenu.add(0, songList.getKey(), Menu.NONE, songList.getTitle())
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                songListDao.insertSongToListWithKey(songList.getKey(), songArrayList.get(position));
                                fragmentView.notifySongAddedToPlaylist();
                                return true;
                            }
                        });
            }
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.delete_song_item):
                        DeleteSongDialogFragment dialogFragment = new DeleteSongDialogFragment();
                        dialogFragment.setInformator(deleteSongInformator);
                        dialogFragment.setPosition(position);
                        dialogFragment.setSong(songArrayList.get(position));
                        dialogFragment.show(activity.getFragmentManager(), "DeleteSongDialogFragment");
                        return true;
                    case(R.id.new_playlist_item):
                        AddNewPlaylistDialogFragment playlistDialogFragment = new AddNewPlaylistDialogFragment();
                        playlistDialogFragment.setSong(songArrayList.get(position));
                        playlistDialogFragment.setNewPlaylistInformator(newPlaylistInformator);
                        playlistDialogFragment.show(activity.getFragmentManager(), "AddNewPlaylistDialogFragment");
                        return true;
                        default:
                            return false;
                }
            }
        });

        popupMenu.show();
    }

    public int getSongRowsCount(){
        return songArrayList.size();
    }

    public void setSongArrayList(ArrayList<Song> songArrayList){
        this.songArrayList = songArrayList;
    }

    public ArrayList<Song> getSongArrayList() {
        return songArrayList;
    }
}
