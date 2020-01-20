package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Response;
import lambda.test.model.Test;
import lambda.AuthenticatedRequest;
import lambda.test.model.TestRequest;

class DeleteTest extends AuthenticatedHandler<TestRequest> {

    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiterWithValidRequest().isPresent())
            return requireRecruiterWithValidRequest().get();

        Test test = new Test();
        test.setRecruiterId(getBody().getOwnerId());
        test.setTestId(getBody().getTestId());
        getMapper().delete(test);
        return responseOf(204, "");
    }
}
