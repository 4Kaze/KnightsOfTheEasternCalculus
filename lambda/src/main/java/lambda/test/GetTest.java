package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import lambda.request.TestRequest;
import model.Test;
import lambda.Response;

public class GetTest extends Handler<AuthenticatedRequest<TestRequest>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");
        if(!authenticatedRequest.getUserId().equals(authenticatedRequest.getBody().getOwnerId()))
            return new Response(403, "Insufficient permissions");

        Test test = getMapper().load(Test.class, authenticatedRequest.getBody().getOwnerId(), authenticatedRequest.getBody().getTestId());
        if(test == null)
            return new Response(404, "Test was not found");

        else
            return new Response(200, test);
    }
}
