public class Pellet extends GameObject {
    private boolean isPowerPellet;

    public Pellet(int x, int y, boolean isPowerPellet) {
        super(x, y);
        this.isPowerPellet = isPowerPellet;
    }

    public boolean isPowerPellet() { return isPowerPellet; }

    @Override
    public void render() {
        if (isPowerPellet) System.out.print(" O ");
        else System.out.print(" . ");
    }
}