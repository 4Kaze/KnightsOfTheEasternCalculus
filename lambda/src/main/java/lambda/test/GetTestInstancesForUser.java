package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.request.AuthenticatedRequest;
import model.TestInstance;
import lambda.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTestInstancesForUser extends Handler<AuthenticatedRequest<String>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter() && !authenticatedRequest.getUserId().equals(authenticatedRequest.getBody()))
            return new Response(403, "Insufficient permissions");

        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":id", new AttributeValue().withS(authenticatedRequest.getBody()));

        DynamoDBQueryExpression<TestInstance> query = new DynamoDBQueryExpression<TestInstance>()
                .withKeyConditionExpression("applicantId = :id")
                .withExpressionAttributeValues(attributeValues);

        if(authenticatedRequest.isRecruiter()) {
            attributeValues.put(":id2", new AttributeValue().withS(authenticatedRequest.getUserId()));
            query.setFilterExpression("recruiterId = :id2");
        }

        List<TestInstance> tab = getMapper().query(TestInstance.class, query);

        for (int i = 0; i < tab.size(); i++) {
            if (tab.get(i).getCloseQuestions() != null) {
                for (int j = 0; j < tab.get(i).getCloseQuestions().size(); j++) {
                    tab.get(i).getCloseQuestions().get(j).setCorrectAnswers(new ArrayList<>());
                }
            } else {
                tab.get(i).setOpenQuestions(new ArrayList<>());
            }
            if (tab.get(i).getOpenQuestions() != null) {
                for (int j = 0; j < tab.get(i).getOpenQuestions().size(); j++) {
                    tab.get(i).getOpenQuestions().get(j).setCorrectAnswer("");
                }
            } else {
                tab.get(i).setOpenQuestions(new ArrayList<>());
            }
            if (tab.get(i).getValueQuestions() != null) {
                for (int j = 0; j < tab.get(i).getValueQuestions().size(); j++) {
                    tab.get(i).getValueQuestions().get(j).setCorrectAnswer(0f);
                }
            } else {
                tab.get(i).setValueQuestions(new ArrayList<>());
            }
        }

        return new Response(200, tab);
    }
}
