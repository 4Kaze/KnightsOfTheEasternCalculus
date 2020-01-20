package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.TestInstance;
import lambda.AuthenticatedRequest;
import lambda.test.model.TestRequest;

import java.util.ArrayList;

class GetTestInstance extends AuthenticatedHandler<TestRequest> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(!isRecruiter() && !getUserId().equals(getBody().getOwnerId()))
            return responseOf(403, "Insufficient permissions");

        TestInstance test = getMapper().load(TestInstance.class, getBody().getOwnerId(), getBody().getTestId());
        if (test == null)
            return responseOf(404, "Test not found");

        if(isRecruiter() && !getUserId().equals(test.getRecruiterId()))
            return responseOf(403, "Insufficient permissions");

        if (test.getStatus() == 0) {
            test.eraseAnswers();
        } else if (test.getStatus() == 1) {
            test.getOpenQuestions().forEach(question -> question.setReceivedScore(0));
            test.getValueQuestions().forEach(question -> question.setReceivedScore(0));
        }
        return responseOf(200, test);

    }
}
