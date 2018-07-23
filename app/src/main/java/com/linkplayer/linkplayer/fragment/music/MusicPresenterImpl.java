package com.linkplayer.linkplayer.fragment.music;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.Song;

import java.util.ArrayList;
import java.util.Random;

public class MusicPresenterImpl{

    private ArrayList<Song> songList;
    private Context context;
    private MusicFragmentView fragmentView;

    public MusicPresenterImpl(ArrayList<Song> songList, MusicFragmentView fragmentView, Context context){
        this.songList = songList;
        this.context = context;
        this.fragmentView = fragmentView;
    }

    public void onBindSongRowViewAtPosition(MusicRecyclerHolder musicRecyclerHolder, final int position){
        Song song = songList.get(position);
        musicRecyclerHolder.setTitle(song.getTitle());
        musicRecyclerHolder.setAuthor(song.getArtist());
        musicRecyclerHolder.setFirstLetter(Character.toUpperCase(song.getTitle().charAt(0)));
        musicRecyclerHolder.setDuration(getDuration(Integer.valueOf(song.getDuration())));
        musicRecyclerHolder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragmentView.playMusic(position);
            }
        });
        musicRecyclerHolder.setGradientDrawable(getGradientDrawable());
    }

    private String getDuration(int duration){
        int minutes = duration/60000;
        int seconds = (duration/1000) - minutes*60;
        if(seconds<10) {
            return (minutes + ":" + "0" +seconds);
        }else
            return (minutes + ":" + seconds);
    }

    private GradientDrawable getGradientDrawable(){
        int randomColor = getRandomColor();
        int colorYellow = context.getResources().getColor(R.color.colorPrimaryDark);
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR,
                new int[]{randomColor, randomColor});
        gradientDrawable.setCornerRadius(0f);
        return gradientDrawable;
    }

    int getRandomColor(){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public int getSongRowsCount(){
        return songList.size();
    }

}
