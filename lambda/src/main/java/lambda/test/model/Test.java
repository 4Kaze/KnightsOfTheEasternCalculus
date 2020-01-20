package lambda.test.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;
import java.util.List;

@DynamoDBTable(tableName = "Tests")
public class Test implements Serializable {
    private String recruiterId;
    private Long testId;
    private String title;
    private String searchTitle;
    private String language;
    private List<CloseQuestion> closeQuestions;
    private List<OpenQuestion> openQuestions;
    private List<ValueQuestion> valueQuestions;

    public Test() {
    }

    @DynamoDBHashKey(attributeName = "recruiterId")
    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    @DynamoDBRangeKey(attributeName = "timestamp")
    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    @DynamoDBAttribute(attributeName = "closeQuestions")
    public List<CloseQuestion> getCloseQuestions() {
        return closeQuestions;
    }

    public void setCloseQuestions(List<CloseQuestion> closeQuestions) {
        this.closeQuestions = closeQuestions;
    }

    @DynamoDBAttribute(attributeName = "openQuestions")
    public List<OpenQuestion> getOpenQuestions() {
        return openQuestions;
    }

    public void setOpenQuestions(List<OpenQuestion> openQuestions) {
        this.openQuestions = openQuestions;
    }

    @DynamoDBAttribute(attributeName = "valueQuestions")
    public List<ValueQuestion> getValueQuestions() {
        return valueQuestions;
    }

    public void setValueQuestions(List<ValueQuestion> valueQuestions) {
        this.valueQuestions = valueQuestions;
    }

    @DynamoDBAttribute(attributeName = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "searchTitle")
    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }
}
