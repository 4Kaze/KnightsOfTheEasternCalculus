package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.Test;
import lambda.AuthenticatedRequest;

class UpdateTest extends AuthenticatedHandler<Test> {
    private final DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
            .build();

    @Override
    public Response handleRequest(AuthenticatedRequest<Test> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiter().isPresent())
            return requireRecruiter().get();
        if(!getUserId().equals(getBody().getRecruiterId()))
            return responseOf(403, "Insufficient permissions");

        Test input = getBody();
        input.setSearchTitle(input.getTitle().toLowerCase());
        getMapper().save(input, dynamoDBMapperConfig);
        return responseOf(200, input);
    }
}
