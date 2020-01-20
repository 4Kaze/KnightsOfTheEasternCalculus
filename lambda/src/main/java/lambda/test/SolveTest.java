package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import model.*;
import lambda.Response;

import java.util.ArrayList;
import java.util.List;

class SolveTest extends Handler<AuthenticatedRequest<TestInstance>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<TestInstance> authenticatedRequest, Context context) {
        if(!authenticatedRequest.getUserId().equals(authenticatedRequest.getBody().getApplicantId()))
            return new Response(403, "Insufficient permissions");

        TestInstance input = authenticatedRequest.getBody();
        TestInstance test = getMapper().load(TestInstance.class, input.getApplicantId(), input.getTimestamp());

        if (test.getApplicantId() == null) {
            return new Response(400, "ApplicantID can't be null");
        }

        test.setReceivedScore(0);

        if (test.getCloseQuestions() != null) {
            List<SolvableClosedQuestion> close = new ArrayList<>();
            SolvableClosedQuestion c;
            for (int i = 0; i < test.getCloseQuestions().size(); i++) {
                c = test.getCloseQuestions().get(i);
                c.setChosenAnswers(input.getCloseQuestions().get(i).getChosenAnswers());
                close.add(c);
            }
            test.setCloseQuestions(close);
            calculateClosed(test.getCloseQuestions());

        } else {
            test.setCloseQuestions(new ArrayList<>());
        }

        if (test.getOpenQuestions() != null) {
            List<SolvableOpenQuestion> open = new ArrayList<>();
            SolvableOpenQuestion o;
            for (int i = 0; i < test.getOpenQuestions().size(); i++) {
                o = test.getOpenQuestions().get(i);
                o.setAnswer(input.getOpenQuestions().get(i).getAnswer());
                o.setReceivedScore(input.getOpenQuestions().get(i).getReceivedScore());
                open.add(o);
            }
            test.setOpenQuestions(open);

        } else {
            test.setOpenQuestions(new ArrayList<>());
        }

        if (test.getValueQuestions() != null) {
            List<SolvableValueQuestion> value = new ArrayList<>();
            SolvableValueQuestion v;
            for (int i = 0; i < test.getValueQuestions().size(); i++) {
                v = test.getValueQuestions().get(i);
                v.setAnswer(input.getValueQuestions().get(i).getAnswer());
                v.setReceivedScore(input.getValueQuestions().get(i).getReceivedScore());
                value.add(v);
            }
            test.setValueQuestions(value);
            calculateValue(test.getValueQuestions());

        } else {
            test.setValueQuestions(new ArrayList<>());
        }

        test.calculatePoints();

        test.setStatus(TestStatus.SOLVED.getValue());

        DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE)
                .build();

        getMapper().save(test, dynamoDBMapperConfig);
        return new Response(200, test);

    }

    private void calculateClosed(List<SolvableClosedQuestion> closed) {
        closed.forEach(question -> {
            question.setReceivedScore(question.getChosenAnswers().stream()
                    .reduce(0, (sum, answer) -> sum + question.getAnswerScore() *
                            (question.getCorrectAnswers().contains(answer) ? 1 : -1)));
            if (question.getReceivedScore() < 0) question.setReceivedScore(0);
        });
    }

    private void calculateValue(List<SolvableValueQuestion> value) {
        value.forEach( question -> question.setReceivedScore((Math.abs(question.getAnswer() - question.getCorrectAnswer()) < 0.01
                ? question.getMaxScore() : 0)));
    }

}
