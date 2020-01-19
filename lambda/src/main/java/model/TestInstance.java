package model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;
import java.util.List;

@DynamoDBTable(tableName = "TestInstances")
public class TestInstance implements Serializable {
    private String applicantId;
    private long timestamp;
    private String title;
    private List<SolvableClosedQuestion> closeQuestions;
    private List<SolvableOpenQuestion> openQuestions;
    private List<SolvableValueQuestion> valueQuestions;
    private float maxScore;
    private float receivedScore;
    private int status;
    private String recruiterId;
    private Long testId;

    public TestInstance() {
    }

    @DynamoDBHashKey(attributeName = "applicantId")
    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    @DynamoDBRangeKey(attributeName = "timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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


    public List<SolvableValueQuestion> getValueQuestions() {
        return valueQuestions;
    }

    public void setValueQuestions(List<SolvableValueQuestion> valueQuestions) {
        this.valueQuestions = valueQuestions;
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


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public void calculatePoints() {
        for (SolvableClosedQuestion c : this.closeQuestions) {
            this.receivedScore += c.getReceivedScore();
        }

        for (SolvableOpenQuestion c : this.openQuestions) {
            this.receivedScore += c.getReceivedScore();
        }

        for (SolvableValueQuestion c: this.valueQuestions){
            this.receivedScore += c.getReceivedScore();
        }
    }
}
