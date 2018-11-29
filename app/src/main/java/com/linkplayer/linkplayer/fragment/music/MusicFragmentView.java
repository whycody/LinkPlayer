package com.linkplayer.linkplayer.fragment.music;

import com.linkplayer.linkplayer.model.Song;

public interface MusicFragmentView {

    void onItemClick(int position);

    void notifyItemChanged(int lastPosition, int position);

    void notifyItemDeleted(int position);

    void notifyItemChanged(Song lastSong, Song song);

    void notifySongAddedToPlaylist();

}
