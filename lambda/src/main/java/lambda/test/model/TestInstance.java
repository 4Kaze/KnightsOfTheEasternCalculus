package lambda.test.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;
import java.util.ArrayList;
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
        this.closeQuestions.forEach(question -> this.receivedScore += question.getReceivedScore());
        this.openQuestions.forEach(question -> this.receivedScore += question.getReceivedScore());
        this.valueQuestions.forEach(question -> this.receivedScore += question.getReceivedScore());
    }

    public void fillAnswers(TestInstance solvedTest) {
        if (this.getCloseQuestions() != null) {
            List<SolvableClosedQuestion> close = new ArrayList<>();
            for (int i = 0; i < this.getCloseQuestions().size(); i++) {
                SolvableClosedQuestion c = this.getCloseQuestions().get(i);
                c.setChosenAnswers(solvedTest.getCloseQuestions().get(i).getChosenAnswers());
                close.add(c);
            }
            this.setCloseQuestions(close);
            calculateClosed(this.getCloseQuestions());
        }

        if (this.getOpenQuestions() != null) {
            List<SolvableOpenQuestion> open = new ArrayList<>();
            for (int i = 0; i < this.getOpenQuestions().size(); i++) {
                SolvableOpenQuestion o = this.getOpenQuestions().get(i);
                o.setAnswer(solvedTest.getOpenQuestions().get(i).getAnswer());
                open.add(o);
            }
            this.setOpenQuestions(open);
        }

        if (this.getValueQuestions() != null) {
            List<SolvableValueQuestion> value = new ArrayList<>();
            for (int i = 0; i < this.getValueQuestions().size(); i++) {
                SolvableValueQuestion v = this.getValueQuestions().get(i);
                v.setAnswer(solvedTest.getValueQuestions().get(i).getAnswer());
                value.add(v);
            }
            this.setValueQuestions(value);
            calculateValue(this.getValueQuestions());
        }
    }
    
    public void gradeTest(TestInstance checkedTest) {
        if (this.getOpenQuestions() != null) {
            List<SolvableOpenQuestion> open = new ArrayList<>();
            for (int i = 0; i < this.getOpenQuestions().size(); i++) {
                SolvableOpenQuestion o = this.getOpenQuestions().get(i);
                o.setReceivedScore(checkedTest.getOpenQuestions().get(i).getReceivedScore());
                open.add(o);
            }
            this.setOpenQuestions(open);

        }

        if (this.getValueQuestions() != null) {
            List<SolvableValueQuestion> value = new ArrayList<>();
            for (int i = 0; i < this.getValueQuestions().size(); i++) {
                SolvableValueQuestion v = this.getValueQuestions().get(i);
                v.setReceivedScore(checkedTest.getValueQuestions().get(i).getReceivedScore());
                value.add(v);
            }
            this.setValueQuestions(value);
        }
    }

    private void calculateClosed(List<SolvableClosedQuestion> closed) {
        closed.forEach(question -> {
            question.setReceivedScore(question.getChosenAnswers().stream()
                    .reduce(0, (sum, answer) -> sum + question.getAnswerScore() *
                            (question.getCorrectAnswers().contains(answer) ? 1 : -1)));
            if (question.getReceivedScore() < 0) question.setReceivedScore(0);
        });
    }

    private void calculateValue(List<SolvableValueQuestion> value) {
        value.forEach( question -> question.setReceivedScore((Math.abs(question.getAnswer() - question.getCorrectAnswer()) < 0.01
                ? question.getMaxScore() : 0)));
    }

    public void eraseAnswers() {
        if(closeQuestions == null) closeQuestions = new ArrayList<>();
        else closeQuestions.forEach(question -> question.setCorrectAnswers(new ArrayList<>()));
        if(openQuestions == null) openQuestions = new ArrayList<>();
        else openQuestions.forEach(question -> question.setCorrectAnswer(null));
        if(valueQuestions == null) valueQuestions = new ArrayList<>();
        else valueQuestions.forEach(question -> question.setCorrectAnswer(null));
    }
}
