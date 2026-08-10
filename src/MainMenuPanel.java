import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private GameWindow window;

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

        // --- NEW: LEVEL SELECTOR DROPDOWN ---
        String[] levels = {"level1.txt", "level2.txt"}; // Add more files here as you make them!
        JComboBox<String> levelSelector = new JComboBox<>(levels);
        levelSelector.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        this.add(levelSelector, gbc);

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
                // Pass it to the window!
                window.startGame(selectedLevel);
            }
        });
        gbc.gridy = 2;
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