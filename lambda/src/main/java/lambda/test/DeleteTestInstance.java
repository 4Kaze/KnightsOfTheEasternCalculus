package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.TestInstance;
import lambda.AuthenticatedRequest;
import lambda.test.model.TestRequest;

class DeleteTestInstance extends AuthenticatedHandler<TestRequest> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiter().isPresent())
            return requireRecruiter().get();

        TestInstance test = getMapper().load(TestInstance.class, getBody().getOwnerId(), getBody().getTestId());
        if(test == null)
            return responseOf(404, "Test not found");
        if(!getUserId().equals(test.getRecruiterId()))
            return responseOf(403, "The caller is not the owner of the test");

        getMapper().delete(test);
        return responseOf(204, null);
    }
}
