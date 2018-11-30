package com.linkplayer.linkplayer.dialog.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

public class AddSongToPlaylistDialogFragment extends DialogFragment implements AddSongToPlaylistInformator {

    private AddSongToPlaylistAdapter adapter;

    private String CANCEL;

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
                .setNegativeButton(CANCEL, null);

        return builder.create();
    }

    public void setAdapter(AddSongToPlaylistAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void songAddedToPlaylist(boolean added) {
        dismiss();
    }

    private void initializeStrings(){
        CANCEL = getResources().getString(R.string.cancel);
    }
}
