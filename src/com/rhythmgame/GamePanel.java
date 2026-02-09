package com.rhythmgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements KeyListener {
    private Chart currentChart;
    private MusicPlayer musicPlayer;
    private ScoreManager scoreManager;
    
    private List<Note> activeNotes;
    private Timer gameTimer;
    private long gameStartTime;
    private boolean isPlaying;
    
    // 判定线位置
    private final int JUDGE_LINE_Y = 500;
    private final int[] LANE_X = {150, 250, 350, 450, 550, 650};
    private final char[] LANE_KEYS = {'D', 'F', 'J', 'K'};
    
    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        musicPlayer = new MusicPlayer();
        scoreManager = new ScoreManager();
        activeNotes = new ArrayList<>();
        isPlaying = false;
        
        gameTimer = new Timer(16, e -> update());
    }
    
    public void loadChart(String chartPath) {
        try {
            currentChart = ChartLoader.loadChart(chartPath);
            musicPlayer.loadMusic(currentChart.getMusicPath());
            JOptionPane.showMessageDialog(this, 
                "谱面加载成功!\n歌曲: " + currentChart.getSongName() +
                "\n难度: " + currentChart.getDifficulty() +
                "\nBPM: " + currentChart.getBpm());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "谱面加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void startGame() {
        if (currentChart == null) {
            JOptionPane.showMessageDialog(this, "请先选择歌曲!");
            return;
        }
        
        activeNotes.clear();
        activeNotes.addAll(currentChart.getNotes());
        scoreManager.reset();
        
        gameStartTime = System.currentTimeMillis();
        musicPlayer.play_your_dick();
        isPlaying = true;
        gameTimer.start();
        
        requestFocusInWindow();
    }
    
    private void update() {
        if (!isPlaying) return;
        
        long currentTime = System.currentTimeMillis() - gameStartTime;
        
        // 移除超时的音符
        Iterator<Note> iterator = activeNotes.iterator();
        while (iterator.hasNext()) {
            Note note = iterator.next();
            if (currentTime > note.getTime() + 500) { // 超过500ms未击打
                iterator.remove();
                scoreManager.addMiss();
            }
        }
        
        // 游戏结束判断
        if (activeNotes.isEmpty() && !musicPlayer.isPlaying()) {
            endGame();
        }
        
        repaint();
    }
    
    private void endGame() {
        isPlaying = false;
        gameTimer.stop();
        JOptionPane.showMessageDialog(this, 
            "游戏结束!\n" + scoreManager.getSummary());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制轨道
        g2d.setColor(new Color(50, 50, 50));
        for (int i = 0; i < 4; i++) {
            g2d.fillRect(LANE_X[i] - 25, 0, 50, getHeight());
        }
        
        // 绘制判定线
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(0, JUDGE_LINE_Y, getWidth(), JUDGE_LINE_Y);
        
        // 绘制按键提示
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        for (int i = 0; i < 4; i++) {
            g2d.drawString(String.valueOf(LANE_KEYS[i]), LANE_X[i] - 10, JUDGE_LINE_Y + 40);
        }
        
        // 绘制音符
        if (isPlaying) {
            long currentTime = System.currentTimeMillis() - gameStartTime;
            g2d.setColor(Color.CYAN);
            
            for (Note note : activeNotes) {
                long timeDiff = note.getTime() - currentTime;
                if (timeDiff < 2000 && timeDiff > -500) { // 显示2秒内的音符
                    int y = JUDGE_LINE_Y - (int)(timeDiff * 0.25); // 下落速度
                    int x = LANE_X[note.getLane()];
                    g2d.fillOval(x - 20, y - 20, 40, 40);
                }
            }
        }
        
        // 绘制分数
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("SCORE: " + scoreManager.getScore(), 20, 30);
        g2d.drawString("COMBO: " + scoreManager.getCombo(), 20, 60);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (!isPlaying) return;
        
        char key = Character.toUpperCase(e.getKeyChar());
        int lane = -1;
        
        for (int i = 0; i < LANE_KEYS.length; i++) {
            if (LANE_KEYS[i] == key) {
                lane = i;
                break;
            }
        }
        
        if (lane != -1) {
            checkHit(lane);
        }
    }
    
    private void checkHit(int lane) {
        long currentTime = System.currentTimeMillis() - gameStartTime;
        Note closestNote = null;
        long minDiff = Long.MAX_VALUE;
        
        for (Note note : activeNotes) {
            if (note.getLane() == lane) {
                long diff = Math.abs(note.getTime() - currentTime);
                if (diff < minDiff && diff < 150) { // 150ms判定窗口
                    minDiff = diff;
                    closestNote = note;
                }
            }
        }
        
        if (closestNote != null) {
            activeNotes.remove(closestNote);
            
            if (minDiff < 50) {
                scoreManager.addPerfect();
            } else if (minDiff < 100) {
                scoreManager.addGood();
            } else {
                scoreManager.addOk();
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}
