package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.data.SongListDao;
import com.linkplayer.linkplayer.fragment.playlist.PlaylistView;
import com.linkplayer.linkplayer.model.SongList;

public class DeletePlaylistDialogFragment extends DialogFragment {

    private SongList songList;
    private int position;
    private PlaylistView playlistView;

    private String CANCEL, REALLY_DELETE, PLAYLIST, PLAYLIST_DELETED, DELETE, DELETE_PLAYLIST;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        String message = REALLY_DELETE + songList.getTitle() + "\" " + PLAYLIST + "?";
        final SongListDao songListDao = new SongListDao(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(DELETE_PLAYLIST)
                .setMessage(message)
                .setPositiveButton(DELETE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        songListDao.deleteSongListById(songList.getKey());
                        playlistView.notifyItemDeleted(position);
                        Toast.makeText(getActivity(), PLAYLIST_DELETED, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(CANCEL, null);

        return builder.create();
    }

    private void initializeStrings() {
        CANCEL = getResources().getString(R.string.cancel);
        REALLY_DELETE = getResources().getString(R.string.really_delete);
        PLAYLIST = getResources().getString(R.string.playlist);
        PLAYLIST_DELETED = getResources().getString(R.string.playlist_deleted);
        DELETE = getResources().getString(R.string.delete);
        DELETE_PLAYLIST = getResources().getString(R.string.delete_playlist);
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
