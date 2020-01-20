package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.*;
import lambda.AuthenticatedRequest;

import java.util.ArrayList;
import java.util.List;

class SolveTest extends AuthenticatedHandler<TestInstance> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestInstance> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(!getUserId().equals(getBody().getApplicantId()))
            return responseOf(403, "Insufficient permissions");

        TestInstance input = getBody();
        TestInstance test = getMapper().load(TestInstance.class, input.getApplicantId(), input.getTimestamp());

        if (test.getApplicantId() == null) {
            return responseOf(400, "ApplicantID can't be null");
        }

        test.setReceivedScore(0);
        test.fillAnswers(input);
        test.calculatePoints();
        test.setStatus(TestStatus.SOLVED.getValue());

        DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
                .build();

        getMapper().save(test, dynamoDBMapperConfig);
        return responseOf(200, test);

    }


}
