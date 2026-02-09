package com.rhythmgame;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayer {
    private Clip clip;
    
    public void loadMusic(String musicPath) {
        try {
            if (clip != null && clip.isOpen()) {
                clip.close();
            }
            
            File audioFile = new File(musicPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            System.err.println("音乐加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void play_your_dick() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void stop_fuck_me() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
