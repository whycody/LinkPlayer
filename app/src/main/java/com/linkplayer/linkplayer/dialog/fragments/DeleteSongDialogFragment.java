package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.linkplayer.linkplayer.fragment.music.MusicFragmentView;
import com.linkplayer.linkplayer.model.Song;

import java.io.File;

public class DeleteSongDialogFragment extends DialogFragment {

    private Song song;
    private MusicFragmentView fragmentView;
    private int position;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Delete song";
        String message = "Do you really want to delete file \"" + song.getPath() + "\" from the device memory?";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSong();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private void deleteSong(){
        boolean deleted;
        Uri uri = null;
        String authority = "com.linkplayer.linkplayer.fileprovider";
        File file = new File(song.getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                uri =  FileProvider.getUriForFile(getActivity(), authority, file);
                getActivity().getContentResolver().delete(uri, null, null);
                deleted = true;
            }catch(Exception e){
                deleted = false;
            }
        } else {
            deleted = file.getAbsoluteFile().delete();
        }

        if(deleted) {
            fragmentView.notifyItemDeleted(position);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            getActivity().sendBroadcast(intent);
        }else
            Toast.makeText(getActivity(), "Cannot delete", Toast.LENGTH_SHORT).show();
    }

    public void setSong(Song song){
        this.song = song;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setFragmentView(MusicFragmentView fragmentView){
        this.fragmentView = fragmentView;
    }
}