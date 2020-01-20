package lambda.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;

@DynamoDBDocument
public class SolvableValueQuestion implements Serializable {

    private String question;
    private Float correctAnswer;
    private Float answer;
    private int maxScore;
    private int receivedScore;

    public SolvableValueQuestion() {
    }

    public SolvableValueQuestion(ValueQuestion valueQuestion) {
        this.question = valueQuestion.getQuestion();
        this.maxScore = valueQuestion.getMaxScore();
        this.correctAnswer = valueQuestion.getCorrectAnswer();
        this.answer = null;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Float getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Float correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Float getAnswer() {
        return answer;
    }

    public void setAnswer(Float answer) {
        this.answer = answer;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getReceivedScore() {
        return receivedScore;
    }

    public void setReceivedScore(int receivedScore) {
        this.receivedScore = receivedScore;
    }
}
