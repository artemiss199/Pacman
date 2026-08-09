public class Ghost extends MovableObject {
    private String color;
    private GhostState state;

    public Ghost(int x, int y, String color) {
        super(x, y, 1);
        this.color = color;
        this.state = GhostState.SCATTER;
    }

    public void setFrightened() {
        this.state = GhostState.FRIGHTENED;
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
    }

    private void calculateRandomPath(Maze maze) {
        // Fallback/erratic movement
    }

    @Override
    public void render() {
        System.out.print(" G ");
    }

    public GhostState getState() {
        return this.state;
    }

    public String getColorName() {
        return this.color;
    }
}