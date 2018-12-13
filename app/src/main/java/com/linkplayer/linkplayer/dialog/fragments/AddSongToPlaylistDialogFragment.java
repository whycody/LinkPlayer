package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkplayer.linkplayer.R;
import com.linkplayer.linkplayer.main.add.song.to.playlist.AddSongToPlaylistAdapter;
import com.linkplayer.linkplayer.main.add.song.to.playlist.AddSongToPlaylistInformator;
import com.linkplayer.linkplayer.model.Song;

public class AddSongToPlaylistDialogFragment extends DialogFragment implements AddSongToPlaylistInformator {

    private AddSongToPlaylistAdapter adapter;
    private NewPlaylistInformator newPlaylistInformator;
    private Song song;

    private String CANCEL;
    private String CREATE_NEW;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        adapter.getPresenter().setAddSongToPlaylistInformator(this);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_songs_dialog, null);
        RecyclerView recyclerView = view.findViewById(R.id.add_songs_recycler);
        ((ViewGroup)recyclerView.getParent()).removeView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(recyclerView)
                .setNegativeButton(CANCEL, null)
                .setPositiveButton(CREATE_NEW, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddNewPlaylistDialogFragment dialogFragment = new AddNewPlaylistDialogFragment();
                        dialogFragment.setNewPlaylistInformator(newPlaylistInformator);
                        dialogFragment.setSong(song);
                        dialogFragment.show(getFragmentManager(), "AddNewPlaylistDialogFragment");
                    }
                });

        return builder.create();
    }

    private void initializeStrings(){
        CANCEL = getResources().getString(R.string.cancel);
        CREATE_NEW = getResources().getString(R.string.create_new);
    }

    public void setAdapter(AddSongToPlaylistAdapter adapter) {
        this.adapter = adapter;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setNewPlaylistInformator(NewPlaylistInformator newPlaylistInformator) {
        this.newPlaylistInformator = newPlaylistInformator;
    }

    @Override
    public void songAddedToPlaylist(boolean added) {
        dismiss();
    }

}
