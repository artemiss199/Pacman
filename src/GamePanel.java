import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// extends JPanel, implements interfaces for input and looping
public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Game game;
    private Timer timer;
    private final int TILE_SIZE = 32; // Size of each grid square in pixels
    private final GameWindow window;

    public GamePanel(GameWindow window) {
        this.setFocusable(true); // Allows the panel to receive keyboard input
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.window = window;

        this.game = new Game("level1.txt");
        this.timer = new Timer(150, this);
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Draw Map/Maze
        char[][] grid = game.getMaze().getGrid();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == '#') {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // 2. Draw Pellets
        g.setColor(Color.WHITE);
        for (Pellet p : game.getMaze().getPellets()) {
            if (p.isActive()) {
                int size = p.isPowerPellet() ? 16 : 6;
                int offset = (TILE_SIZE - size) / 2;
                g.fillOval((p.getX() * TILE_SIZE) + offset, (p.getY() * TILE_SIZE) + offset, size, size);
            }
        }

        // 3. Draw Pacman
        g.setColor(Color.YELLOW);
        g.fillArc(game.getPacman().getX() * TILE_SIZE, game.getPacman().getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE, 45, 270);

        // 4. Draw Ghosts
        for (Ghost ghost : game.getGhosts()) {
            if (ghost.getState() == GhostState.FRIGHTENED) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(ghost.getColorName().equals("RED") ? Color.RED : Color.PINK);
            }
            g.fillRect(ghost.getX() * TILE_SIZE, ghost.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + game.getScoreManager().getCurrentScore(), 10, 20);

        if (game.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 100, 200);
        }
    }

    // Loop
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!game.isGameOver()) {
            game.tick(); // Update backend logic
        }
        repaint(); // Force paintComponent to run again with new coordinates
    }

    // Input (Requirement 3a)
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            game.getPacman().setNextDirection(Direction.UP);
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            game.getPacman().setNextDirection(Direction.DOWN);
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            game.getPacman().setNextDirection(Direction.LEFT);
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            game.getPacman().setNextDirection(Direction.RIGHT);
        } else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0); // Requirement 2b: Leaving the app
        }

        else if (key == KeyEvent.VK_R) {
            // restart the game by  overwriting the old game object with a new one
            this.game = new Game("level1.txt");
        }

        else if (key == KeyEvent.VK_M) {
            this.timer.stop();
            window.showMainMenu();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}