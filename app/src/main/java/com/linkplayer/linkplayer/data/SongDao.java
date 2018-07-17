package com.linkplayer.linkplayer.data;

import android.content.Context;
import android.widget.Toast;

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

    public void editSongListTitle(int key, String title){
        realm.beginTransaction();
        realm.where(SongListRealm.class).equalTo("key", key).findFirst().setTitle(title);
        realm.commitTransaction();
    }

    public void insertSongList(Song song, String title){
        realm.beginTransaction();

        SongRealm songRealm = songMapper.toRealm(song);
        SongListRealm songListRealm = realm.createObject(SongListRealm.class, generateIdForList());
        songListRealm.addSong(songRealm);
        songListRealm.setTitle(title);

        realm.commitTransaction();
    }

    public void insertSongToListNamed(String name, Song song){
        realm.beginTransaction();

        SongRealm songRealm = songMapper.toRealm(song);
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("name", name).findFirst();
        songListRealm.addSong(songRealm);

        realm.commitTransaction();
    }

    public void deleteSongListById(final long key) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(SongListRealm.class).equalTo("key", key).findFirst().deleteFromRealm();
            }
        });
    }

    public List<Song> getAllSongs() {
        List<Song> notes = new ArrayList<>();

        RealmResults<SongRealm> all = realm.where(SongRealm.class).findAll().sort("id");
        for (SongRealm noteRealm : all) {
            notes.add(songMapper.fromRealm(noteRealm));
        }

        return notes;
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
        songRealm.setAlbumPhotoAvailable(song.isAlbumPhotoAvailable());
        songRealm.setArtist(song.getArtist());
        songRealm.setId(song.getId());
        songRealm.setPath(song.getPath());
        songRealm.setTitle(song.getTitle());
    }

    public Song getLatestMusic(){
        int idLastSongValue = 1999999999;
        SongRealm songRealm = realm.where(SongRealm.class).equalTo("key", idLastSongValue).findFirst();
        if(songRealm==null) {
            return new Song(0l, "Play a song!", "", "", null, false);
        }
        Song song = songMapper.fromRealm(songRealm);
        return song;
    }

    private int generateIdForList() {
        if(realm.where(SongListRealm.class).max("key")==null)
            return 0;

        return realm.where(SongListRealm.class).max("key").intValue() + 1;
    }
}
