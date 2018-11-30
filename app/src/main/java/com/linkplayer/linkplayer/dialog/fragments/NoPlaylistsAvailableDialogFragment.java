package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.model.Song;

public class NoPlaylistsAvailableDialogFragment extends DialogFragment {

    private Song song;
    private NewPlaylistInformator informator;

    private String CANCEL, OK, ANY_PLAYLIST, LETS_CREATE_NEW;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(getMessage())
                .setNegativeButton(CANCEL, null)
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAddNewPlaylistDialogFragment();
                    }
                });
        return builder.create();
    }

    private String getMessage() {
        return ANY_PLAYLIST + "\"" + song.getTitle() + "\"" + LETS_CREATE_NEW;
    }

    private void showAddNewPlaylistDialogFragment(){
        AddNewPlaylistDialogFragment dialogFragment = new AddNewPlaylistDialogFragment();
        dialogFragment.setSong(song);
        dialogFragment.setNewPlaylistInformator(informator);
        dialogFragment.show(getFragmentManager(), "AddNewPlaylistDialogFragment");
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setInformator(NewPlaylistInformator informator) {
        this.informator = informator;
    }

    private void initializeStrings(){
        CANCEL = getResources().getString(R.string.cancel);
        OK = getResources().getString(R.string.ok);
        ANY_PLAYLIST = getResources().getString(R.string.any_playlist);
        LETS_CREATE_NEW = getResources().getString(R.string.lets_create_new);
    }
}
