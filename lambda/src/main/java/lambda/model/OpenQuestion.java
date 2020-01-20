package lambda.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;

@DynamoDBDocument
public class OpenQuestion implements Serializable {
    private String question;
    private String correctAnswer;
    private int maxScore;

    public OpenQuestion() {
    }

    public OpenQuestion(OpenQuestion openQuestion) {
        this.question = openQuestion.question;
        this.correctAnswer = openQuestion.correctAnswer;
        this.maxScore = openQuestion.maxScore;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
