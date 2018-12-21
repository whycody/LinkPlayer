package com.linkplayer.linkplayer.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.linkplayer.linkplayer.mappers.SongListMapper;
import com.linkplayer.linkplayer.mappers.SongMapper;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;
import com.linkplayer.linkplayer.model.SongListRealm;
import com.linkplayer.linkplayer.model.SongRealm;
import com.linkplayer.linkplayer.playlist.view.PlaylistViewActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SongListDao {

    private Realm realm;
    private Context context;
    private SongMapper songMapper;
    private SongListMapper songListMapper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferencesEditor;
    private MusicListData musicListData;

    public static final String LATEST_SONGLIST_TYPE = "latestSonglistType";
    public static final String LATEST_SONGLIST_KEY = "latestSonglistKey";
    public static final String LATEST_SONGLIST_TITLE = "latestSonglistTitle";
    public static final String UNASSIGNED = "unassigned";

    public SongListDao(Context context){
        this.context = context;
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);
        songMapper = new SongMapper();
        songListMapper = new SongListMapper();
        sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        preferencesEditor = sharedPreferences.edit();
        musicListData = new MusicListData(context);
    }

    public ArrayList<SongList> getAllTheSongLists() {
        ArrayList<SongList> songs = new ArrayList<>();
        SongListMapper songListMapper = new SongListMapper();
        RealmResults<SongListRealm> all = realm.where(SongListRealm.class).findAll().sort("key");
        for (SongListRealm songListRealm : all) {
            songs.add(songListMapper.fromRealm(songListRealm));
        }
        return songs;
    }

    public void deleteAllSongsByPath(final String path){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SongRealm> realms = realm.where(SongRealm.class).equalTo("path", path).findAll();
                realms.deleteAllFromRealm();
            }
        });
    }

    public void changeSongID(final long oldID, final long newID){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SongRealm> realms = realm.where(SongRealm.class).equalTo("id", oldID).findAll();
                for(SongRealm songRealm: realms){
                    songRealm.setId(newID);
                    SongRealm newSongRealm = realm.copyToRealmOrUpdate(songRealm);
                }
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

    public void insertSongsToListWithKey(final int key, final ArrayList<Song> songList){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm r) {
                ArrayList<Song> songListRealmNew = (ArrayList<Song>)songList.clone();
                SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", key).findFirst();
                if (songListRealm != null) {
                    RealmList<SongRealm> songs = songListRealm.getSongList();
                    for(Song song: songListRealmNew) {
                        SongRealm songRealm = createSongRealm(song);
                        SongRealm savedSongRealm = realm.copyToRealmOrUpdate(songRealm);
                        if (!songs.contains(savedSongRealm)) {
                            songs.add(savedSongRealm);
                        }
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
        songRealmNew.setDateModified(song.getDateModified());
        return songRealmNew;
    }

    public SongList getSongListWithKey(int key){
        SongListRealm songListRealm = realm.where(SongListRealm.class).equalTo("key", key).findFirst();
        if(songListRealm!=null)
            return songListMapper.fromRealm(songListRealm);
        else
            return musicListData.getAllMusicSongList();
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

    public SongList getLatestSongList(){
        SongList songList;

        String type = sharedPreferences.getString(LATEST_SONGLIST_TYPE, UNASSIGNED);
        String title = sharedPreferences.getString(LATEST_SONGLIST_TITLE, UNASSIGNED);
        int key = sharedPreferences.getInt(LATEST_SONGLIST_KEY, 0);

        if(type.equals(UNASSIGNED) || type.equals(PlaylistViewActivity.ALL_SONGS_TYPE))
            return musicListData.getAllMusicSongList();

        songList = type.equals(PlaylistViewActivity.PLAYLIST_TYPE) ? getSongListWithKey(key) :
                musicListData.getArtistSongList(title);
        return songList.getSongList().size()>0 ? songList : musicListData.getAllMusicSongList();
    }

    public void changeLatestSongList(SongList songList){
        if(songList.getKey()!=0) {
            preferencesEditor.putString(LATEST_SONGLIST_TYPE, PlaylistViewActivity.PLAYLIST_TYPE);
            preferencesEditor.putInt(LATEST_SONGLIST_KEY, songList.getKey());
        }else if(!songList.getTitle().equals(MusicListData.ALL_MUSIC_SONGLIST)){
            preferencesEditor.putString(LATEST_SONGLIST_TYPE, PlaylistViewActivity.ARTIST_TYPE);
            preferencesEditor.putString(LATEST_SONGLIST_TITLE, songList.getTitle());
        }else{
            preferencesEditor.putString(LATEST_SONGLIST_TYPE, PlaylistViewActivity.ALL_SONGS_TYPE);
        }
        preferencesEditor.apply();
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
