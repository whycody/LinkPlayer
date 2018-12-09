package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.Song;

import java.io.File;

public class SetSongAsRingstoneDialogFragment extends DialogFragment {

    private String RINGTONE, WANT_SET_SONG, AS_RINGTONE, CANCEL, OK;
    private Song song;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        String message = WANT_SET_SONG + song.getTitle() + AS_RINGTONE;

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle(RINGTONE)
                .setMessage(message)
                .setNegativeButton(CANCEL, null)
                .setPositiveButton(OK, setSongAsRingtoneOnClick);
        return dialog.create();
    }

    private DialogInterface.OnClickListener setSongAsRingtoneOnClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try{
                String filepath =song.getPath();
                File ringtoneFile = new File(filepath);

                ContentValues content = new ContentValues();
                content.put(MediaStore.MediaColumns.DATA,ringtoneFile.getAbsolutePath());
                content.put(MediaStore.MediaColumns.TITLE, song.getTitle());
                content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
                content.put(MediaStore.Audio.Media.ARTIST, song.getArtist());
                content.put(MediaStore.Audio.Media.DURATION, Integer.parseInt(song.getDuration()));
                content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                content.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                content.put(MediaStore.Audio.Media.IS_ALARM, false);
                content.put(MediaStore.Audio.Media.IS_MUSIC, false);

                Uri uri = MediaStore.Audio.Media.getContentUriForPath(
                        ringtoneFile.getAbsolutePath());
                Uri newUri = getActivity().getContentResolver().insert(uri, content);
                RingtoneManager.setActualDefaultRingtoneUri(getActivity(),
                        RingtoneManager.TYPE_RINGTONE,newUri);
            }catch(Exception exc){
                Log.d("MYTAG", exc.toString());
            }
        }
    };

    private void initializeStrings(){
        RINGTONE = getActivity().getResources().getString(R.string.ringtone);
        WANT_SET_SONG = getActivity().getResources().getString(R.string.want_set_song);
        AS_RINGTONE = getActivity().getResources().getString(R.string.as_ringtone);
        CANCEL = getActivity().getResources().getString(R.string.cancel);
        OK = getActivity().getResources().getString(R.string.ok);
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
