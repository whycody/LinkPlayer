package com.linkplayer.linkplayer.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.fragment.artist.ArtistFragment;
import com.linkplayer.linkplayer.fragment.music.MusicFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistFragment;

public class MainTabsPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    private MusicFragment musicFragment;
    private ArtistFragment artistFragment;
    private PlaylistFragment playlistFragment;

    public MainTabsPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        mNoOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MusicFragment();
            case 1:
                return new ArtistFragment();
            case 2:
                return new PlaylistFragment();
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch(position){
            case 0:
                musicFragment = (MusicFragment) createdFragment;
                break;
            case(1):
                artistFragment = (ArtistFragment) createdFragment;
                break;
            case(2):
                playlistFragment = (PlaylistFragment) createdFragment;
                break;
        }
        return createdFragment;
    }

    public MusicFragment getMusicFragment() {
        return musicFragment;
    }

    public ArtistFragment getArtistFragment() {
        return artistFragment;
    }

    public PlaylistFragment getPlaylistFragment() {
        return playlistFragment;
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
