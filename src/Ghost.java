import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost extends MovableObject {
    private String color;
    private GhostState state;
    private Random random; // new random direction


    private final int startX;
    private final int startY;


    public Ghost(int x, int y, String color) {
        super(x, y, 1);
        this.startX = x;
        this.startY = y;
        this.color = color;
        this.state = GhostState.CHASE;
        this.random = new Random();
    }

    public void setNormal() {
        this.state = GhostState.CHASE;
    }

    public void setFrightened() {
        this.state = GhostState.FRIGHTENED;
    }

    public void respawn() {
        this.x = startX;
        this.y = startY;
        this.state = GhostState.CHASE;
    }

    @Override
    public void update(Maze maze, ScoreManager scoreManager) {
        // Requirement 14: AI Implementation
        if (state == GhostState.CHASE) {
            calculateAStarPath(maze); // Seek Pacman
        } else if (state == GhostState.FRIGHTENED) {
            calculateRandomPath(maze); // Run away
        }
    }

    private void calculateAStarPath(Maze maze) {
        // TODO: Implement A* or BFS algorithm here for routing
        calculateRandomPath(maze);
    }

    private void calculateRandomPath(Maze maze) {
        int nextX = x;
        int nextY = y;

        if (currentDirection == Direction.UP) nextY -= speed;
        else if (currentDirection == Direction.DOWN) nextY += speed;
        else if (currentDirection == Direction.LEFT) nextX -= speed;
        else if (currentDirection == Direction.RIGHT) nextX += speed;

        if (currentDirection == Direction.NONE || !maze.isValidMove(nextX, nextY)) {
            List<Direction> possibleMoves = new ArrayList<>();
            if (maze.isValidMove(x, y - speed)) possibleMoves.add(Direction.UP);
            if (maze.isValidMove(x, y + speed)) possibleMoves.add(Direction.DOWN);
            if (maze.isValidMove(x - speed, y)) possibleMoves.add(Direction.LEFT);
            if (maze.isValidMove(x + speed, y)) possibleMoves.add(Direction.RIGHT);
            if (!possibleMoves.isEmpty()) {
                this.currentDirection = possibleMoves.get(random.nextInt(possibleMoves.size()));
            }
        } else {
            this.x = nextX;
            this.y = nextY;
        }
    }

    @Override
    public void render() {
        System.out.print(" G ");
    }
    public GhostState getState() {return this.state;}
    public String getColorName() {return this.color;}
}