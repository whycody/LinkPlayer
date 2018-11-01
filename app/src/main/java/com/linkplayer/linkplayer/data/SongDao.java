package com.linkplayer.linkplayer.data;

import android.content.Context;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.mappers.SongMapper;
import com.linkplayer.linkplayer.model.SongRealm;

import io.realm.Realm;

public class SongDao {

    private Realm realm;
    private Context context;
    private SongMapper songMapper = new SongMapper();
    private final int LAST_SONG_VALUE_ID = 3;

    public SongDao(Context context){
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void changeLatestSong(Song song){
        deleteLatestSong();
        createLatestSong(song);
    }

    private void deleteLatestSong(){
        if(realm.where(SongRealm.class).equalTo("key", LAST_SONG_VALUE_ID).findFirst()!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(SongRealm.class).equalTo("key", LAST_SONG_VALUE_ID).findFirst().deleteFromRealm();
                }
            });
        }
    }

    private void createLatestSong(Song song){
        realm.beginTransaction();
        createSongRealmObject(song);
        realm.commitTransaction();
    }

    private void createSongRealmObject(Song song){
        SongRealm songRealm = realm.createObject(SongRealm.class, LAST_SONG_VALUE_ID);
        songRealm.setArtist(song.getArtist());
        songRealm.setId(song.getId());
        songRealm.setPath(song.getPath());
        songRealm.setTitle(song.getTitle());
        songRealm.setDuration(song.getDuration());
    }

    public Song getLatestSong(){
        SongRealm songRealm = realm.where(SongRealm.class).equalTo("key", LAST_SONG_VALUE_ID).findFirst();
        if(songRealm==null) {
            return new MusicListData(context).getSongList().get(0);
        }
        return songMapper.fromRealm(songRealm);
    }
}
