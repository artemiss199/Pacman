package logic;

public abstract class MovableObject extends GameObject {
    protected int speed;
    protected Direction currentDirection;

    public MovableObject(int x, int y, int speed) {
        super(x, y);
        this.speed = speed;
        this.currentDirection = Direction.NONE;
    }
    public Direction getDirection() { return currentDirection; }
    // forcing subclasses to define their own movement logic
    public abstract void update(Maze maze, ScoreManager scoreManager);
}