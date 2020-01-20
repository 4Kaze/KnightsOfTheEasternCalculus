package lambda.test.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;

@DynamoDBDocument
public class ValueQuestion implements Serializable {
    private String question;
    private float correctAnswer;
    private int maxScore;

    public ValueQuestion() {
    }

    public ValueQuestion(ValueQuestion valueQuestion) {
        this.question = valueQuestion.question;
        this.correctAnswer = valueQuestion.correctAnswer;
        this.maxScore = valueQuestion.maxScore;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public float getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(float correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
