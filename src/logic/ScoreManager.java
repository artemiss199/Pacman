package logic;

import java.io.*;

public class ScoreManager {
    private int currentScore;
    private int highScore;
    private final String SCORE_FILE = "highscore.txt";

    private void loadHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            highScore = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            highScore = 0; // if we couldn't find score file then highScore is considered zero
        }
    }

    public ScoreManager() {
        this.currentScore = 0;
        loadHighScore();
    }

    public void addScore(int points) {
        currentScore += points;
    }

    public int getCurrentScore() { return currentScore; }

    public void checkAndSaveHighScore() {
        if (currentScore > highScore) {
            highScore = currentScore;
            try (PrintWriter out = new PrintWriter(new FileWriter(SCORE_FILE))) {
                out.println(highScore);
            } catch (IOException e) {
                System.out.println("Error saving high score.");
            }
        }
    }

    public int getHighScore() { return highScore; }

}