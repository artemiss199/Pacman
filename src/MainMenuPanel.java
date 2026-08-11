import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainMenuPanel extends JPanel {
    private GameWindow window;
    private final String SETTINGS_FILE = "settings.txt";

    public MainMenuPanel(GameWindow window) {
        this.window = window;
        this.setBackground(Color.BLACK);

        // 1. We just use a normal GridBagLayout for the main panel now
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Title
        JLabel titleLabel = new JLabel("<html><span style='text-shadow: 2px 2px #000000;'>PAC-MAN OOP</span></html>");
        titleLabel.setFont(FontManager.getFont(45f));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        this.add(titleLabel, gbc);

        // Level Selector
        String[] levels = {"level1.txt", "level2.txt"};
        JComboBox<String> levelSelector = new JComboBox<>(levels);
        levelSelector.setFont(FontManager.getFont(14f));
        gbc.gridy = 1;
        this.add(levelSelector, gbc);

        // Difficulty File IO
        String savedDifficulty = "Normal";
        try {
            File file = new File(SETTINGS_FILE);
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    savedDifficulty = line;
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println("No previous settings found, defaulting to Normal.");
        }

        // Difficulty Selector
        String[] difficulties = {"Easy", "Normal", "Hard"};
        JComboBox<String> diffSelector = new JComboBox<>(difficulties);
        diffSelector.setFont(FontManager.getFont(14f));

        if (window.getLastDifficulty() != null && !window.getLastDifficulty().equals("Normal")) {
            diffSelector.setSelectedItem(window.getLastDifficulty());
        } else {
            diffSelector.setSelectedItem(savedDifficulty);
        }

        gbc.gridy = 2;
        this.add(diffSelector, gbc);

        // Start Game Button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(FontManager.getFont(18f));
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLevel = (String) levelSelector.getSelectedItem();
                String selectedDiff = (String) diffSelector.getSelectedItem();
                window.startGame(selectedLevel, selectedDiff);
            }
        });
        gbc.gridy = 3;
        this.add(startButton, gbc);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(FontManager.getFont(18f));
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridy = 4;
        this.add(exitButton, gbc);

        // --- THE SCALED GIF COMPONENT ---
        // Instead of making it the background, it is now a dedicated video box!
        Image bgImage = new ImageIcon("Assets/main/video.gif").getImage();
        JPanel videoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, 300, getHeight(), this);
            }
        };
        // You can change these numbers to make the video bigger or smaller!
        videoPanel.setPreferredSize(new Dimension(450, 120));
        videoPanel.setBackground(Color.BLACK);

        gbc.gridy = 5; // Put it directly below the Exit button
        gbc.insets = new Insets(30, 10, 10, 10); // Add extra top padding so it doesn't touch the Exit button
        this.add(videoPanel, gbc);
    }
}