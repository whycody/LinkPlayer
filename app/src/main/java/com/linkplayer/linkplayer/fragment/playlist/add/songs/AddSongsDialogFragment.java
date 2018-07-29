package com.linkplayer.linkplayer.fragment.playlist.add.songs;

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
import com.linkplayer.linkplayer.model.Song;
import com.linkplayer.linkplayer.model.SongList;

public class AddSongsDialogFragment extends DialogFragment{

    private AddSongsRecyclerAdapter recyclerAdapter;
    private AddSongsPresenter addSongsPresenter;
    private SongList songList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = "Choose songs";

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_songs_dialog, null);

        RecyclerView recyclerView = view.findViewById(R.id.add_songs_recycler);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(title)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addSongsPresenter.addSongsToPlaylist(songList);
                    }
                }).setNegativeButton("Cancel", null);

        Dialog dialog = builder.create();

        return dialog;
    }

    public void setRecyclerAdapter(AddSongsRecyclerAdapter recyclerAdapter){
        this.recyclerAdapter = recyclerAdapter;
    }

    public void setAddSongsPresenter(AddSongsPresenter addSongsPresenter){
        this.addSongsPresenter = addSongsPresenter;
    }

    public void setSongList(SongList songList){
        this.songList = songList;
    }
}
