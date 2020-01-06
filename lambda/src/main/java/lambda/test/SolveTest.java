package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import model.test.SolvableClosedQuestion;
import model.test.SolvableOpenQuestion;
import model.test.TestInstance;
import util.Response;

import java.util.ArrayList;
import java.util.List;

public class SolveTest extends Handler<TestInstance> {
    @Override
    public Response handleRequest(TestInstance input, Context context) {
        if (input != null) {
            String appID = input.getApplicantID();
            long time = input.getTimestamp();
            List<TestInstance> tests = getMapper().scan(TestInstance.class, new DynamoDBScanExpression());
            TestInstance test = new TestInstance();
            for (TestInstance t : tests) {
                if (t.getApplicantID().equals(appID) && t.getTimestamp() == time) {
                    test = t;
                    break;
                }
            }
            if (test.getApplicantID() == null) {
                return new Response(500, "ApplicantID can't be null");
            }


            List<SolvableClosedQuestion> close = new ArrayList<>();
            SolvableClosedQuestion c = null;
            for (int i = 0; i < test.getCloseQuestions().size(); i++) {
                c = test.getCloseQuestions().get(i);
                c.setChosenAnswers(input.getCloseQuestions().get(i).getChosenAnswers());
                close.add(c);
            }
            test.setCloseQuestions(close);

            List<SolvableOpenQuestion> open = new ArrayList<>();
            SolvableOpenQuestion o = null;
            for (int i = 0; i < test.getOpenQuestions().size(); i++) {
                o = test.getOpenQuestions().get(i);
                o.setAnswer(input.getOpenQuestions().get(i).getAnswer());
                open.add(o);
            }
            test.setOpenQuestions(open);

            calculateClosed(test.getCloseQuestions());
            calculateOpen(test.getOpenQuestions());
            test.calculatePoints();

            test.setStatus(2);

            DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
                    .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                    .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
                    .build();

            getMapper().save(test, dynamoDBMapperConfig);
            return new Response(200, "Test successfully saved");

        }

        return new Response(400, "Input can't be empty");
    }

    private void calculateClosed(List<SolvableClosedQuestion> closed) {
        float sum;
        for (int i = 0; i < closed.size(); i++) {
            sum = 0;
            for (Integer j : closed.get(i).getChosenAnswers()) {
                if (closed.get(i).getCorrectAnswers().contains(j)) {
                    sum += (closed.get(i).getMaxScore() / (closed.get(i).getCorrectAnswers().size() * 1.0));
                }
            }
            closed.get(i).setReceivedScore(sum);
        }
    }

    private void calculateOpen(List<SolvableOpenQuestion> open) {
        for (SolvableOpenQuestion a : open) {
            if (a.getCorrectAnswer().equalsIgnoreCase(a.getAnswer())) {
                a.setReceivedScore(a.getMaxScore());
            }
        }

    }
}
