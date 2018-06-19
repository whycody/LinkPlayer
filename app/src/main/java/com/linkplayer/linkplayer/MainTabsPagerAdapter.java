package com.linkplayer.linkplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.linkplayer.linkplayer.fragment.artist.ArtistFragment;
import com.linkplayer.linkplayer.fragment.music.MusicFragment;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistFragment;

public class MainTabsPagerAdapter extends FragmentPagerAdapter{

    int mNoOfTabs;

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
    public int getCount() {
        return mNoOfTabs;
    }
}
