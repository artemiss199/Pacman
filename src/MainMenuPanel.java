import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMenuPanel extends JPanel {
    private GameWindow window;
    private final String SETTINGS_FILE = "settings.txt";

    public MainMenuPanel(GameWindow window) {
        this.window = window;
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;

        // Title
        JLabel titleLabel = new JLabel("<html><span style='text-shadow: 2px 2px #000000;'>PAC-MAN OOP</span></html>");
        titleLabel.setFont(FontManager.getFont(45f));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        this.add(titleLabel, gbc);

        // Scanning levels folder for maps
        File levelFolder = new File("levels");
        List<String> levelList = new ArrayList<>();

        if (levelFolder.exists() && levelFolder.isDirectory()) {
            // Find all files ending in .txt
            File[] txtFiles = levelFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (txtFiles != null) {
                for (File f : txtFiles) {
                    levelList.add(f.getName()); // Add just the file name (e.g., "level1.txt")
                }
            }
        }

        if (levelList.isEmpty()) {
            levelList.add("level1.txt");
        }

        // Sort the files based on level
        Collections.sort(levelList);

        JComboBox<String> levelSelector = new JComboBox<>(levelList.toArray(new String[0]));
        styleComboBox(levelSelector);
        if (window.getLastLevel() != null && levelList.contains(window.getLastLevel())) {
            levelSelector.setSelectedItem(window.getLastLevel());
        }

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
        styleComboBox(diffSelector);

        if (window.getLastDifficulty() != null && !window.getLastDifficulty().equals("Normal")) {
            diffSelector.setSelectedItem(window.getLastDifficulty());
        } else {
            diffSelector.setSelectedItem(savedDifficulty);
        }

        gbc.gridy = 2;
        this.add(diffSelector, gbc);

        // Start Game Button
        JButton startButton = new JButton("START GAME");
        styleButton(startButton);

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
        JButton exitButton = new JButton("EXIT");
        styleButton(exitButton);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridy = 4;
        this.add(exitButton, gbc);

        // GIF
        Image bgImage = new ImageIcon("Assets/main/video.gif").getImage();
        JPanel videoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, 300, getHeight(), this);
            }
        };
        videoPanel.setPreferredSize(new Dimension(450, 120));
        videoPanel.setBackground(Color.BLACK);

        gbc.gridy = 5;
        gbc.insets = new Insets(30, 10, 10, 10);
        this.add(videoPanel, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(FontManager.getFont(18f));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 3),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.YELLOW);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 3),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 3),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(FontManager.getFont(14f));
        comboBox.setBackground(Color.BLACK);
        comboBox.setForeground(Color.CYAN);
        comboBox.setFocusable(false);
        comboBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? Color.DARK_GRAY : Color.BLACK);
                label.setForeground(isSelected ? Color.YELLOW : Color.CYAN);
                label.setHorizontalAlignment(CENTER);
                return label;
            }
        });
    }
}