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
import java.util.Collections;

public class MusicListData {

    private Context context;
    private MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
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

            do {
                long id = musicCursor.getLong(idColumn);
                String path = musicCursor.getString(column_index);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                String duration = musicCursor.getString(durationColumn);

                try {
                    metaRetriver.setDataSource(path);
                    songList.add(new Song(id, title, artist, path, duration));
                }catch (Exception exc){}

            } while (musicCursor.moveToNext());
        }
        return songList;
    }

    public SongList getAllMusicSongList(){
        SongList songList = new SongList(getSongList(), ALL_MUSIC_SONGLIST, 0);
        return songList;
    }

    public ArrayList<SongList> getArtistList(){
        ArrayList<SongList> songListArrayList = new ArrayList<>();
        ArrayList<Song> songArrayList = getSongList();
        boolean added;

        for(Song song: songArrayList){
            added = false;
            if(songListArrayList.size()!=0){
                for(SongList songList : songListArrayList){
                    if(songList.getTitle().equals(song.getArtist())){
                        songList.addSong(song);
                        added = true;
                    }
                }
                if(!added){
                    SongList songList = new SongList();
                    songList.setTitle(song.getArtist());
                    songList.addSong(song);
                    songListArrayList.add(songList);
                }
            }else{
                SongList songList = new SongList();
                songList.setTitle(song.getArtist());
                songList.addSong(song);
                songListArrayList.add(songList);
            }
        }
        Collections.sort(songListArrayList);
        Collections.reverse(songListArrayList);
        return songListArrayList;
    }

    public SongList getSongsAvailableToAdd(SongList songList){
        ArrayList<Song> songsAvailable = new ArrayList<>();
        boolean available;
        for(Song song: getSongList()){
            available = true;
            for(int i =0; i<songList.getSongList().size(); i++){
                if(song.getPath().equals(songList.getSongList().get(i).getPath())){
                    available = false;
                }
            }
            Log.d("available", available + "");
            if(available)
                songsAvailable.add(song);
        }
        SongList newSongList = new SongList();
        newSongList.setTitle(songList.getTitle());
        newSongList.setKey(songList.getKey());
        newSongList.setSongList(songsAvailable);
        return newSongList;
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
