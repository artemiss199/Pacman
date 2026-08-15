package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    private char[][] grid;
    private List<Pellet> pellets;

    public Maze(String filename) {
        pellets = new ArrayList<>();
        loadMap(filename);
    }

    private void loadMap(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            grid = new char[lines.size()][lines.get(0).length()];

            for (int y = 0; y < lines.size(); y++) {
                for (int x = 0; x < lines.get(y).length(); x++) {
                    char tile = lines.get(y).charAt(x);
                    grid[y][x] = tile;
                    if (tile == '.') pellets.add(new Pellet(x, y, false));
                    if (tile == 'O') pellets.add(new Pellet(x, y, true));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load map: " + filename);
        }
    }

    public boolean isValidMove(int x, int y) {
        if (    y < 0
                || y >= grid.length
                || x < 0
                || x >= grid[0].length)
            return false;           // Avoid going outside
        return grid[y][x] != '#';   // Avoid Passing Wall
    }

    public List<Pellet> getPellets() { return pellets; }
    public char[][] getGrid() {
        return this.grid;
    }
}