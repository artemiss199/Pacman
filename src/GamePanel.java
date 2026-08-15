import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import java.io.File;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int lastRenderedLevel = 1;
    private Image blinkyImg, pinkyImg, inkyImg, clydeImg, frightenedImg;
    private Image dotImg, powerPelletImg;
    private Image[][] pacmanImgs;
    private int animTick = 0;

    private Game game;
    private Timer timer;
    private StatsPanel statsPanel;
    private final int TILE_SIZE = 32;
    private final GameWindow window;
    private String currentLevelFile;
    private String currentDifficulty;

    private JButton nextLevelButton;

    public GamePanel(GameWindow window, String levelFile, String difficulty) {
        this.window = window;
        this.currentLevelFile = levelFile;
        this.currentDifficulty = difficulty;

        this.setLayout(null);
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);

        loadImages();

        this.game = new Game("levels/" + currentLevelFile, currentDifficulty);

        setupNextLevelButton();

        this.timer = new Timer(150, this);
        this.timer.start();
        updateWindowSize();
    }

    private void setupNextLevelButton() {
        nextLevelButton = new JButton("NEXT LEVEL");
        nextLevelButton.setFont(FontManager.getFont(18f));
        nextLevelButton.setBackground(Color.BLACK);
        nextLevelButton.setForeground(Color.CYAN);
        nextLevelButton.setFocusPainted(false);
        nextLevelButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.CYAN, 3),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        nextLevelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextLevelButton.setVisible(false);

        nextLevelButton.addActionListener(e -> {
            int nextLvl = game.getCurrentLevel() + 1;
            String nextFile = "level" + nextLvl + ".txt";

            File checkFile = new File("levels/" + nextFile);
            if (!checkFile.exists()) {
                System.out.println("Final level cleared! Wrapping back to level 1.");
                nextFile = "level1.txt";
            }

            window.startGame(nextFile, currentDifficulty);
        });

        nextLevelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nextLevelButton.setForeground(Color.YELLOW);
                nextLevelButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 3),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                nextLevelButton.setForeground(Color.CYAN);
                nextLevelButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.CYAN, 3),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        this.add(nextLevelButton);
    }

    private void loadImages() {
        try {
            blinkyImg = ImageIO.read(new File("Assets/pacman-art/ghosts/blinky.png"));
            pinkyImg = ImageIO.read(new File("Assets/pacman-art/ghosts/pinky.png"));
            frightenedImg = ImageIO.read(new File("Assets/pacman-art/ghosts/blue_ghost.png"));
            inkyImg = ImageIO.read(new File("Assets/pacman-art/ghosts/inky.png"));
            clydeImg = ImageIO.read(new File("Assets/pacman-art/ghosts/clyde.png"));
            dotImg = ImageIO.read(new File("Assets/pacman-art/other/dot.png"));
            powerPelletImg = ImageIO.read(new File("Assets/pacman-art/other/apple.png"));

            pacmanImgs = new Image[4][3];
            String[] dirs = {"up", "down", "left", "right"};

            for (int d = 0; d < 4; d++) {
                for (int f = 1; f <= 3; f++) {
                    String path = "Assets/pacman-art/pacman-" + dirs[d] + "/" + f + ".png";
                    pacmanImgs[d][f-1] = ImageIO.read(new File(path));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load images.");
        }
    }

    private void updateWindowSize() {
        char[][] grid = game.getMaze().getGrid();
        int mapWidth = grid[0].length * TILE_SIZE;
        int mapHeight = grid.length * TILE_SIZE;

        this.setPreferredSize(new Dimension(mapWidth, mapHeight));
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
        // we use G2D for better walls grahic
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        char[][] grid = game.getMaze().getGrid();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == '#') {
                    // 1. Draw the thick outer glow
                    g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.setColor(new Color(0, 0, 150)); // Dark Blue Glow
                    drawNeonWall(g2d, x, y, grid); // decides on shape
                    g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.setColor(new Color(50, 150, 255)); // Bright Cyan Core
                    drawNeonWall(g2d, x, y, grid);
                }
            }
        }

        // Draw Pellets
        for (Pellet p : game.getMaze().getPellets()) {
            if (p.isActive()) {
                Image imgToDraw = p.isPowerPellet() ? powerPelletImg : dotImg;
                g.drawImage(imgToDraw, p.getX() * TILE_SIZE, p.getY() * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE, null);
            }
        }

        // Draw Pacman
        int dirIdx = getDirIndex(game.getPacman().getDirection());
        int frameIdx = animTick % 3; // for mouth animation

        g.drawImage(pacmanImgs[dirIdx][frameIdx],
                game.getPacman().getX() * TILE_SIZE,
                game.getPacman().getY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE, null);

        // Draw Ghosts
        for (Ghost ghost : game.getGhosts()) {
            Image ghostImg = blinkyImg;

            if (ghost.getState() == GhostState.FRIGHTENED || game.isPaused()) {
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

        FontMetrics fm;

        if (game.isGameOver()) {
            boolean won = game.getMaze().getPellets().isEmpty();

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());

            if (won) {
                String title = "LEVEL CLEARED!";
                g.setFont(FontManager.getFont(32f));
                g.setColor(Color.GREEN);
                fm = g.getFontMetrics();
                g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 40);

                if (!nextLevelButton.isVisible()) {
                    int btnWidth = 250;
                    int btnHeight = 50;
                    nextLevelButton.setBounds((getWidth() - btnWidth) / 2, getHeight() / 2, btnWidth, btnHeight);
                    nextLevelButton.setVisible(true);
                }
            } else {
                String title = "WASTED";
                g.setFont(FontManager.getFont(45f));
                g.setColor(Color.RED);
                fm = g.getFontMetrics();
                g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 20);

                String sub1 = "PRESS 'R' TO RESTART";
                String sub2 = "PRESS 'M' FOR MENU";

                g.setFont(FontManager.getFont(14f));
                g.setColor(Color.WHITE);
                fm = g.getFontMetrics();

                g.drawString(sub1, (getWidth() - fm.stringWidth(sub1)) / 2, getHeight() / 2 + 40);
                g.drawString(sub2, (getWidth() - fm.stringWidth(sub2)) / 2, getHeight() / 2 + 70);
            }
        }
        else if (game.isPaused()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());

            String title = "PAUSED";
            g.setFont(FontManager.getFont(40f));
            g.setColor(Color.CYAN);
            fm = g.getFontMetrics();
            g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 20);

            String sub1 = "PRESS 'ESC' TO RESUME";
            g.setFont(FontManager.getFont(14f));
            g.setColor(Color.WHITE);
            fm = g.getFontMetrics();
            g.drawString(sub1, (getWidth() - fm.stringWidth(sub1)) / 2, getHeight() / 2 + 30);
        }
        else {
            g.setColor(Color.WHITE);
            g.setFont(FontManager.getFont(14f));
            g.drawString("Score: " + game.getScoreManager().getCurrentScore(), 10, 25);
        }
    }
    // More beautiful wall using G2D
    private void drawNeonWall(Graphics2D g2d, int x, int y, char[][] grid) {
        int px = x * TILE_SIZE;
        int py = y * TILE_SIZE;

        int margin = 8;
        int size = TILE_SIZE;

        // Calculate the inner hollow rectangle bounds
        int x1 = px + margin;
        int y1 = py + margin;
        int x2 = px + size - margin;
        int y2 = py + size - margin;

        // Check neighboring tiles to see if they are also walls
        boolean up = (y > 0 && grid[y - 1][x] == '#');
        boolean down = (y < grid.length - 1 && grid[y + 1][x] == '#');
        boolean left = (x > 0 && grid[y][x - 1] == '#');
        boolean right = (x < grid[0].length - 1 && grid[y][x + 1] == '#');

        // Draw Top Edge
        if (!up) {
            g2d.drawLine(x1, y1, x2, y1);
        } else {
            g2d.drawLine(x1, py, x1, y1); // Open up to connect upwards
            g2d.drawLine(x2, py, x2, y1);
        }

        // Draw Bottom Edge
        if (!down) {
            g2d.drawLine(x1, y2, x2, y2);
        } else {
            g2d.drawLine(x1, y2, x1, py + size);
            g2d.drawLine(x2, y2, x2, py + size);
        }

        // Draw Left Edge
        if (!left) {
            g2d.drawLine(x1, y1, x1, y2);
        } else {
            g2d.drawLine(px, y1, x1, y1);
            g2d.drawLine(px, y2, x1, y2);
        }

        // Draw Right Edge
        if (!right) {
            g2d.drawLine(x2, y1, x2, y2);
        } else {
            g2d.drawLine(x2, y1, px + size, y1);
            g2d.drawLine(x2, y2, px + size, y2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!game.isGameOver()) {
            game.tick();
            if (!game.isPaused()) {
                animTick++;
            }
        }
        if (game.getCurrentLevel() != lastRenderedLevel) {
            lastRenderedLevel = game.getCurrentLevel();
            updateWindowSize();
        }

        if (statsPanel != null) {
            statsPanel.updateStats();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) {
            game.togglePause();
        }
        else if (!game.isPaused()) {
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                game.getPacman().setNextDirection(Direction.UP);
            } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                game.getPacman().setNextDirection(Direction.DOWN);
            } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                game.getPacman().setNextDirection(Direction.LEFT);
            } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                game.getPacman().setNextDirection(Direction.RIGHT);
            }
        }

        if (key == KeyEvent.VK_R) {
            this.game = new Game("levels/" + currentLevelFile, currentDifficulty);
            nextLevelButton.setVisible(false);
            updateWindowSize();
        } else if (key == KeyEvent.VK_M) {
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