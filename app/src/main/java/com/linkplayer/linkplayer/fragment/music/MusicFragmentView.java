package com.linkplayer.linkplayer.fragment.music;

import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;

public interface MusicFragmentView {

    void playMusic(int position);

    void notifyItemChanged(int lastPosition, int position);

}
