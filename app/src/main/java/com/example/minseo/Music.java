package com.example.minseo;

import java.util.Objects;

public class Music {
    private String title;
    private String artist;
    private int imageResource;
    private int audioResourceId;

    public Music(String title, String artist, int imageResource, int audioResourceId) {
        this.title = title;
        this.artist = artist;
        this.imageResource = imageResource;
        this.audioResourceId = audioResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getAudioResourceId() {
        return audioResourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return title.equals(music.title) && artist.equals(music.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }
}
