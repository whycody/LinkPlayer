package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class MusicPresenterImpl{

    private ArrayList<Song> songArrayList;
    private Context context;
    private MusicFragmentView fragmentView;
    private SongListDao songListDao;

    public MusicPresenterImpl(ArrayList<Song> songArrayList, MusicFragmentView fragmentView, Context context){
        this.songArrayList = songArrayList;
        this.context = context;
        this.fragmentView = fragmentView;
        this.songListDao = new SongListDao(context);
    }

    public void onBindSongRowViewAtPosition(MusicRecyclerHolder musicRecyclerHolder, final int position){
        Song song = songArrayList.get(position);
        if(song.isChoosed()){
            musicRecyclerHolder.setBackground(context.getResources().getDrawable(R.drawable.gray_color));
        }else
            musicRecyclerHolder.setBackground(context.getResources().getDrawable(R.drawable.gray_row_color));
        musicRecyclerHolder.setTitle(song.getTitle());
        musicRecyclerHolder.setAuthor(song.getArtist());
        musicRecyclerHolder.setMinutes(getMinutes(Integer.parseInt(song.getDuration())));
        musicRecyclerHolder.setSeconds(getSeconds(Integer.parseInt(song.getDuration())));
        musicRecyclerHolder.setOnClickItemView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragmentView.playMusic(position);
            }
        });
        musicRecyclerHolder.setOnClickPopupMenu(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });
       // musicRecyclerHolder.setGradientDrawable(getGradientDrawable());
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
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.music_popup_menu, popupMenu.getMenu());


        Menu menu = popupMenu.getMenu();
        SubMenu subMenu = menu.getItem(1).getSubMenu();

        for(final SongList songList: songListDao.getAllListSongs()){
            subMenu.add(0, songList.getKey(), Menu.NONE, songList.getTitle())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    songListDao.insertSongToListWithKey(songList.getKey(), songArrayList.get(position));
                    return true;
                }
            });
        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.delete_song_item):
                        Toast.makeText(context, "We cant delete, sorry", Toast.LENGTH_SHORT).show();
                        return true;
                    case(R.id.new_playlist_item):
                        Toast.makeText(context, "You havent playlist", Toast.LENGTH_SHORT).show();
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

}
