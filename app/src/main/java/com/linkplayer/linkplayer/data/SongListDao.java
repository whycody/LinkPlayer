package com.linkplayer.linkplayer.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.linkplayer.linkplayer.mappers.SongListMapper;
import com.linkplayer.linkplayer.mappers.SongMapper;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.model.SongListRealm;
import com.linkplayer.linkplayer.model.SongRealm;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
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

    public ArrayList<SongList> getAllTheSongLists() {
        ArrayList<SongList> songs = new ArrayList<>();
        SongListMapper songListMapper = new SongListMapper();
        RealmResults<SongListRealm> all = realm.where(SongListRealm.class).findAll().sort("key");
        for (SongListRealm songListRealm : all) {
            if(songListRealm.getKey()==idLastSongListValue)
                continue;
            songs.add(songListMapper.fromRealm(songListRealm));
        }
        return songs;
    }

//    private void checkSongListFiles(SongListRealm songListRealm){
//        if(songListRealm.getSongList().size()!=0) {
//            for (SongRealm songRealm : songListRealm.getSongList()) {
//                File file = new File(songRealm.getPath());
//                if (!file.exists())
//                    deleteSongFromSonglist(songRealm.getKey(), songListRealm.getKey());
//            }
//        }
//    }

    public void deleteAllSongsByPath(final String path){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SongRealm> realms = realm.where(SongRealm.class).equalTo("path", path).findAll();
                realms.deleteAllFromRealm();
            }
        });
    }

    public void deleteSongFromSonglist(int songKey, int songListKey){
        realm.beginTransaction();

        SongRealm songRealm = realm.where(SongRealm.class).equalTo("key", songKey).findFirst();
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", songListKey).findFirst();
        for(int i =0; i<songListRealm.getSongList().size(); i++){
            SongRealm songRealmAtList = songListRealm.getSongList().get(i);
            if(songRealmAtList.getPath().equals(songRealm.getPath())){
                songListRealm.getSongList().remove(i);
            }
        }

        realm.commitTransaction();
        Log.d("SongListDao", "Deleted, size: " + songListRealm.getSongList().size());
    }

    public boolean songListContainsSong(int key, Song song){
        SongList songList = getSongListWithKey(key);
        for(Song songFromList: songList.getSongList()){
            if(songFromList.getId()==song.getId())
                return true;
        }
        return false;
    }

    public void insertSongToListWithKey(final int key, final Song song){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                SongRealm songRealmNew = createSongRealm(song);
                SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", key).findFirst();
                if (songListRealm != null) {
                    SongRealm savedSongRealm = realm.copyToRealmOrUpdate(songRealmNew);
                    RealmList<SongRealm> songs = songListRealm.getSongList();
                    if(!songs.contains(savedSongRealm)) {
                        songs.add(savedSongRealm);
                    }
                }
            }
        });
    }

    private SongRealm createSongRealm(Song song){
        SongRealm songRealmNew = realm.createObject(SongRealm.class, generateIdForSong());
        songRealmNew.setId(song.getId());
        songRealmNew.setTitle(song.getTitle());
        songRealmNew.setPath(song.getPath());
        songRealmNew.setArtist(song.getArtist());
        songRealmNew.setDuration(song.getDuration());
        return songRealmNew;
    }

    public SongList getSongListWithKey(int key){
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", key).findFirst();
        return songListMapper.fromRealm(songListRealm);
    }

    public SongList insertSongList(String title){
        realm.beginTransaction();

        SongListRealm songListRealm = realm.createObject(SongListRealm.class, generateIdForList());
        songListRealm.setTitle(title);
        realm.commitTransaction();
        return songListMapper.fromRealm(songListRealm);
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
        SongList songList = new SongList(new MusicListData(context).getSongList(), "All music", 0);
        if(songListRealm!=null && songListRealm.getSongList().size()>0){
            songList = songListMapper.fromRealm(songListRealm);
        }
        return songList;
    }

    public void changeLatestSongList(SongList songList){
        deleteLatestSongList();
        createLatestSongList(songList);
    }

    private void deleteLatestSongList(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(realm.where(SongListRealm.class).equalTo("key", idLastSongListValue).findFirst()!=null) {
                    realm.where(SongListRealm.class).equalTo("key", idLastSongListValue).findFirst().deleteFromRealm();
                }
            }
        });
    }

    private void createLatestSongList(SongList songList) {
        realm.beginTransaction();
        SongListRealm songListRealm = realm.createObject(SongListRealm.class, idLastSongListValue);
        songListRealm.setTitle(songList.getTitle());
        realm.commitTransaction();
        for(Song song : songList.getSongList()) {
            insertSongToListWithKey(songListRealm.getKey(), song);
        }
    }

    private int generateIdForList() {
        if(realm.where(SongListRealm.class).max("key")==null)
            return 0;
        return realm.where(SongListRealm.class).max("key").intValue() + 1;
    }

    private int generateIdForSong() {
        if(realm.where(SongRealm.class).max("key")==null)
            return 0;
        return realm.where(SongRealm.class).max("key").intValue() + 1;
    }
}
