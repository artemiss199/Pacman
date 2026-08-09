import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        this.setTitle("PacMan");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Requirement 2b
        this.setResizable(false);
        GamePanel panel = new GamePanel();
        this.add(panel);

        // Calculate window size based on a 20x20 grid (20 * 32px = 640px)
        this.setSize(640, 640);
        this.setLocationRelativeTo(null); // Centers the window on screen
        this.setVisible(true);
    }
}