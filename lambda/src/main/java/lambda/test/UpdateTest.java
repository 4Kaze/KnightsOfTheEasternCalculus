package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import model.Test;
import lambda.Response;

public class UpdateTest extends Handler<AuthenticatedRequest<Test>> {
    private DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
            .build();

    @Override
    public Response handleRequest(AuthenticatedRequest<Test> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");
        if(!authenticatedRequest.getUserId().equals(authenticatedRequest.getBody().getRecruiterId()))
            return new Response(403, "Insufficient permissions");

        Test input = authenticatedRequest.getBody();
        input.setSearchTitle(input.getTitle().toLowerCase());
        getMapper().save(input, dynamoDBMapperConfig);
        return new Response(200, input);
    }
}
