package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistView;
import com.linkplayer.linkplayer.model.SongList;

public class DeletePlaylistDialogFragment extends DialogFragment {

    private SongList songList;
    private int position;
    private PlaylistView playlistView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Delete playlist";
        String message = "Are you really want to delete \"" + songList.getTitle() + "\" playlist?";
        final SongListDao songListDao = new SongListDao(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        songListDao.deleteSongListById(songList.getKey());
                        playlistView.notifyItemDeleted(position);
                        Toast.makeText(getActivity(), "Playlist deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", null);


        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public void setSongList(SongList songList){
        this.songList = songList;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setPlaylistView(PlaylistView playlistView) {
        this.playlistView = playlistView;
    }
}
