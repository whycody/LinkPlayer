package com.linkplayer.linkplayer.dialog.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

public class AddNewPlaylistDialogFragment extends DialogFragment {

    private SongListDao songListDao;
    private Song song;
    private NewPlaylistInformator newPlaylistInformator;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = "Add new playlist";
        ConstraintLayout constraintLayout = new ConstraintLayout(getActivity());
        constraintLayout.setPadding(20,20,20,20);
        final EditText edittext = new EditText(getActivity());
        edittext.setLayoutParams(new ConstraintLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        constraintLayout.addView(edittext);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(constraintLayout)
                .setTitle(title)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(edittext.getText().toString())){
                            songListDao = new SongListDao(getActivity());
                            SongList songList = songListDao.insertSongList(edittext.getText().toString());
                            songListDao.insertSongToListWithKey(songList.getKey(), song);
                            newPlaylistInformator.notifyNewPlaylistAdded(true);
                            Toast.makeText(getActivity(), "Playlist added", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getActivity(), "Add a playlist title", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public void setSong(Song song){
        this.song = song;
    }

    public void setNewPlaylistInformator(NewPlaylistInformator newPlaylistInformator){
        this.newPlaylistInformator = newPlaylistInformator;
    }
}
