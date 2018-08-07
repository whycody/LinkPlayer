package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.content.Context;
import android.widget.CompoundButton;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class AddSongsPresenterImpl implements AddSongsPresenter {

    private ArrayList<Song> songItems;
    private ArrayList<Song> markedSongs = new ArrayList<>();
    private boolean[] songsChecked;
    private Context context;
    private int position;
    private AddSongsInformator addSongsInformator;

    public AddSongsPresenterImpl(ArrayList<Song> songItems, Context context) {
        this.songItems = songItems;
        this.context = context;
        songsChecked = new boolean[songItems.size()];
    }

    @Override
    public void onBindSongRowViewAtPosition(AddSongsRecyclerHolder holder, final int position) {
        holder.setTitle(songItems.get(position).getTitle());
        holder.setArtist(songItems.get(position).getArtist());
        holder.setOnCheckedListener(null);
        holder.setChoosed(songItems.get(position).isChoosed());
        holder.setOnCheckedListener(getCheckedChangeListener(position));

    }

    private CompoundButton.OnCheckedChangeListener getCheckedChangeListener(final int position) {
       return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markedSongs.add(songItems.get(position));
                    songItems.get(position).setChoosed(true);

                } else {
                    markedSongs.remove(songItems.get(position));
                    songItems.get(position).setChoosed(false);
                }
            }
        };
    }


    @Override
    public void addSongsToPlaylist(SongList songList) {
        SongListDao songListDao = new SongListDao(context);
        for(Song song: markedSongs){
            songListDao.insertSongToListWithKey(songList.getKey(), song);
        }
        addSongsInformator.notifyItemChanged(songListDao.getSongListWithKey(songList.getKey()), position);
    }

    @Override
    public void setAddSongsInformator(AddSongsInformator addSongsInformator) {
        this.addSongsInformator = addSongsInformator;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getAddSongsRowsCount() {
        return songItems.size();
    }
}
