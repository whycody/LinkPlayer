package com.linkplayer.linkplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
        File file = new File(song.getPath());
        boolean deleted = file.getAbsoluteFile().delete();
        if(deleted)
            fragmentView.notifyItemDeleted(position);
        else
            Toast.makeText(getActivity(), file.exists() + "", Toast.LENGTH_SHORT).show();
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
