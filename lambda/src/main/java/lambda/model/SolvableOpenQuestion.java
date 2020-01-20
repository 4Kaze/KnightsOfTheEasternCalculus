package lambda.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;

@DynamoDBDocument
public class SolvableOpenQuestion implements Serializable {

    private String question;
    private int maxScore;
    private String answer;
    private String correctAnswer;
    private float receivedScore;

    public SolvableOpenQuestion() {
    }

    public SolvableOpenQuestion(OpenQuestion openQuestion) {
        this.question = openQuestion.getQuestion();
        this.maxScore = openQuestion.getMaxScore();
        this.correctAnswer = openQuestion.getCorrectAnswer();
        this.answer = "";
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public float getReceivedScore() {
        return receivedScore;
    }

    public void setReceivedScore(float receivedScore) {
        this.receivedScore = receivedScore;
    }
}
