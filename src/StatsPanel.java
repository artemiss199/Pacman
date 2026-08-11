import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private JLabel scoreLabel, highScoreLabel, levelLabel, diffLabel, deathLabel;
    private Game game;
    private FontMetrics fm;

    public StatsPanel(Game game) {
        this.game = game;
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridLayout(1, 5, 10, 0));
        this.setPreferredSize(new Dimension(640, 40)); // 40 pixels tall

        Font font = FontManager.getFont(10f);
        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(font);

        highScoreLabel = new JLabel("", SwingConstants.CENTER);
        highScoreLabel.setForeground(Color.YELLOW);
        highScoreLabel.setFont(font);

        levelLabel = new JLabel("", SwingConstants.CENTER);
        levelLabel.setForeground(Color.CYAN);
        levelLabel.setFont(font);

        diffLabel = new JLabel("", SwingConstants.CENTER);
        diffLabel.setForeground(Color.GREEN);
        diffLabel.setFont(font);

        deathLabel = new JLabel("", SwingConstants.CENTER);
        deathLabel.setForeground(Color.RED);
        deathLabel.setFont(font);

        this.add(scoreLabel);
        this.add(highScoreLabel);
        this.add(levelLabel);
        this.add(diffLabel);
        this.add(deathLabel);

        updateStats();
    }

    public void updateStats() {
        scoreLabel.setText("Score: " + game.getScoreManager().getCurrentScore());
        highScoreLabel.setText("High Score: " + game.getScoreManager().getHighScore());
        levelLabel.setText("Level: " + game.getCurrentLevel());
        diffLabel.setText("Diff: " + game.getDifficulty());
        deathLabel.setText("Deaths: " + game.getDeathTracker().getDeaths());
    }

    // Used when the player presses 'R' to restart the game
    public void setGame(Game newGame) {
        this.game = newGame;
        updateStats();
    }
}