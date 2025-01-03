import javax.swing.*;
import java.awt.*;

public class QuestionManager extends Component {
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private int number1;
    private int number2;
    private Runnable onCorrectAnswer;
    GamePanel gamePanel;


    public QuestionManager(GamePanel gamePanel, Runnable onCorrectAnswer) {
        this.gamePanel = gamePanel;
        this.onCorrectAnswer = onCorrectAnswer;
        initializeComponents(gamePanel);
    }

    private void initializeComponents(Container parent) {
        questionLabel = new JLabel();
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
        submitButton.addActionListener(e -> handleAnswer());

        parent.add(questionLabel);
        parent.add(answerField);
        parent.add(submitButton);
    }

    public void activateQuestion(int number1, int number2) {
        this.number1 = number1;
        this.number2 = number2;

        if (gamePanel.level != 3) {
            questionLabel.setText("Solve this: " + number1 + " + " + number2);
        } else {
            questionLabel.setText("Solve this: " + number1 + " * " + number2);
        }

        questionLabel.setVisible(true);
        answerField.setText("");
        answerField.setVisible(true);
        submitButton.setVisible(true);
    }

    public void deactivateQuestion() {
        questionLabel.setVisible(false);
        answerField.setVisible(false);
        submitButton.setVisible(false);
    }

    public boolean isVisible() {
        return questionLabel.isVisible();
    }

    public JLabel getQuestionLabel() {
        return questionLabel;
    }

    public JTextField getAnswerField() {
        return answerField;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    private void handleAnswer() {
        int userAnswer = Integer.parseInt(answerField.getText());
        if (gamePanel.level != 3) {
            if (userAnswer == number1 + number2) {
                questionLabel.setText("Correct! Press Enter to continue.");
                questionLabel.setVisible(true);
                answerField.setVisible(false);
                submitButton.setVisible(false);
                gamePanel.points+=3;
                if(gamePanel.points%5==0){

                    if (gamePanel.points == 15 * gamePanel.level) {
                        gamePanel.level++;
                        if (gamePanel.level == 2)
                            gamePanel.GAME_SPEED = 120;
                        if (gamePanel.level == 3)
                            gamePanel.GAME_SPEED = 90;
                        if (gamePanel.level > 3) {
                            gamePanel.isGameActive = false;

                        }
                        JOptionPane.showMessageDialog(this, "Level Up! You are now on Level " + gamePanel.level + "!", "Level Up", JOptionPane.INFORMATION_MESSAGE);
                        gamePanel.gameTimer.setDelay(gamePanel.GAME_SPEED);
                    }
                }

                SwingUtilities.invokeLater(onCorrectAnswer);
            } else {
                questionLabel.setText("Incorrect. Game Over.");
                questionLabel.setVisible(true);
                gamePanel.endGame(); // Zakończenie gry
            }
        } else {
            if (userAnswer == number1 * number2) {
                questionLabel.setText("Correct! Press Enter to continue.");
                questionLabel.setVisible(true);
                answerField.setVisible(false);
                submitButton.setVisible(false);
                gamePanel.points+=3;

                SwingUtilities.invokeLater(onCorrectAnswer);
            } else {
                questionLabel.setText("Incorrect. Game Over.");
                questionLabel.setVisible(true);
                gamePanel.endGame(); // Zakończenie gry
            }
        }
    }
}