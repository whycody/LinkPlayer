package com.linkplayer.linkplayer.fragment.playlist.add.songs;

import com.linkplayer.linkplayer.model.SongList;

public interface AddSongsInformator {

    void notifyItemChanged(SongList songList, int position);
}
