import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private JLabel pointsLabel;
    private JLabel levelLabel;
    private JLabel timeLabel;
    private GamePanel gamePanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public Frame() {
        this.setTitle("Matematyczny snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tworzenie panelu gry
        gamePanel = new GamePanel();

        // Tworzenie panelu startowego
        JPanel startPanel = createStartPanel();

        // Dodanie paneli do głównego panelu z CardLayout
        mainPanel.add(startPanel, "StartPanel");
        mainPanel.add(gamePanel, "GamePanel");

        // Dodanie głównego panelu do JFrame
        this.add(mainPanel, BorderLayout.CENTER);

        // Dodanie dolnego menu
        JPanel menuPanel = createMenuPanel();
        this.add(menuPanel, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel createStartPanel() {
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.setBackground(Color.BLACK);

        JButton startButton = new JButton("START");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startPanel.add(startButton);

        // Akcja po kliknięciu przycisku START
        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "GamePanel"); // Przełączenie na panel gry
            gamePanel.requestFocusInWindow();       // Ustaw fokus na GamePanel
            gamePanel.startGame();                  // Rozpoczęcie gry
        });

        return startPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(1024, 100));
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuPanel.add(pointsLabel);

        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuPanel.add(levelLabel);

        timeLabel = new JLabel("Time: 0s");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuPanel.add(timeLabel);

        // Dodanie przycisków do sterowania grą
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> gamePanel.pauseGame());
        menuPanel.add(pauseButton);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> gamePanel.resumeGame());
        menuPanel.add(resumeButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        menuPanel.add(exitButton);

        JButton restartButton = new JButton("Restart");
        restartButton.setPreferredSize(new Dimension(100, 25));
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.addActionListener(e -> gamePanel.restartGame()); // Akcja restartu
        menuPanel.add(restartButton);

        return menuPanel;
    }

    // Aktualizacja punktów i poziomu
    public void updateStats(int points, int level, long elapsedTime) {
        pointsLabel.setText("Points: " + points);
        levelLabel.setText("Level: " + level);
        timeLabel.setText("Time: " + elapsedTime + "s");
    }
}