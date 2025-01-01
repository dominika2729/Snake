import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private JLabel pointsLabel;
    private JLabel levelLabel;
    private JButton menuButton;

    Frame() {
        // Ustawienia ramki
        this.setTitle("Matematyczny snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout()); // Użycie BorderLayout

        // Dodanie panelu gry
        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel, BorderLayout.CENTER);

        // Dodanie panelu menu
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

        // Przycisk menu
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.BOLD, 18));
        menuButton.addActionListener(e -> showMenu());
        menuPanel.add(menuButton);

        return menuPanel;
    }

    // Metoda do wyświetlenia menu po kliknięciu przycisku
    private void showMenu() {
        JOptionPane.showMessageDialog(this, "Returning to the main menu is not implemented yet!",
                "Menu", JOptionPane.INFORMATION_MESSAGE);
    }

    // Aktualizacja punktów i poziomu (do wywołania z GamePanel)
    public void updateStats(int points, int level) {
        pointsLabel.setText("Points: " + points);
        levelLabel.setText("Level: " + level);
    }
}