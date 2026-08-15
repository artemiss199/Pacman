package main;

import presentation.GameWindow;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameWindow(); // Requirement 2a: logic.Game Start
            }
        });
    }
}