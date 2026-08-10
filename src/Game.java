import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    private Maze maze;
    private Pacman pacman;
    private List<Ghost> ghosts;
    private ScoreManager scoreManager;
    private SoundManager soundManager;
    private boolean isGameOver;
    private int powerPelletTimer = 0;
    private int currentLevel;
    public Game(String levelFile) {
        maze = new Maze(levelFile);
        scoreManager = new ScoreManager();
        soundManager = new SoundManager();
        ghosts = new ArrayList<>();
        pacman = new Pacman(1, 1);
        ghosts.add(new Ghost(10, 10, "RED",pacman));
        ghosts.add(new Ghost(11, 10, "PINK",pacman));
        isGameOver = false;
        soundManager.playSound("beginning");
        this.currentLevel = 1;
        Matcher matcher = Pattern.compile("\\d+").matcher(levelFile);
        while (matcher.find()) {
            this.currentLevel = Integer.parseInt(matcher.group());
        }
    }

    public void tick() {
        if (isGameOver) return;

        // 1. Update Entities
        pacman.update(maze, scoreManager);

        checkCollisions();
        if (isGameOver) return;

        for (Ghost ghost : ghosts) {
            ghost.update(maze, scoreManager);
        }

        // 2. Check Collisions (Requirement 7)
        checkCollisions();// updates powerPelletTimer on collision with powerPellet

        if (powerPelletTimer > 0) {
            powerPelletTimer--;
            if (powerPelletTimer == 0) {
                for (Ghost g : ghosts) {
                    g.setNormal();
                }
            }
        }

        // 3. Check Win Condition
        if (maze.getPellets().isEmpty()) {
            System.out.println("YOU WIN! All beans collected.");
            scoreManager.addScore(500); // Requirement 4e
            scoreManager.checkAndSaveHighScore();
            isGameOver = true;
        }
    }

    private void checkCollisions() {
        checkPelletCollisions();
        checkGhostCollisions();
    }

    private void checkPelletCollisions() { //(Requirement 7c)
        Pellet eatenPellet = null;
        for (Pellet p : maze.getPellets()) {
            if (p.isActive() && p.getX() == pacman.getX() && p.getY() == pacman.getY()) {
                eatenPellet = p;
                p.setActive(false);
                scoreManager.addScore(10); // Requirement 4c

                if (p.isPowerPellet()) {
                    soundManager.playSound("eatfruit");
                    for (Ghost g : ghosts) g.setFrightened(); // Requirement 10
                    powerPelletTimer = 40;
                } else {
                    soundManager.playSound("chomp");
                }
                break;
            }
        }
        if (eatenPellet != null) maze.getPellets().remove(eatenPellet);
    }

    private void checkGhostCollisions() {
        // Pacman hits Ghost (Requirement 6a)
        for (Ghost g : ghosts) {
            if (g.getX() == pacman.getX() && g.getY() == pacman.getY()) {
                if (g.getState() == GhostState.FRIGHTENED) {
                    soundManager.playSound("eatghost");
                    scoreManager.addScore(200);
                    g.respawn();
                } else {
                    soundManager.playSound("death");
                    System.out.println("GAME OVER! Ghost caught Pacman.");
                    scoreManager.checkAndSaveHighScore();
                    isGameOver = true;
                }

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

    public int getCurrentLevel() {
        return this.currentLevel;
    }


}