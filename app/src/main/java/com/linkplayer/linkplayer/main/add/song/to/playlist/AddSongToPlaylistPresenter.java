package com.linkplayer.linkplayer.main.add.song.to.playlist;

import android.support.annotation.NonNull;

public interface AddSongToPlaylistPresenter {

    int getItemCount();

    void onBindViewHolder(@NonNull AddSongToPlaylistHolder holder, int position);

    void setAddSongToPlaylistInformator(AddSongToPlaylistInformator informator);
}
