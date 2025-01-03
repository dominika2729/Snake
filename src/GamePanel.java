import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {

    private static final int TILE_SIZE = 50;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static int GAME_SPEED = 180;

    private Snake snake;
    private Food food;
    private QuestionManager questionManager;
    private Timer gameTimer;

    private int dx = TILE_SIZE;
    private int dy = 0;
    int points = 0;
    public int level = 1;
    private boolean isGameActive = true;

    private long startTime;
    private Image appleImage;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new MyKeyAdapter());

        loadAppleImage();
        initializeComponents();
        createMenu(); // Tworzenie menu
    }


    public void restartGame() {
        points = 0;
        level = 1;
        dx = TILE_SIZE;
        dy = 0;
        isGameActive = true;
        GAME_SPEED = 180; // Przywróć domyślną prędkość gry
        snake.reset();
        food.generate(WIDTH, HEIGHT);
        startTime = System.currentTimeMillis();
        gameTimer.restart();
        questionManager.deactivateQuestion(); // Ukryj pytanie
        reattachKeyListener(); // Ponowne przypisanie KeyListener
        repaint(); // Odśwież panel
    }

    public void pauseGame() {
        if (isGameActive) {
            gameTimer.stop(); // Wstrzymanie timera
            JOptionPane.showMessageDialog(this, "Game Paused. Select 'Resume' to continue.", "Paused", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void resumeGame() {
        if (isGameActive && gameTimer != null) { // Sprawdzenie, czy gra jest aktywna i timer istnieje
            reattachKeyListener();              // Ponowne przypisanie KeyListener
            gameTimer.start();                  // Wznowienie timera
            this.requestFocusInWindow();        // Ustawienie fokusu na panel gry
        }
    }
    private void reattachKeyListener() {
        this.removeKeyListener(getKeyListeners()[0]); // Usuń starego nasłuchiwacza
        this.addKeyListener(new MyKeyAdapter());      // Dodaj nowego nasłuchiwacza
        this.requestFocusInWindow();                 // Wymuś fokus na panel gry
    }

    public void endGame() {
        gameTimer.stop();
        int choice = JOptionPane.showConfirmDialog(this, "Game Over! Do you want to exit?", "Exit Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0); // Zakończenie programu
        }
    }
    public int getPoints() {
        return points;
    }

    public int getLevel() {
        return level;
    }
    private void updateStats() {
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof Frame) {
            ((Frame) frame).updateStats(points, level, elapsedSeconds);
        }
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Gra
        JMenu gameMenu = new JMenu("Game");

        // Opcja Wstrzymaj
        JMenuItem pauseItem = new JMenuItem("Pause");
        pauseItem.addActionListener(e -> pauseGame());
        gameMenu.add(pauseItem);

        // Opcja Wznów
        JMenuItem resumeItem = new JMenuItem("Resume");
        resumeItem.addActionListener(e -> resumeGame());
        gameMenu.add(resumeItem);

        // Opcja Zakończ
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> endGame());
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);

        // Dodanie menu na górę okna
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setJMenuBar(menuBar);
        }
    }
    private void loadAppleImage() {
        try {
            appleImage = new ImageIcon(getClass().getResource("/apple.png")).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load apple image.");
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        snake = new Snake((WIDTH * HEIGHT) / TILE_SIZE);
        food = new Food();
        questionManager = new QuestionManager(this, this::displayCorrectAnswerMessage);
    }

    public void startGame() {
        snake.reset();
        food.generate(WIDTH, HEIGHT);
        if (gameTimer == null) { // Inicjalizacja timera tylko raz
            gameTimer = new Timer(GAME_SPEED, this);
        }
        gameTimer.start(); // Start timera
        startTime = System.currentTimeMillis();
    }
    private void startPanel(JFrame frame) {
        JMenuBar startBar = new JMenuBar(); // Tworzymy pasek menu
        JMenu startMenu = new JMenu("Game"); // Tworzymy menu o nazwie "Game"
        JMenuItem startItem = new JMenuItem("START"); // Tworzymy element menu "START"

        // Dodajemy akcję do przycisku START
        startItem.addActionListener(e -> startGame());
        startMenu.add(startItem); // Dodajemy element START do menu

        startBar.add(startMenu); // Dodajemy menu do paska menu

        // Dodajemy pasek menu do JFrame
        frame.setJMenuBar(startBar);
    }

    private void displayCorrectAnswerMessage() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    questionManager.deactivateQuestion();
                    gameTimer.start(); // Wznowienie gry
                    GamePanel.this.removeKeyListener(this); // Usuń nasłuchiwanie Enter
                }
            }
        });
        gameTimer.stop(); // Wstrzymanie gry
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isGameActive) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    private void drawGame(Graphics g) {
        food.draw(g, appleImage);

        for (int i = 0; i < snake.getLength(); i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(34, 139, 34));
            }
            g.fillRect(snake.getX(i), snake.getY(i), TILE_SIZE, TILE_SIZE);
        }
    }

    void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());

        if (level > 3) {
            gameTimer.stop();
            g.drawString("You Won!", (WIDTH - metrics.stringWidth("You Won!")) / 2, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            String result = "Final Score: " + points + ", Time: " + ((System.currentTimeMillis() - startTime) / 1000) + "s";
            g.drawString(result, (WIDTH - metrics.stringWidth(result)) / 2, HEIGHT / 2);
        } else {
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameActive && !questionManager.isVisible()) {
            snake.move(dx, dy);
            if (snake.getX(0) == food.getX() && snake.getY(0) == food.getY()) {
                snake.grow();
                points++;

                if (points == 9 * level) {
                    level++;
                    if (level == 2)
                        GAME_SPEED = 100;
                    if (level == 3)
                        GAME_SPEED = 30;
                    if (level > 3) {
                        isGameActive = false;
                    } else {
                        JOptionPane.showMessageDialog(this, "Level Up! You are now on Level " + level + "!", "Level Up", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                if (points % 3 == 0 && level == 1) {
                    questionManager.activateQuestion(generateRandomNumber1(), generateRandomNumber1());
                } else if (points % 3 == 0 && level == 2) {
                    questionManager.activateQuestion(generateRandomNumber2(), generateRandomNumber2());
                } else if (points % 3 == 0 && level == 3) {
                    questionManager.activateQuestion(generateRandomNumber3(), generateRandomNumber3());
                } else {
                    food.generate(WIDTH, HEIGHT);
                }
            }

            if (snake.checkCollision(WIDTH, HEIGHT)) {
                isGameActive = false;
            }
        }
        updateStats(); // Aktualizacja statystyk w Frame
        repaint();
    }


    private int generateRandomNumber1() {
        return (int) (Math.random() * 16);
    }
    private int generateRandomNumber2() {
        return (int) (Math.random() * 16)+15;
    }
    private int generateRandomNumber3() {
        return (int) (Math.random() * 10);
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_A && dx == 0) {
                dx = -TILE_SIZE;
                dy = 0;
            } else if (key == KeyEvent.VK_D && dx == 0) {
                dx = TILE_SIZE;
                dy = 0;
            } else if (key == KeyEvent.VK_W && dy == 0) {
                dy = -TILE_SIZE;
                dx = 0;
            } else if (key == KeyEvent.VK_S && dy == 0) {
                dy = TILE_SIZE;
                dx = 0;
            }
        }
    }
}
