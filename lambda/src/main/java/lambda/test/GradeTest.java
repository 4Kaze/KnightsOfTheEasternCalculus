package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.SolvableOpenQuestion;
import lambda.test.model.SolvableValueQuestion;
import lambda.test.model.TestInstance;
import lambda.test.model.TestStatus;
import lambda.AuthenticatedRequest;

import java.util.ArrayList;
import java.util.List;

class GradeTest extends AuthenticatedHandler<TestInstance> {

    @Override
    public Response handleRequest(AuthenticatedRequest<TestInstance> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiter().isPresent())
            return requireRecruiter().get();
        if(!getUserId().equals(getBody().getRecruiterId()))
            return responseOf(403, "Insufficient permissions");

        TestInstance input = getBody();
        TestInstance test = getMapper().load(TestInstance.class, input.getApplicantId(), input.getTimestamp());
        test.setReceivedScore(0);
        test.gradeTest(input);
        test.calculatePoints();
        test.setStatus(TestStatus.CHECKED.getValue());

        DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
                .build();

        getMapper().save(test, dynamoDBMapperConfig);
        return responseOf(200, "Test successfully saved");

    }
}
