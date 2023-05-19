package com.vitargo.webradiotracker.db;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Song implements Serializable {

    private int id;
    private String artist;
    private String title;
    private String date;

    public Song() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isTheSameSong(Song song){
        return this.getArtist().equals(song.getArtist()) && this.getTitle().equals(song.getTitle());
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "Song {artist = " + this.getArtist() +
                ", title =  " + this.getTitle() + "}";
    }
}
