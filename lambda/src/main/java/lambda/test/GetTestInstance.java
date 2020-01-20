package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.TestInstance;
import lambda.AuthenticatedRequest;
import lambda.test.model.TestRequest;

import java.util.ArrayList;

class GetTestInstance extends Handler<AuthenticatedRequest<TestRequest>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestRequest> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter() && !authenticatedRequest.getUserId().equals(authenticatedRequest.getBody().getOwnerId()))
            return new Response(403, "Insufficient permissions");

        TestInstance test = getMapper().load(TestInstance.class, authenticatedRequest.getBody().getOwnerId(), authenticatedRequest.getBody().getTestId());
        if (test == null)
            return new Response(404, "Test not found");

        if(authenticatedRequest.isRecruiter() && !authenticatedRequest.getUserId().equals(test.getRecruiterId()))
            return new Response(403, "Insufficient permissions");

        if (test.getStatus() == 0) {
            test.eraseAnswers();
        } else if (test.getStatus() == 1) {
            test.getOpenQuestions().forEach(question -> question.setReceivedScore(0));
            test.getValueQuestions().forEach(question -> question.setReceivedScore(0));
        }
        return new Response(200, test);

    }
}
