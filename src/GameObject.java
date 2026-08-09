public abstract class GameObject {
    protected int x;
    protected int y;
    protected boolean isActive;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}