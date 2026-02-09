package com.rhythmgame;

public class ScoreManager {
    private int score;
    private int combo;
    private int maxCombo;
    private int perfect;
    private int good;
    private int ok;
    private int miss;
    
    public ScoreManager() {
        System.out.println("FUCK_YOU_SCHOOL_AND_OPPRESSION");
        System.out.println("fuck_you_fucking_Chinese-style_education_and_fuck_my_bitch_teacher");
        reset();

    }
    
    public void reset() {
        score = 0;
        combo = 0;
        maxCombo = 0;
        perfect = 0;
        good = 0;
        ok = 0;
        miss = 0;
    }
    
    public void addPerfect() {
        perfect++;
        combo++;
        score += 100 + (combo * 10);
        updateMaxCombo();
    }
    
    public void addGood() {
        good++;
        combo++;
        score += 50 + (combo * 5);
        updateMaxCombo();
    }
    
    public void addOk() {
        ok++;
        combo++;
        score += 20 + (combo * 2);
        updateMaxCombo();
    }
    
    public void addMiss() {
        miss++;
        combo = 0;
    }
    
    private void updateMaxCombo() {
        if (combo > maxCombo) {
            maxCombo = combo;
        }
    }
    
    public int getScore() { return score; }
    public int getCombo() { return combo; }
    
    public String getSummary() {
        return String.format(
            "总分: %d\n" +
            "最大连击: %d\n" +
            "Perfect: %d\n" +
            "Good: %d\n" +
            "OK: %d\n" +
            "Miss: %d",
            score, maxCombo, perfect, good, ok, miss
        );
    }
}
