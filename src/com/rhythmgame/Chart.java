package com.rhythmgame;

import java.util.List;

public class Chart {
    private String songName;
    private String artist;
    private int bpm;
    private String difficulty;
    private String musicPath;
    private List<Note> notes;
    
    public Chart(String songName, String artist, int bpm, String difficulty, 
                 String musicPath, List<Note> notes) {
        this.songName = songName;
        this.artist = artist;
        this.bpm = bpm;
        this.difficulty = difficulty;
        this.musicPath = musicPath;
        this.notes = notes;
    }
    
    public String getSongName() { return songName; }
    public String getArtist() { return artist; }
    public int getBpm() { return bpm; }
    public String getDifficulty() { return difficulty; }
    public String getMusicPath() { return musicPath; }
    public List<Note> getNotes() { return notes; }
}
