package com.linkplayer.linkplayer.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MusicListData {

    private Context context;
    public static final String ALL_MUSIC_SONGLIST = "allMusicPlaylist18";

    public MusicListData(Context context){
        this.context = context;
    }

    public ArrayList<Song> getSongList() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        ArrayList<Song> songList = new ArrayList<>();
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int column_index = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int dateModified = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED);
            do {
                long id = musicCursor.getLong(idColumn);
                long dateModif = musicCursor.getLong(dateModified);
                String path = musicCursor.getString(column_index);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                String duration = musicCursor.getString(durationColumn);
                songList.add(new Song(id, dateModif, title, artist, path, duration));
            } while (musicCursor.moveToNext());
        }

        Song[] songs = new Song[songList.size()];
        songs = songList.toArray(songs);

        Arrays.sort(songs, new Comparator<Song>(){
            public int compare(Song s1, Song s2) {
                return Long.valueOf
                        (s1.getDateModified() + (s1.getId()%100) + Integer.parseInt(s1.getDuration()))
                        .compareTo(s2.getDateModified() + (s2.getId()%100) + Integer.parseInt(s2.getDuration()));
            } });

        return new ArrayList<>(Arrays.asList(songs));
    }

    public long getIDOfNewRingtone(){
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, null);
        if (musicCursor != null && musicCursor.moveToLast()) {
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            return musicCursor.getLong(idColumn);
        }
        return 0;
    }

    public SongList getAllMusicSongList(){
        return new SongList(getSongList(), ALL_MUSIC_SONGLIST, 0);
    }

    private ArrayList<SongList> songListArrayList;
    private boolean added;

    public ArrayList<SongList> getArtistList(){
        songListArrayList = new ArrayList<>();
        fillTheSongListArrayList();
        Collections.sort(songListArrayList);
        Collections.reverse(songListArrayList);
        return songListArrayList;
    }

    private void fillTheSongListArrayList(){
        for(Song song: getSongList()){
            added = false;
            if(songListArrayList.size()!=0){
                checkSongListExitsAndAddSong(song);
                if(!added)
                    addNewSongListToArrayList(song);
            }else{
                addNewSongListToArrayList(song);
            }
        }
    }

    private void checkSongListExitsAndAddSong(Song song){
        for(SongList songList : songListArrayList){
            if(songList.getTitle().equals(song.getArtist())){
                songList.addSong(song);
                added = true;
            }
        }
    }

    private void addNewSongListToArrayList(Song song){
        SongList songList = new SongList();
        songList.setTitle(song.getArtist());
        songList.addSong(song);
        songListArrayList.add(songList);
    }


    private ArrayList<Song> songsAvailable;
    private boolean available;

    public SongList getSongsAvailableToAdd(SongList songList){
        songsAvailable = new ArrayList<>();
        fillTheAvailableSongsList(songList);
        return new SongList(songsAvailable, songList.getTitle(), songList.getKey());
    }

    private void fillTheAvailableSongsList(SongList songList){
        for(Song song: getSongList()){
            available = true;
            checkSongListHasSong(song, songList);
            if(available)
                songsAvailable.add(song);
        }
    }

    private void checkSongListHasSong(Song song, SongList songList){
        for(int i =0; i<songList.getSongList().size(); i++){
            if(song.getPath().equals(songList.getSongList().get(i).getPath())){
                available = false;
            }
        }
    }

    public SongList getArtistSongList(String artist){
        SongList songList = new SongList();
        songList.setTitle(artist);
        for(Song song: getSongList()){
            if(song.getArtist().equals(artist))
                songList.addSong(song);
        }
        return songList;
    }
}
