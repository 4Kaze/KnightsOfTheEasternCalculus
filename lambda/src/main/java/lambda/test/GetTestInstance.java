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
            if(test.getCloseQuestions() == null)
                test.setCloseQuestions(new ArrayList<>());
            else
                test.getCloseQuestions().forEach(question -> question.setCorrectAnswers(new ArrayList<>()));

            if(test.getOpenQuestions() == null)
                test.setOpenQuestions(new ArrayList<>());
            else
                test.getOpenQuestions().forEach(question -> question.setCorrectAnswer(""));

            if(test.getValueQuestions() == null)
                test.setValueQuestions(new ArrayList<>());
            else
                test.getValueQuestions().forEach(question -> question.setCorrectAnswer(null));

        } else if (test.getStatus() == 1) {
            test.getOpenQuestions().forEach(question -> question.setReceivedScore(0));
            test.getValueQuestions().forEach(question -> question.setReceivedScore(0));
        }
        return new Response(200, test);

    }
}
