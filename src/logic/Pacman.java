package logic;

public class Pacman extends MovableObject {
    private Direction nextDirection;

    public Pacman(int x, int y) {
        super(x, y, 1);
        this.nextDirection = Direction.NONE; // first fixed
    }


    @Override
    public void update(Maze maze, ScoreManager scoreManager) {
        if (nextDirection == Direction.NONE) {
            return; // skip
        }

        int nextX = x;
        int nextY = y;

        // using speed so that we may want to change it using difficulty later
        if (nextDirection == Direction.UP) nextY -= speed;
        else if (nextDirection == Direction.DOWN) nextY += speed;
        else if (nextDirection == Direction.LEFT) nextX -= speed;
        else if (nextDirection == Direction.RIGHT) nextX += speed;

        if (maze.isValidMove(nextX, nextY)) {
            this.x = nextX;
            this.y = nextY;
            this.currentDirection = nextDirection;

            scoreManager.addScore(-1);
        }
    }
    public void setNextDirection(Direction d) {
        this.nextDirection = d;
    }
    @Override
    public void render() {
        System.out.print(" P "); // Replace Later for GUI
    }
}