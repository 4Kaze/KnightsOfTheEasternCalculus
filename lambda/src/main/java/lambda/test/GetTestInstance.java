package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import lambda.request.TestRequest;
import model.TestInstance;
import lambda.Response;

import java.util.ArrayList;

public class GetTestInstance extends Handler<AuthenticatedRequest<TestRequest>> {
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
            if (test.getCloseQuestions() != null) {
                for (int i = 0; i < test.getCloseQuestions().size(); i++) {
                    test.getCloseQuestions().get(i).setCorrectAnswers(new ArrayList<>());
                }
            } else {
                test.setCloseQuestions(new ArrayList<>());
            }
            if (test.getOpenQuestions() != null) {
                for (int i = 0; i < test.getOpenQuestions().size(); i++) {
                    test.getOpenQuestions().get(i).setCorrectAnswer("");
                }
            } else {
                test.setOpenQuestions(new ArrayList<>());
            }
            if (test.getValueQuestions() != null) {
                for (int i = 0; i < test.getValueQuestions().size(); i++) {
                    test.getValueQuestions().get(i).setCorrectAnswer(0f);
                }
            } else {
                test.setValueQuestions(new ArrayList<>());
            }
        } else if (test.getStatus() == 1) {
            for (int i = 0; i < test.getOpenQuestions().size(); i++ ){
                test.getOpenQuestions().get(i).setReceivedScore(0);
            }
            for (int i = 0; i < test.getValueQuestions().size(); i++) {
                test.getValueQuestions().get(i).setReceivedScore(0);
            }
        }
        return new Response(200, test);

    }
}
