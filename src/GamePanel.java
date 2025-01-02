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
    private int points = 0;
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
        startGame();
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

    private void startGame() {
        snake.reset();
        food.generate(WIDTH, HEIGHT);
        gameTimer = new Timer(GAME_SPEED, this);
        gameTimer.start();
        startTime = System.currentTimeMillis();
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

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Points: " + points, 10, 20);
        g.drawString("Level: " + level, 10, 40);
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        g.drawString("Time: " + elapsedSeconds + "s", 10, 60);
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
                    if (level==2)
                        GAME_SPEED = 100;
                    if (level==3)
                        GAME_SPEED = 30;
                    if (level > 3) {
                        isGameActive = false;
                    } else {
                        JOptionPane.showMessageDialog(this, "Level Up! You are now on Level " + level + "!", "Level Up", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                if (points % 3 == 0 && level == 1) {
                    questionManager.activateQuestion(generateRandomNumber1(), generateRandomNumber1());
                }
                if (points % 3 == 0 && level == 2) {
                    questionManager.activateQuestion(generateRandomNumber2(), generateRandomNumber2());
                }
                if (points % 3 == 0 && level == 3){
                    questionManager.activateQuestion(generateRandomNumber3(), generateRandomNumber3());
                }
                else {
                    food.generate(WIDTH, HEIGHT);
                }
            }

            if (snake.checkCollision(WIDTH, HEIGHT)) {
                isGameActive = false;
            }
        }
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
    public void endGame() {
        isGameActive = false;
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
