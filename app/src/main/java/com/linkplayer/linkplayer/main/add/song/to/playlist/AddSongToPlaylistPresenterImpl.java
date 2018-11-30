package com.linkplayer.linkplayer.main.add.song.to.playlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;

public class AddSongToPlaylistPresenterImpl implements AddSongToPlaylistPresenter {

    private SongListDao songListDao;
    private AddSongToPlaylistInformator informator;
    private Context context;
    private Song song;
    private ArrayList<SongList> songList;

    public AddSongToPlaylistPresenterImpl(Context context, Song song, ArrayList<SongList> songList){
        this.song = song;
        this.songList = songList;
        this.context = context;
        this.songListDao = new SongListDao(context);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AddSongToPlaylistHolder holder, int position) {
        holder.setPlaylistText(songList.get(position).getTitle());
        holder.setOnItemViewClickListener(getOnClickListener(position));
    }

    @Override
    public void setAddSongToPlaylistInformator(AddSongToPlaylistInformator informator) {
        this.informator = informator;
    }

    private View.OnClickListener getOnClickListener(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSongToPlaylist(songList.get(position));
                informator.songAddedToPlaylist(true);
                Toast.makeText(context, getMessage(songList.get(position)), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private String getMessage(SongList songList){
        return "\"" + song.getTitle() + "\"" + context.getResources().getString
                (R.string.added_to_playlist) + "\"" + songList.getTitle() + "\"";
    }

    private void insertSongToPlaylist(SongList songList){
        songListDao.insertSongToListWithKey(songList.getKey(), song);
    }
}
