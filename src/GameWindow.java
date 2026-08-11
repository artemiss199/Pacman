import javax.swing.JFrame;

public class GameWindow extends JFrame {
    SoundManager sound;
    public GameWindow(SoundManager sound) {}
    private String lastLevel = "level1.txt";
    private String lastDifficulty = "Normal";

    public GameWindow() {
        this.setTitle("PacMan");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Requirement 2b
        this.setSize(640, 640);
        this.setLocationRelativeTo(null); // Centers the window on screen
        this.sound = new SoundManager();

        showMainMenu();
        sound.playSound("menu");
        this.setVisible(true);
    }

    public void showMainMenu() {
        // clear anything currently in the window
        this.getContentPane().removeAll();
        this.add(new MainMenuPanel(this));
        // Force Swing to redraw the window with the new components
        this.revalidate();
        this.repaint();
    }

    public void startGame(String levelFile, String difficulty) {
        this.lastLevel = levelFile;
        this.lastDifficulty = difficulty;

        this.getContentPane().removeAll();
        this.setLayout(new java.awt.BorderLayout());

        GamePanel gamePanel = new GamePanel(this, levelFile, difficulty);
        this.add(gamePanel, java.awt.BorderLayout.CENTER);

        StatsPanel statsPanel = new StatsPanel(gamePanel.getGame());
        this.add(statsPanel, java.awt.BorderLayout.SOUTH);
        gamePanel.setStatsPanel(statsPanel);

        this.pack();
        this.revalidate();
        this.repaint();
        gamePanel.requestFocus();
    }
    public String getLastLevel() { return lastLevel; }
    public String getLastDifficulty() { return lastDifficulty; }
}
