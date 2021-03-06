package lambda.applicant;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;
import lambda.test.model.*;
import lambda.AuthenticatedRequest;

import java.util.*;
import java.util.stream.Collectors;

public class AssignApplicant extends AuthenticatedHandler<AssignApplicant.AssignRequest> {
    @Override
    public Response handleRequest(AuthenticatedRequest<AssignRequest> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiter().isPresent())
            return requireRecruiter().get();
        if(!getUserId().equals(getBody().getRecruiterId()))
            return responseOf(403, "The assigned test doesn't belong to the caller");

        AssignRequest input = getBody();

        DynamoDBQueryExpression<TestInstance> queryExpression;

        if(!input.isForce()) {
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":val", new AttributeValue().withN(Long.toString(input.getTestId())));
            eav.put(":val2", new AttributeValue().withS(input.getRecruiterId()));
            eav.put(":id", new AttributeValue().withS(input.getApplicantId()));

            Map<String, String> attributeNames = new HashMap<>();
            attributeNames.put("#t", "timestamp");

            queryExpression = new DynamoDBQueryExpression<TestInstance>()
                    .withKeyConditionExpression("applicantId = :id")
                    .withFilterExpression("testId = :val AND recruiterId = :val2")
                    .withExpressionAttributeValues(eav)
                    .withProjectionExpression("#t")
                    .withExpressionAttributeNames(attributeNames);

            List<TestInstance> queryList = new ArrayList<>(getMapper().query(TestInstance.class, queryExpression));

            if (!queryList.isEmpty()) {
                return responseOf(409, queryList.stream()
                        .map(TestInstance::getTimestamp).collect(Collectors.toList()));
            }
        }

        Test test = getMapper().load(Test.class, input.getRecruiterId(), input.getTestId());
        if(test == null) {
            return responseOf(404, "Test was not found");
        }

        Applicant applicant = getMapper().load(Applicant.class, input.getApplicantId());
        if(applicant == null) {
            return responseOf(404, "Applicant was not found");
        }

        TestInstance testInstance = new TestInstance();

        testInstance.setApplicantId(input.getApplicantId());
        testInstance.setTimestamp(new Date().getTime());
        testInstance.setTitle(test.getTitle());
        testInstance.setCloseQuestions(test.getCloseQuestions().stream()
                .map(SolvableClosedQuestion::new).collect(Collectors.toList()));
        testInstance.setOpenQuestions(test.getOpenQuestions().stream()
                .map(SolvableOpenQuestion::new).collect(Collectors.toList()));
        testInstance.setValueQuestions(test.getValueQuestions().stream()
                .map(SolvableValueQuestion::new).collect(Collectors.toList()));
        testInstance.setMaxScore(testInstance.getCloseQuestions().stream()
                .reduce(0F, (sum, question) -> sum + question.getAnswerScore()*question.getCorrectAnswers().size(), Float::sum));
        testInstance.setMaxScore(testInstance.getOpenQuestions().stream()
                .reduce(testInstance.getMaxScore(), (sum, question) -> sum + question.getMaxScore(), Float::sum));
        testInstance.setStatus(TestStatus.NOTSOLVED.getValue());
        testInstance.setRecruiterId(input.getRecruiterId());
        testInstance.setTestId(input.getTestId());

        getMapper().save(testInstance);

        return responseOf(200, testInstance);
    }

    static class AssignRequest {
        private String applicantId;
        private Long testId;
        private String recruiterId;
        private boolean force = false;

        String getApplicantId() {
            return applicantId;
        }

        public void setApplicantId(String applicantId) {
            this.applicantId = applicantId;
        }

        Long getTestId() {
            return testId;
        }

        public void setTestId(Long testId) {
            this.testId = testId;
        }

        boolean isForce() {
            return force;
        }

        public void setForce(boolean force) {
            this.force = force;
        }

        String getRecruiterId() {
            return recruiterId;
        }

        public void setRecruiterId(String recruiterId) {
            this.recruiterId = recruiterId;
        }
    }
}
