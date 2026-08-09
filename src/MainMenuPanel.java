import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private GameWindow window; // Reference to our main frame to trigger panel switching

    public MainMenuPanel(GameWindow window) {
        this.window = window;
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout()); // GridBagLayout automatically centers components

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Adds padding between buttons
        gbc.gridx = 0; // Everything in a single vertical column

        // 1. The Title
        JLabel titleLabel = new JLabel("PAC-MAN OOP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        this.add(titleLabel, gbc);

        // 2. Start Game Button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.startGame(); // Tells the window to switch panels
            }
        });
        gbc.gridy = 1;
        this.add(startButton, gbc);

        // 3. Exit Button (Requirement 2b)
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Closes the application completely
            }
        });
        gbc.gridy = 2;
        this.add(exitButton, gbc);
    }
}