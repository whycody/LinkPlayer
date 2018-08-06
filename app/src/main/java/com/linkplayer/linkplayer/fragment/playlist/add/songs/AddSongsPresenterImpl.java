package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import android.content.Context;
import android.widget.CompoundButton;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.AddSongItem;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class AddSongsPresenterImpl implements AddSongsPresenter {

    private ArrayList<AddSongItem> addSongItems;
    private ArrayList<Song> markedSongs = new ArrayList<>();
    private boolean[] songsChecked;
    private Context context;
    private int position;
    private AddSongsInformator addSongsInformator;

    public AddSongsPresenterImpl(ArrayList<AddSongItem> addSongItems, Context context) {
        this.addSongItems = addSongItems;
        this.context = context;
        songsChecked = new boolean[addSongItems.size()];
    }

    @Override
    public void onBindSongRowViewAtPosition(AddSongsRecyclerHolder holder, final int position) {
        holder.setTitle(addSongItems.get(position).getSong().getTitle());
        holder.setArtist(addSongItems.get(position).getSong().getArtist());
        holder.setOnCheckedListener(null);
        holder.setChecked(addSongItems.get(position).isChecked());
        holder.setOnCheckedListener(getCheckedChangeListener(position));

    }

    private CompoundButton.OnCheckedChangeListener getCheckedChangeListener(final int position) {
       return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    markedSongs.add(addSongItems.get(position).getSong());
                    addSongItems.get(position).setChecked(true);

                } else {
                    markedSongs.remove(addSongItems.get(position).getSong());
                    addSongItems.get(position).setChecked(false);
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
        return addSongItems.size();
    }
}
