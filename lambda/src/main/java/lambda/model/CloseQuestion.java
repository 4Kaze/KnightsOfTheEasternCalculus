package lambda.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;
import java.util.ArrayList;

@DynamoDBDocument
public class CloseQuestion implements Serializable {
    private String question;
    private ArrayList<String> correctAnswers;
    private ArrayList<String> incorrectAnswers;
    private int answerScore;

    public CloseQuestion() {
    }

    public CloseQuestion(CloseQuestion closeQuestion) {
        this.question = closeQuestion.question;
        this.correctAnswers = closeQuestion.correctAnswers;
        this.incorrectAnswers = closeQuestion.incorrectAnswers;
        this.answerScore = closeQuestion.answerScore;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(ArrayList<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(ArrayList<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(int answerScore) {
        this.answerScore = answerScore;
    }
}
