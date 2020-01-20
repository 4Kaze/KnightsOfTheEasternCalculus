package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.TestInstance;
import lambda.AuthenticatedRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GetTestInstancesForUser extends AuthenticatedHandler<String> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(!isRecruiter() && !getUserId().equals(getBody()))
            return responseOf(403, "Insufficient permissions");

        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":id", new AttributeValue().withS(getBody()));

        DynamoDBQueryExpression<TestInstance> query = new DynamoDBQueryExpression<TestInstance>()
                .withKeyConditionExpression("applicantId = :id")
                .withExpressionAttributeValues(attributeValues);

        if(isRecruiter()) {
            attributeValues.put(":id2", new AttributeValue().withS(getUserId()));
            query.setFilterExpression("recruiterId = :id2");
        }

        List<TestInstance> tab = getMapper().query(TestInstance.class, query);
        tab.forEach(test -> {
            test.getCloseQuestions().forEach(question -> question.setCorrectAnswers(null));
            test.getOpenQuestions().forEach(question -> question.setCorrectAnswer(null));
            test.getValueQuestions().forEach(question -> question.setCorrectAnswer(null));
        });

        return responseOf(200, tab);
    }
}
