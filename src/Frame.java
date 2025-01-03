import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private JLabel pointsLabel;
    private JLabel levelLabel;
    private JLabel timeLabel;
    private GamePanel gamePanel;
    private JButton menuButton;

    public Frame() {
        this.setTitle("Matematyczny snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        // Inicjalizacja gamePanel
        gamePanel = new GamePanel();
        this.add(gamePanel, BorderLayout.CENTER);

        // Dodanie menu
        JPanel menuPanel = createMenuPanel();
        this.add(menuPanel, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    // Tworzenie dolnego menu
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(1024, 100));
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        // Punkty
        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuPanel.add(pointsLabel);

        // Poziom
        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuPanel.add(levelLabel);

        // Czas
        timeLabel = new JLabel("Time: 0s");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuPanel.add(timeLabel);

        // Przycisk menu
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 18));
        menuButton.addActionListener(e -> showMenu());
        menuPanel.add(menuButton); // Dodanie przycisku do panelu

        return menuPanel;
    }


    // Metoda do wyświetlenia menu po kliknięciu przycisku
    private void showMenu() {
        String[] options = {"Pause", "Resume", "Restart", "Exit"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "What would you like to do?",
                "Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 0 -> gamePanel.pauseGame(); // Wstrzymaj grę
            case 1 -> gamePanel.resumeGame(); // Wznów grę
            case 2 -> gamePanel.restartGame(); // Zacznij od nowa
            case 3 -> gamePanel.endGame(); // Zakończ grę
        }
    }

    // Aktualizacja punktów i poziomu (do wywołania z GamePanel)
    public void updateStats(int points, int level, long elapsedTime) {
        pointsLabel.setText("Points: " + points);
        levelLabel.setText("Level: " + level);
        timeLabel.setText("Time: " + elapsedTime + "s");
    }
}
