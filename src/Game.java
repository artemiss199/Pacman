import java.util.ArrayList;
import java.util.List;

public class Game {
    private Maze maze;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private ScoreManager scoreManager;
    private boolean isGameOver;

    public Game(String levelFile) {
        maze = new Maze(levelFile);
        scoreManager = new ScoreManager();
        ghosts = new ArrayList<>();
        pacman = new Pacman(1, 1);
        ghosts.add(new Ghost(10, 10, "RED"));
        ghosts.add(new Ghost(11, 10, "PINK"));
        isGameOver = false;
    }

    public void tick() {
        if (isGameOver) return;

        // 1. Update Entities
        pacman.update(maze, scoreManager);
        for (Ghost ghost : ghosts) {
            ghost.update(maze, scoreManager);
        }

        // 2. Check Collisions (Requirement 7)
        checkCollisions();

        // 3. Check Win Condition
        if (maze.getPellets().isEmpty()) {
            System.out.println("YOU WIN! All beans collected.");
            scoreManager.addScore(500); // Requirement 4e
            scoreManager.checkAndSaveHighScore();
            isGameOver = true;
        }
    }

    private void checkCollisions() {
        // Pacman eats Pellets (Requirement 7c)
        Pellet eatenPellet = null;
        for (Pellet p : maze.getPellets()) {
            if (p.isActive() && p.getX() == pacman.getX() && p.getY() == pacman.getY()) {
                eatenPellet = p;
                p.setActive(false);
                scoreManager.addScore(10); // Requirement 4c

                if (p.isPowerPellet()) {
                    for (Ghost g : ghosts) g.setFrightened(); // Requirement 10
                }
                break; // Can only eat one per tick
            }
        }
        if (eatenPellet != null) maze.getPellets().remove(eatenPellet);

        // Pacman hits Ghost (Requirement 6a)
        for (Ghost g : ghosts) {
            if (g.getX() == pacman.getX() && g.getY() == pacman.getY()) {
                System.out.println("GAME OVER! Ghost caught Pacman.");
                scoreManager.checkAndSaveHighScore();
                isGameOver = true;
            }
        }
    }

    public Maze getMaze() {
        return this.maze;
    }

    public Pacman getPacman() {
        return this.pacman;
    }

    public List<Ghost> getGhosts() {
        return this.ghosts;
    }

    public ScoreManager getScoreManager() {
        return this.scoreManager;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }
}