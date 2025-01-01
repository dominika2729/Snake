import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int TILE_SIZE = 50;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static int GAME_SPEED = 100;

    private final int[] snakeX = new int[(WIDTH * HEIGHT) / TILE_SIZE];
    private final int[] snakeY = new int[(WIDTH * HEIGHT) / TILE_SIZE];
    private int snakeLength = 2;

    private int foodX;
    private int foodY;
    private boolean isGameActive = false;
    private boolean isQuestionActive = false; // Flaga do aktywności pytania
    private Timer gameTimer;
    private Image appleImage;

    private Random random = new Random();
    private int number1 =random.nextInt(16);
    private int number2 =random.nextInt(16);

    private int dx = TILE_SIZE;
    private int dy = 0;

    private int points = 0;
    private int level = 1;
    private long startTime;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;


    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null); // Ustawienie layoutu na null do niestandardowych komponentów
        this.addKeyListener(new MyKeyAdapter());

        loadAppleImage();
        initializeQuestionComponents();
        initializeGame();
    }

    private void loadAppleImage() {
        try {
            appleImage = new ImageIcon(getClass().getResource("/apple.png")).getImage();
        } catch (Exception e) {
            System.err.println("Nie udało się załadować obrazka jabłka.");
            e.printStackTrace();
        }
    }

    private void initializeQuestionComponents() {
        questionLabel = new JLabel("Question will appear here.");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBounds(100, 100, 800, 30);
        questionLabel.setVisible(false);

        answerField = new JTextField();
        answerField.setBounds(100, 150, 200, 30);
        answerField.setVisible(false);

        submitButton = new JButton("Submit");
        submitButton.setBounds(320, 150, 100, 30);
        submitButton.setVisible(false);
        submitButton.addActionListener(e -> handleAnswer(number1, number2));



        this.setLayout(null);
        this.add(questionLabel);
        this.add(answerField);
        this.add(submitButton);
    }

    private void initializeGame() {
        snakeLength = 2;

        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = TILE_SIZE * (5 - i);
            snakeY[i] = TILE_SIZE * 5;
        }

        generateFood();
        isGameActive = true;
        gameTimer = new Timer(GAME_SPEED, this);
        gameTimer.start();
        startTime = System.currentTimeMillis();
    }

    private void generateFood() {
        foodX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        foodY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void drawGame(Graphics g) {
        if (appleImage != null) {
            g.drawImage(appleImage, foodX, foodY, TILE_SIZE, TILE_SIZE, this);
        } else {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);
        }

        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(34, 139, 34));
            }
            g.fillRect(snakeX[i], snakeY[i], TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Points: " + points, 10, 20);
        g.drawString("Level: " + level, 10, 40);
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        g.drawString("Time: " + elapsedSeconds + "s", 10, 60);
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

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());

        if (level > 3) {
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
        if (isGameActive && !isQuestionActive) {
            moveSnake();
            checkFoodCollision();
            checkCollision();
        }
        repaint();
    }

    private void moveSnake() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        snakeX[0] += dx;
        snakeY[0] += dy;
    }

    private void checkFoodCollision() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            points++;

            if (points % 3 == 0) {
                number1 =random.nextInt(16);
                number2 =random.nextInt(16);
                activateQuestion(number1, number2);
            }

            if (points == 9*level) {
                level++;
                if (level > 3) {
                    isGameActive = false;
                } else {
                    JOptionPane.showMessageDialog(this, "Level Up! You are now on Level " + level + "!", "Level Up", JOptionPane.INFORMATION_MESSAGE);
                }
            }


            generateFood();
        }
    }

    private void activateQuestion(int number1, int number2) {
        isQuestionActive = true;

        questionLabel.setText("Solve this: "+ number1 +" + "+ number2);
        questionLabel.setVisible(true);

        answerField.setText(""); // Reset pola tekstowego
        answerField.setVisible(true);

        submitButton.setVisible(true);

        gameTimer.stop(); // Wstrzymanie gry na czas pytania
    }
    private void displayCorrectAnswerMessage() {
        questionLabel.setText("Correct! Press Enter to continue.");
        answerField.setVisible(false);
        submitButton.setVisible(false);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    questionLabel.setVisible(false);
                    gameTimer.start(); // Wznowienie gry
                    GamePanel.this.removeKeyListener(this); // Usuń nasłuchiwanie Enter
                }
            }

        });
        gameTimer.stop(); // Wstrzymanie gry
    }
    private void handleAnswer(int number1, int number2) {
        int correctAnswer = number1 + number2; // Ustaw poprawną odpowiedź
        String answer = answerField.getText();
        try {
            int userAnswer = Integer.parseInt(answer); // Parsuj odpowiedź użytkownika jako liczbę
            if (userAnswer == correctAnswer) { // Jeśli odpowiedź jest poprawna
                isQuestionActive = false;
                displayCorrectAnswerMessage(); // Pokaż informację o poprawnej odpowiedzi
            } else {
                questionLabel.setText("Incorrect. Try again.Solve: " +number1 + " + "  +number2); // Zmień tekst pytania

            }
        } catch (NumberFormatException e) {
            questionLabel.setText("Please enter a valid number."); // Komunikat o błędnym wejściu
        }
    }

    private void checkCollision() {
        if (snakeX[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] < 0 || snakeY[0] >= HEIGHT) {
            isGameActive = false;
        }

        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                isGameActive = false;
            }
        }
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