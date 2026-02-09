package com.rhythmgame;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;
    private JComboBox<String> songSelector;
    
    public GameWindow() {
        setTitle("===Rhythm Game===");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("选择歌曲: "));
        
        songSelector = new JComboBox<>();
        loadSongList();
        songSelector.addActionListener(e -> loadSelectedSong());
        topPanel.add(songSelector);
        
        JButton startButton = new JButton("开始游戏");
        startButton.addActionListener(e -> startGame());
        topPanel.add(startButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // panel
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);
    }
    
    private void loadSongList() {
        File songsDir = new File("songs");
        if (songsDir.exists() && songsDir.isDirectory()) {
            File[] songFolders = songsDir.listFiles(File::isDirectory);
            if (songFolders != null) {
                for (File folder : songFolders) {
                    songSelector.addItem(folder.getName());
                }
            }
        }
    }
    
    private void loadSelectedSong() {
        String selected = (String) songSelector.getSelectedItem();
        if (selected != null) {
            gamePanel.loadChart("songs/" + selected + "/chart.json");
        }
    }
    
    private void startGame() {
        gamePanel.startGame();
    }
}
