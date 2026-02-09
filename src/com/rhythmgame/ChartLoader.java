package com.rhythmgame;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChartLoader {
    
    public static Chart loadChart(String chartPath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(chartPath)));
        JSONObject json = new JSONObject(content);
        
        String songName = json.getString("song_name");
        String artist = json.getString("artist");
        int bpm = json.getInt("bpm");
        String difficulty = json.getString("difficulty");
        String musicPath = json.getString("music_path");
        
        List<Note> notes = new ArrayList<>();
        JSONArray notesArray = json.getJSONArray("notes");
        
        for (int i = 0; i < notesArray.length(); i++) {
            JSONObject noteObj = notesArray.getJSONObject(i);
            long time = noteObj.getLong("time");
            int lane = noteObj.getInt("lane");
            notes.add(new Note(time, lane));
        }
        
        return new Chart(songName, artist, bpm, difficulty, musicPath, notes);
    }
}
