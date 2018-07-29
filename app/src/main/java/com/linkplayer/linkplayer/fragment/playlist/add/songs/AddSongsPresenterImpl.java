package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.content.Context;
import android.widget.CompoundButton;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class AddSongsPresenterImpl implements AddSongsPresenter {

    private ArrayList<Song> songList;
    private ArrayList<Song> markedSongs = new ArrayList<>();
    private Context context;

    public AddSongsPresenterImpl(){

    }

    public AddSongsPresenterImpl(ArrayList<Song> songList, Context context){
        this.songList = songList;
        this.context = context;
    }

    @Override
    public void onBindSongRowViewAtPosition(AddSongsRecyclerHolder holder, final int position) {
        holder.setTitle(songList.get(position).getTitle());
        holder.setArtist(songList.get(position).getArtist());
        holder.setOnCheckedListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    markedSongs.add(songList.get(position));
                else
                    markedSongs.remove(position);
            }
        });
    }

    @Override
    public void addSongsToPlaylist(SongList songList) {
        SongListDao songListDao = new SongListDao(context);
        for(Song song: markedSongs){
            songListDao.insertSongToListWithKey(songList.getKey(), song);
        }
    }

    @Override
    public int getAddSongsRowsCount() {
        return songList.size();
    }
}
