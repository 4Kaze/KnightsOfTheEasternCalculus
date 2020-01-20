package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.model.SolvableOpenQuestion;
import lambda.model.SolvableValueQuestion;
import lambda.model.TestInstance;
import lambda.model.TestStatus;
import lambda.request.AuthenticatedRequest;

import java.util.ArrayList;
import java.util.List;

class GradeTest extends Handler<AuthenticatedRequest<TestInstance>> {

    @Override
    public Response handleRequest(AuthenticatedRequest<TestInstance> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");
        if(!authenticatedRequest.getUserId().equals(authenticatedRequest.getBody().getRecruiterId()))
            return new Response(403, "Insufficient permissions");

        TestInstance input = authenticatedRequest.getBody();
        TestInstance test = getMapper().load(TestInstance.class, input.getApplicantId(), input.getTimestamp());
        test.setReceivedScore(0);

        if (test.getOpenQuestions() != null) {
            List<SolvableOpenQuestion> open = new ArrayList<>();
            SolvableOpenQuestion o;
            for (int i = 0; i < test.getOpenQuestions().size(); i++) {
                o = test.getOpenQuestions().get(i);
                o.setAnswer(input.getOpenQuestions().get(i).getAnswer());
                o.setReceivedScore(input.getOpenQuestions().get(i).getReceivedScore());
                o.setCorrectAnswer(input.getOpenQuestions().get(i).getCorrectAnswer());
                open.add(o);
            }
            test.setOpenQuestions(open);

        } else {
            test.setOpenQuestions(new ArrayList<>());
        }

        if (test.getValueQuestions() != null) {
            List<SolvableValueQuestion> value = new ArrayList<>();
            SolvableValueQuestion v;
            for (int i = 0; i < test.getValueQuestions().size(); i++) {
                v = test.getValueQuestions().get(i);
                v.setAnswer(input.getValueQuestions().get(i).getAnswer());
                v.setReceivedScore(input.getValueQuestions().get(i).getReceivedScore());
                v.setCorrectAnswer(input.getValueQuestions().get(i).getCorrectAnswer());
                value.add(v);
            }
            test.setValueQuestions(value);
        } else {
            test.setValueQuestions(new ArrayList<>());
        }


        test.calculatePoints();

        test.setStatus(TestStatus.CHECKED.getValue());

        DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
                .build();

        getMapper().save(test, dynamoDBMapperConfig);
        return new Response(200, "Test successfully saved");

    }
}
