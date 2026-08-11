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
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // 1. Title
        JLabel titleLabel = new JLabel("PAC-MAN OOP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        this.add(titleLabel, gbc);

        // Level Selector
        String[] levels = {"level1.txt", "level2.txt"}; // Add more files here as you make them!
        JComboBox<String> levelSelector = new JComboBox<>(levels);
        levelSelector.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        this.add(levelSelector, gbc);

        // Difficulty
        String savedDifficulty = "Normal"; // Default fallback
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
        diffSelector.setFont(new Font("Arial", Font.BOLD, 20));
        diffSelector.setSelectedItem(savedDifficulty);
        diffSelector.setSelectedItem(window.getLastDifficulty());
        gbc.gridy = 2;
        this.add(diffSelector, gbc);


        // 2. Start Game Button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the string that the user selected in the dropdown
                String selectedLevel = (String) levelSelector.getSelectedItem();
                String selectedDiff = (String) diffSelector.getSelectedItem();
                // Pass it to the window!
                window.startGame(selectedLevel, selectedDiff);
            }
        });
        gbc.gridy = 3;
        this.add(startButton, gbc);

        // 3. Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridy = 3;
        this.add(exitButton, gbc);
    }
}