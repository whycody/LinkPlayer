package com.linkplayer.linkplayer.data;

import android.content.Context;

import com.linkplayer.linkplayer.mappers.SongListMapper;
import com.linkplayer.linkplayer.mappers.SongMapper;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.model.SongListRealm;
import com.linkplayer.linkplayer.model.SongRealm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SongListDao {

    private Realm realm;
    private Context context;
    private SongMapper songMapper;
    private SongListMapper songListMapper;

    public SongListDao(Context context){
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        songMapper = new SongMapper();
        songListMapper = new SongListMapper();
    }

    public ArrayList<SongList> getAllListSongs() {
        ArrayList<SongList> songs = new ArrayList<>();
        SongListMapper mapper = new SongListMapper();
        RealmResults<SongListRealm> all = realm.where(SongListRealm.class).findAll().sort("key");
        for (SongListRealm noteRealm : all) {
            songs.add(mapper.fromRealm(noteRealm));
        }
        return songs;
    }

    public boolean songListContainsSong(int key, Song song){
        SongList songList = getSongListWithKey(key);
        for(Song songFromList: songList.getSongList()){
            if(songFromList.getPath().equals(song.getPath()))
                return true;
        }
        return false;
    }

    public void insertSongToListWithKey(int key, Song song){
        realm.beginTransaction();

        SongRealm songRealm = songMapper.toRealm(song);
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", key).findFirst();
        if(songListRealm!=null)
            songListRealm.addSong(songRealm);

        realm.commitTransaction();
    }

    public SongList getSongListWithKey(int key){
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", key).findFirst();
        return songListMapper.fromRealm(songListRealm);
    }

    public void insertSongList(String title){
        realm.beginTransaction();

        SongListRealm songListRealm = realm.createObject(SongListRealm.class, generateIdForList());
        songListRealm.setTitle(title);
        realm.commitTransaction();
    }

    public void editSongListTitle(int key, String title){
        realm.beginTransaction();
        realm.where(SongListRealm.class).equalTo("key", key).findFirst().setTitle(title);
        realm.commitTransaction();
    }

    public void deleteSongListById(final int key) {
        if(realm.where(SongListRealm.class).equalTo("key", key).findFirst()!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(SongListRealm.class).equalTo("key", key).findFirst().deleteFromRealm();
                }
            });
        }
    }

    private int idLastSongListValue = 1999999999;

    public SongList getLatestSongList(){
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", idLastSongListValue).findFirst();
        SongListMapper songListMapper = new SongListMapper();
        SongList songList = songListMapper.fromRealm(songListRealm);
        return songList;
    }

    public void changeLatestSongList(SongList songList){
        deleteLatestSongList();
        realm.beginTransaction();
        createLatestSongList(songList);
        realm.commitTransaction();
    }

    private void deleteLatestSongList(){
        if(realm.where(SongListRealm.class).equalTo("key", idLastSongListValue).findFirst()!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(SongListRealm.class).equalTo("key", idLastSongListValue).findFirst().deleteFromRealm();
                }
            });
        }
    }

    private void createLatestSongList(SongList songList) {
        SongListRealm songListRealm = realm.createObject(SongListRealm.class, idLastSongListValue);
        for(Song song : songList.getSongList()) {
            songListRealm.addSong(songMapper.toRealm(song));
        }
        songListRealm.setTitle(songList.getTitle());
    }

    private int generateIdForList() {
        if(realm.where(SongListRealm.class).max("key")==null)
            return 0;
        return realm.where(SongListRealm.class).max("key").intValue() + 1;
    }
}
