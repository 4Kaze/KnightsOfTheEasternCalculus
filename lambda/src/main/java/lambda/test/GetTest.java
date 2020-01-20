package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Response;
import lambda.test.model.Test;
import lambda.AuthenticatedRequest;
import lambda.test.model.TestRequest;

class GetTest extends AuthenticatedHandler<TestRequest> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiterWithValidRequest().isPresent())
            return requireRecruiterWithValidRequest().get();

        Test test = getMapper().load(Test.class, getBody().getOwnerId(), getBody().getTestId());
        if(test == null)
            return responseOf(404, "Test was not found");

        else
            return responseOf(200, test);
    }
}
