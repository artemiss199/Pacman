import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// to add assets
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;

// extends JPanel, implements interfaces for input and looping
public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int lastRenderedLevel = 1;
    private Image blinkyImg, pinkyImg, inkyImg, clydeImg, frightenedImg;
    private Image dotImg, powerPelletImg;
    private Image[][] pacmanImgs; // 2D array: [direction][animation_frame]
    private int animTick = 0; // Used to cycle through 1.png, 2.png, 3.png

    private Game game;
    private Timer timer;
    private StatsPanel statsPanel;
    private final int TILE_SIZE = 32; // Size of each grid square in pixels
    private final GameWindow window;
    private String currentLevelFile;
    private String currentDifficulty;


    // Construcctor
    public GamePanel(GameWindow window,String levelFile, String difficulty) {
        this.window = window;
        this.currentLevelFile = levelFile;
        this.currentDifficulty = difficulty;

        this.setFocusable(true); // Allows the panel to receive keyboard input
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);

        loadImages();

        this.game = new Game(currentLevelFile, currentDifficulty);
        this.timer = new Timer(150, this);
        this.timer.start();
        updateWindowSize();
    }


    private void loadImages() {
        try {
            // Load Ghosts
            blinkyImg = ImageIO.read(new File("Assets/pacman-art/ghosts/blinky.png"));
            pinkyImg = ImageIO.read(new File("Assets/pacman-art/ghosts/pinky.png"));
            frightenedImg = ImageIO.read(new File("Assets/pacman-art/ghosts/blue_ghost.png"));
            inkyImg = ImageIO.read(new File("Assets/pacman-art/ghosts/inky.png"));
            clydeImg = ImageIO.read(new File("Assets/pacman-art/ghosts/clyde.png"));
            // Load Items (Using Apple for Power Pellet)
            dotImg = ImageIO.read(new File("Assets/pacman-art/other/dot.png"));
            powerPelletImg = ImageIO.read(new File("Assets/pacman-art/other/apple.png"));

            // Load Pac-Man Animation Frames (4 directions, 3 frames each)
            pacmanImgs = new Image[4][3];
            String[] dirs = {"up", "down", "left", "right"};

            for (int d = 0; d < 4; d++) {
                for (int f = 1; f <= 3; f++) {
                    String path = "Assets/pacman-art/pacman-" + dirs[d] + "/" + f + ".png";
                    pacmanImgs[d][f-1] = ImageIO.read(new File(path));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load images. Check your folder paths!");
            e.printStackTrace();
        }
    }

    private void updateWindowSize() {
        char[][] grid = game.getMaze().getGrid();
        int mapWidth = grid[0].length * TILE_SIZE;
        int mapHeight = grid.length * TILE_SIZE;

        // Tell the panel how big it needs to be to fit the map
        this.setPreferredSize(new Dimension(mapWidth, mapHeight));

        // Tell the window to snap to this new size and re-center on the screen!
        window.pack();
        window.setLocationRelativeTo(null);
    }

    private int getDirIndex(Direction d) {
        if (d == Direction.UP) return 0;
        if (d == Direction.DOWN) return 1;
        if (d == Direction.LEFT) return 2;
        return 3;
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
        for (Pellet p : game.getMaze().getPellets()) {
            if (p.isActive()) {
                Image imgToDraw = p.isPowerPellet() ? powerPelletImg : dotImg;
                g.drawImage(imgToDraw, p.getX() * TILE_SIZE, p.getY() * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE, null);
            }
        }

        // 3. Draw Pacman
        int dirIdx = getDirIndex(game.getPacman().getDirection());
        int frameIdx = animTick % 3; // Loops between 0, 1, and 2

        g.drawImage(pacmanImgs[dirIdx][frameIdx],
                game.getPacman().getX() * TILE_SIZE,
                game.getPacman().getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE, null);

        // 4. Draw Ghosts
        for (Ghost ghost : game.getGhosts()) {
            Image ghostImg = blinkyImg; // Default

            if (ghost.getState() == GhostState.FRIGHTENED) {
                ghostImg = frightenedImg;
            } else {
                String color = ghost.getColorName();
                if (color.equals("PINK")) ghostImg = pinkyImg;
                else if (color.equals("CYAN")) ghostImg = inkyImg;
                else if (color.equals("ORANGE")) ghostImg = clydeImg;
            }

            g.drawImage(ghostImg,
                    ghost.getX() * TILE_SIZE,
                    ghost.getY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + game.getScoreManager().getCurrentScore(), 10, 20);

        if (game.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Wasted", 300, 200);
        }
    }

    // Loop
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!game.isGameOver()) {
            game.tick();
            animTick++;
        }
        if (game.getCurrentLevel() != lastRenderedLevel) {
            lastRenderedLevel = game.getCurrentLevel();
            updateWindowSize();
        }

        if (statsPanel != null) {
            statsPanel.updateStats();
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
            this.game = new Game(currentLevelFile, currentDifficulty);
            updateWindowSize();
        }

        else if (key == KeyEvent.VK_M) {
            this.timer.stop();
            window.showMainMenu();
        }

        if (statsPanel != null) {
            statsPanel.setGame(this.game);
        }
    }

    public void setStatsPanel(StatsPanel statsPanel) {
        this.statsPanel = statsPanel;
    }

    public Game getGame() {
        return this.game;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}