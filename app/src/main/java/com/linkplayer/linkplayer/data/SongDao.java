package com.linkplayer.linkplayer.data;

import android.content.Context;

import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.mappers.SongListMapper;
import com.linkplayer.linkplayer.model.SongListRealm;
import com.linkplayer.linkplayer.mappers.SongMapper;
import com.linkplayer.linkplayer.model.SongRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SongDao {

    private Realm realm;
    private Context context;
    private SongMapper songMapper = new SongMapper();

    public SongDao(Context context){
        this.context = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }


    public Song getSongById(int id){
        SongRealm songRealm = realm.where(SongRealm.class).equalTo("id", id).findFirst();
        return songMapper.fromRealm(songRealm);
    }

    public List<Song> getAllSongs() {
        List<Song> notes = new ArrayList<>();

        RealmResults<SongRealm> all = realm.where(SongRealm.class).findAll().sort("id");
        for (SongRealm noteRealm : all) {
            notes.add(songMapper.fromRealm(noteRealm));
        }

        return notes;
    }

    private int idLastSongValue = 1999999999;

    public void changeLatestMusic(Song song){
        deleteLatestMusic();
        realm.beginTransaction();
        createLatestMusic(song);
        realm.commitTransaction();
    }

    private void deleteLatestMusic(){
        if(realm.where(SongRealm.class).equalTo("key", idLastSongValue).findFirst()!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(SongRealm.class).equalTo("key", idLastSongValue).findFirst().deleteFromRealm();
                }
            });
        }
    }

    private void createLatestMusic(Song song){
        SongRealm songRealm = realm.createObject(SongRealm.class, idLastSongValue);
        songRealm.setArtist(song.getArtist());
        songRealm.setId(song.getId());
        songRealm.setPath(song.getPath());
        songRealm.setTitle(song.getTitle());
    }

    public Song getLatestMusic(){
        SongRealm songRealm = realm.where(SongRealm.class).equalTo("key", idLastSongValue).findFirst();
        if(songRealm==null) {
            return new Song(0l, "Play a song!", "", "", "");
        }
        Song song = songMapper.fromRealm(songRealm);
        return song;
    }
}
