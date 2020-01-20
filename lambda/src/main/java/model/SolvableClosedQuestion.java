package model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

@DynamoDBDocument
public class SolvableClosedQuestion implements Serializable {

    private String question;
    private ArrayList<String> answers;
    private ArrayList<Integer> chosenAnswers;
    private ArrayList<Integer> correctAnswers;
    private int answerScore;
    private float receivedScore;

    public SolvableClosedQuestion() {
    }

    public SolvableClosedQuestion(CloseQuestion closeQuestion) {
        this.question = closeQuestion.getQuestion();
        this.answers = new ArrayList<>();
        this.answers.addAll(closeQuestion.getCorrectAnswers());
        this.answers.addAll(closeQuestion.getIncorrectAnswers());
        Collections.shuffle(this.answers);
        this.correctAnswers = new ArrayList<>();
        this.chosenAnswers = new ArrayList<>();
        closeQuestion.getCorrectAnswers().forEach(answer -> correctAnswers.add(this.answers.indexOf(answer)));
        this.answerScore = closeQuestion.getAnswerScore();
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<Integer> getChosenAnswers() {
        return chosenAnswers;
    }

    public void setChosenAnswers(ArrayList<Integer> chosenAnswers) {
        this.chosenAnswers = chosenAnswers;
    }

    public int getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(int answerScore) {
        this.answerScore = answerScore;
    }

    public ArrayList<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(ArrayList<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public float getReceivedScore() {
        return receivedScore;
    }

    public void setReceivedScore(float receivedScore) {
        this.receivedScore = receivedScore;
    }
}
