package model.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SolvableTest {


    private String applicantID;
    private Timestamp timestamp;
    private String title;
    private List<SolvableClosedQuestion> closeQuestions;
    private List<SolvableOpenQuestion> openQuestions;
    private float maxScore;
    private float receivedScore;
    private TestStatus status;

    public SolvableTest(String applicantID, Timestamp timestamp, String title, List<SolvableClosedQuestion> closeQuestions, List<SolvableOpenQuestion> openQuestions, float maxScore, float receivedScore, TestStatus status) {
        this.applicantID = applicantID;
        this.timestamp = timestamp;
        this.title = title;
        this.closeQuestions = closeQuestions;
        this.openQuestions = openQuestions;
        this.maxScore = maxScore;
        this.receivedScore = receivedScore;
        this.status = status;
    }

    public SolvableTest(TestInstance test) {
        this.applicantID = test.getApplicantID();
        this.timestamp = test.getTimestamp();
        this.title = test.getTitle();
        this.maxScore = test.getMaxScore();
        this.receivedScore = test.getReceivedScore();
        this.status = test.getStatus();
        this.closeQuestions = new ArrayList<>();
        this.openQuestions = new ArrayList<>();

        for (CloseQuestion c : test.getCloseQuestions()) {
            this.closeQuestions.add(new SolvableClosedQuestion(c));
        }
        for (OpenQuestion o : test.getOpenQuestions()) {
            this.openQuestions.add(new SolvableOpenQuestion(o));
        }
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SolvableClosedQuestion> getCloseQuestions() {
        return closeQuestions;
    }

    public void setCloseQuestions(List<SolvableClosedQuestion> closeQuestions) {
        this.closeQuestions = closeQuestions;
    }

    public List<SolvableOpenQuestion> getOpenQuestions() {
        return openQuestions;
    }

    public void setOpenQuestions(List<SolvableOpenQuestion> openQuestions) {
        this.openQuestions = openQuestions;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public float getReceivedScore() {
        return receivedScore;
    }

    public void setReceivedScore(float receivedScore) {
        this.receivedScore = receivedScore;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }
}
