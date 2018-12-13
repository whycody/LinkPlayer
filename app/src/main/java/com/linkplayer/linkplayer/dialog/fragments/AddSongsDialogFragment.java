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
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsInformator;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsPresenter;
import com.linkplayer.linkplayer.fragment.playlist.add.songs.AddSongsRecyclerAdapter;
import com.linkplayer.linkplayer.model.SongList;

public class AddSongsDialogFragment extends DialogFragment{

    private AddSongsRecyclerAdapter recyclerAdapter;
    private AddSongsPresenter addSongsPresenter;
    private SongList songList;
    private int position;
    private AddSongsInformator addSongsInformator;

    private String CANCEL, ADD;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeStrings();
        addSongsPresenter.setAddSongsInformator(addSongsInformator);
        addSongsPresenter.setPosition(position);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_songs_dialog, null);
        setRecyclerViewProperties(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(ADD, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addSongsPresenter.addSongsToPlaylist(songList);
                    }
                }).setNegativeButton(CANCEL, null);

        return builder.create();
    }

    private void initializeStrings(){
        CANCEL = getResources().getString(R.string.cancel);
        ADD = getResources().getString(R.string.add);
    }

    private void setRecyclerViewProperties(View view){
        RecyclerView recyclerView = view.findViewById(R.id.add_songs_recycler);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
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

    public void setPosition(int position){
        this.position = position;
    }

    public void setAddSongInformator(AddSongsInformator addSongInformator){
        this.addSongsInformator = addSongInformator;
    }

}
