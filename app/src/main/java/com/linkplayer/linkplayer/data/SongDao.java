package com.linkplayer.linkplayer.data;

import android.content.Context;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.mappers.SongMapper;
import com.linkplayer.linkplayer.model.SongRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SongDao {

    private Realm realm;
    private Context context;
    private SongMapper songMapper;

    public SongDao(Context context){
        Realm.init(context);
        this.context = context;
        songMapper = new SongMapper();
        realm = Realm.getDefaultInstance();
    }

    private final int LAST_SONG_VALUE_ID = 3;

    public void changeLatestMusic(Song song){
        deleteLatestMusic();
        realm.beginTransaction();
        createLatestMusic(song);
        realm.commitTransaction();
    }

    private void deleteLatestMusic(){
        if(realm.where(SongRealm.class).equalTo("key", LAST_SONG_VALUE_ID).findFirst()!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(SongRealm.class).equalTo("key", LAST_SONG_VALUE_ID).findFirst().deleteFromRealm();
                }
            });
        }
    }

    private void createLatestMusic(Song song){
        SongRealm songRealm = realm.createObject(SongRealm.class, LAST_SONG_VALUE_ID);
        songRealm.setArtist(song.getArtist());
        songRealm.setId(song.getId());
        songRealm.setPath(song.getPath());
        songRealm.setTitle(song.getTitle());
        songRealm.setDuration(song.getDuration());
    }

    public Song getLatestMusic(){
        SongRealm songRealm = realm.where(SongRealm.class).equalTo("key", LAST_SONG_VALUE_ID).findFirst();
        if(songRealm==null) {
            return new MusicListData(context).getSongList().get(0);
        }
        return songMapper.fromRealm(songRealm);
    }
}
