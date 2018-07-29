package com.linkplayer.linkplayer.model;

public class AddSongItem {

    private Song song;
    private boolean checked;

    public AddSongItem(){

    }

    public AddSongItem(Song song, boolean checked) {
        this.song = song;
        this.checked = checked;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
