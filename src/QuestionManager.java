import javax.swing.*;
import java.awt.*;

public class QuestionManager {
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private int number1;
    private int number2;
    private Runnable onCorrectAnswer;

    public QuestionManager(Container parent, Runnable onCorrectAnswer) {
        this.onCorrectAnswer = onCorrectAnswer;
        initializeComponents(parent);
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

        questionLabel.setText("Solve this: " + number1 + " + " + number2);
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
        try {
            int userAnswer = Integer.parseInt(answerField.getText());
            if (userAnswer == number1 + number2) {
                questionLabel.setText("Correct! Press Enter to continue.");
                questionLabel.setVisible(true);
                answerField.setVisible(false);
                submitButton.setVisible(false);
                onCorrectAnswer.run();
            } else {
                questionLabel.setText("Incorrect. Try again: " + number1 + " + " + number2);
            }
        } catch (NumberFormatException e) {
            questionLabel.setText("Please enter a valid number.");
        }
    }
}