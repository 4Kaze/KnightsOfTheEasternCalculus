package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import lambda.request.TestRequest;
import model.TestInstance;
import lambda.Response;

public class DeleteTestInstance extends Handler<AuthenticatedRequest<TestRequest>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");

        TestInstance test = getMapper().load(TestInstance.class, authenticatedRequest.getBody().getOwnerId(), authenticatedRequest.getBody().getTestId());
        if(test == null)
            return new Response(404, "Test not found");
        if(!authenticatedRequest.getUserId().equals(test.getRecruiterId()))
            return new Response(403, "The caller is not the owner of the test");

        getMapper().delete(test);
        return new Response(204, null);
    }
}
