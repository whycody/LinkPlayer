package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.Song;

import java.io.File;

public class DeleteSongDialogFragment extends DialogFragment {

    private Song song;
    private DeleteSongInformator deleteSongInformator;
    private int position;
    private final String TAG = "DeleteSongDialog";

    private String DELETE_SONG, REALLY_DELETE_FILE, FROM_DEVICE, DELETE, CANCEL, CANNOT_DELETE;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        String message = REALLY_DELETE_FILE + song.getPath() + FROM_DEVICE;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(DELETE_SONG)
                .setMessage(message)
                .setPositiveButton(DELETE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSongs();
                    }
                }).setNegativeButton(CANCEL, null);
        return builder.create();
    }

    private void initializeStrings(){
        DELETE_SONG = getResources().getString(R.string.delete_song);
        REALLY_DELETE_FILE = getResources().getString(R.string.really_delete_file);
        FROM_DEVICE = getResources().getString(R.string.from_device);
        DELETE = getResources().getString(R.string.delete);
        CANCEL = getResources().getString(R.string.cancel);
        CANNOT_DELETE = getResources().getString(R.string.cannot_delete);
    }

    private void deleteSongs(){
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getId());
        getActivity().getContentResolver().delete(uri, null, null);
        File newSongFile = new File(uri.getPath());
        if(!newSongFile.exists()){
            notifySongDeleted();
        }else{
            if(newSongFile.delete())
                notifySongDeleted();
            else{
                Toast.makeText(getActivity(), CANNOT_DELETE, Toast.LENGTH_SHORT).show();
                deleteSongInformator.notifySongDeleted(position, false);
            }
        }
    }

    private void notifySongDeleted(){
        SongListDao songListDao = new SongListDao(getActivity());
        songListDao.deleteAllSongsByPath(song.getPath());
        deleteSongInformator.notifySongDeleted(position, true);
    }

    public void setSong(Song song){
        this.song = song;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setInformator(DeleteSongInformator deleteSongInformator){
        this.deleteSongInformator = deleteSongInformator;
    }

}
