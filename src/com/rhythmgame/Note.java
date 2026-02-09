package com.rhythmgame;

public class Note {
    private long time; // 音符时间(毫秒)
    private int lane;  // 轨道(0-3)
    
    public Note(long time, int lane) {
        this.time = time;
        this.lane = lane;
    }
    
    public long getTime() {
        return time;
    }
    
    public int getLane() {
        return lane;
    }
}
