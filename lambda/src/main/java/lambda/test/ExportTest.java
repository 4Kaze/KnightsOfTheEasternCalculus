package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import model.Test;
import lambda.Response;

public class ExportTest extends Handler<AuthenticatedRequest<String>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");
        if(!authenticatedRequest.getUserId().equals(authenticatedRequest.getBody())) //TODO: zmienić na id rekrutera z requestu
            return new Response(403, "Insufficient permissions");

        Test test = getMapper().load(Test.class, authenticatedRequest.getBody());
        if (test == null)
            return new Response(404, "Test was not found");
        else {
            String csv = "";
            int index = 1;


            return new Response(200, csv);
        }
    }

    private void addCloseQuestions(Test test, String csv, int index) {
        for (int i = 0; i < test.getCloseQuestions().size(); i++) {
            csv += index + ";";
            csv += "W" + ";";
            csv += test.getLanguage() + ";";
            csv += test.getCloseQuestions().get(i).getQuestion() + ";";
            csv += test.getCloseQuestions().get(i).getCorrectAnswers() + ";";
            //TODO
        }
    }

    private void addOpenQuestions(Test test, String csv, int index) {
        for (int i = 0; i < test.getOpenQuestions().size(); i++) {
            csv += index + ";";
            csv += "O" + ";";
            csv += test.getLanguage() + ";";
            csv += test.getOpenQuestions().get(i).getQuestion() + ";";
            csv += test.getOpenQuestions().get(i).getCorrectAnswer() + ";";
            csv += test.getOpenQuestions().get(i).getMaxScore() + ";";
            //TODO
        }
    }

    private void addValueQuestions(Test test, String csv, int index) {
        //TODO
    }
}
