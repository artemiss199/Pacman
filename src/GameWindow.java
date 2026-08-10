import javax.swing.JFrame;

public class GameWindow extends JFrame {
    SoundManager sound;
    public GameWindow(SoundManager sound) {}
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

    public void startGame(String levelFile) {
        this.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(this, levelFile);
        this.add(gamePanel);
        this.pack();
        this.revalidate();
        this.repaint();
        gamePanel.requestFocus();
    }
}
