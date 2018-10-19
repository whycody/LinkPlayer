package com.linkplayer.linkplayer.fragment.now;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.dialog.fragments.AddNewPlaylistDialogFragment;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongDialogFragment;
import com.linkplayer.linkplayer.dialog.fragments.DeleteSongInformator;
import com.linkplayer.linkplayer.fragment.music.MusicFragmentView;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

public class NowPresenterImpl {

    private SongList songList;
    private Activity activity;
    private MusicFragmentView musicFragmentView;
    private DeleteSongInformator deleteSongInformator;
    private SongListDao songListDao;

    public NowPresenterImpl(SongList songList, Activity activity,
                            MusicFragmentView musicFragmentView, DeleteSongInformator deleteSongInformator){
        this.songList = songList;
        this.activity = activity;
        this.musicFragmentView = musicFragmentView;
        this.deleteSongInformator = deleteSongInformator;
        this.songListDao = new SongListDao(activity);
    }

    public void onBindSongRowViewAtPosition(NowRecyclerHolder nowRecyclerHolder, final int position) {
        Song song = songList.getSongList().get(position);

        nowRecyclerHolder.setTitle(song.getTitle());
        nowRecyclerHolder.setTime(getTime(Integer.parseInt(song.getDuration())));
        if(song.isChoosed()){
            nowRecyclerHolder.setTextColor(activity.getResources().getColor(R.color.colorYellow));
        }else
            nowRecyclerHolder.setTextColor(activity.getResources().getColor(R.color.colorLightWhite));
        nowRecyclerHolder.setOnClickItemView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicFragmentView.onItemClick(position);
            }
        });
        nowRecyclerHolder.setOnClickPopupMenu(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });

    }

    private String getTime(int time){
        return getMinutes(time)+":"+getSeconds(time);
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
            if(!songListDao.songListContainsSong(songList.getKey(), this.songList.getSongList().get(position))) {
                subMenu.add(0, songList.getKey(), Menu.NONE, songList.getTitle())
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                songListDao.insertSongToListWithKey(songList.getKey(), songList.getSongList().get(position));
                                musicFragmentView.notifySongAddedToPlaylist();
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
                        dialogFragment.setSong(songList.getSongList().get(position));
                        dialogFragment.show(activity.getFragmentManager(), "DeleteSongDialogFragment");
                        return true;
                    case(R.id.new_playlist_item):
                        AddNewPlaylistDialogFragment playlistDialogFragment = new AddNewPlaylistDialogFragment();
                        playlistDialogFragment.setSong(songList.getSongList().get(position));
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
        return songList.getSongList().size();
    }

    public void setSongList(SongList songList){
        this.songList = songList;
    }

    public SongList getSongList() {
        return songList;
    }
}
