import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        this.setTitle("PacMan");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Requirement 2b
        this.setSize(640, 640);
        this.setLocationRelativeTo(null); // Centers the window on screen

        showMainMenu();
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

    public void startGame() {
        this.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel);
        this.revalidate();
        this.repaint();
        gamePanel.requestFocus();
    }
}
