package logic;

import java.util.*;

public class Ghost extends MovableObject {
    private String color;
    private GhostState state;
    private Random random; // new random direction


    private final int startX;
    private final int startY;
    private final Pacman target;
    private int mistakeChance;

    public Ghost(int x, int y, String color, Pacman target, String difficulty) {
        super(x, y, 1);
        this.startX = x;
        this.startY = y;
        this.color = color;
        this.state = GhostState.CHASE;
        this.random = new Random();
        this.target = target;

        // Set the nerf based on what the player chose!
        if (difficulty.equals("Easy")) {
            this.mistakeChance = 90; // 90% chance to act dumb (choose random path)
        } else if (difficulty.equals("Normal")) {
            this.mistakeChance = 60; // 60% chance to act dumb
        } else if (difficulty.equals("Hard")) {
            this.mistakeChance = 0;  // 0% chance (Death mode!)
        } else {
            this.mistakeChance = 25; // Fallback
        }


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
        if (state == GhostState.CHASE) {
            if (random.nextInt(100) < mistakeChance) {
                calculateRandomPath(maze); // Act dumb and choose random path
            } else {
                calculateAStarPath(maze); // Seek logic.Pacman using A* algorithm
            }
        } else if (state == GhostState.FRIGHTENED) {
            calculateRandomPath(maze); // Run away using random path selection for ease of catching
        }
    }

    private class Node implements Comparable<Node> { // node for A* algorithm implementation
        int x, y;
        int gCost; // Steps taken from the start
        int hCost; // Manhattan distance to the target
        Node parent; // for trace back

        public Node(int x, int y, int gCost, int hCost, Node parent) {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
        }

        public int getFCost() { return gCost + hCost; }

        @Override
        public int compareTo(Node other) {
            int compare = Integer.compare(this.getFCost(), other.getFCost());
            if (compare == 0) return Integer.compare(this.hCost, other.hCost);
            return compare;
        }
    }

    private void calculateAStarPath(Maze maze) {
        int targetX = target.getX();
        int targetY = target.getY();
        Set<String> closedList = new HashSet<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        openList.add(new Node(this.x, this.y, 0, getManhattanDistance(this.x, this.y, targetX, targetY), null));
        Node targetNode = null;
        Direction oppositeDir = getOpposite(currentDirection);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.x == targetX && current.y == targetY) { // found pacman
                targetNode = current;
                break;
            }

            String key = current.x + "," + current.y;
            if (closedList.contains(key)) continue;
            closedList.add(key);

            // 3. Evaluate 4 Neighbors (Up, Down, Left, Right)
            int[][] directions = {{0, -speed}, {0, speed}, {-speed, 0}, {speed, 0}};
            Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

            for (int i = 0; i < 4; i++) {
                int nx = current.x + directions[i][0];
                int ny = current.y + directions[i][1];
                Direction moveDir = dirs[i];

                if (current.parent == null && moveDir == oppositeDir) continue;
                String neighborKey = nx + "," + ny;

                if (maze.isValidMove(nx, ny) && !closedList.contains(neighborKey)) {
                    int newGCost = current.gCost + 1;
                    int newHCost = getManhattanDistance(nx, ny, targetX, targetY);
                    openList.add(new Node(nx, ny, newGCost, newHCost, current));
                }
            }
        }

        // best move
        if (targetNode != null) {
            Node nextStep = targetNode;

            // Backtrack until we find the node right after the logic.Ghost's current position
            while (nextStep.parent != null && nextStep.parent.parent != null) {
                nextStep = nextStep.parent;
            }

            if (nextStep.x > this.x) this.currentDirection = Direction.RIGHT;
            else if (nextStep.x < this.x) this.currentDirection = Direction.LEFT;
            else if (nextStep.y > this.y) this.currentDirection = Direction.DOWN;
            else if (nextStep.y < this.y) this.currentDirection = Direction.UP;

            this.x = nextStep.x;
            this.y = nextStep.y;
        } else {
            calculateRandomPath(maze); // for fallbakck
        }
    }

    private int getManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private Direction getOpposite(Direction d) {
        if (d == Direction.UP) return Direction.DOWN;
        if (d == Direction.DOWN) return Direction.UP;
        if (d == Direction.LEFT) return Direction.RIGHT;
        if (d == Direction.RIGHT) return Direction.LEFT;
        return Direction.NONE;
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