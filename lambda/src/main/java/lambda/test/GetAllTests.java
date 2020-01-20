package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.Test;
import lambda.AuthenticatedRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GetAllTests extends Handler<AuthenticatedRequest<GetAllTests.TestQuery>> {

    @Override
    public Response handleRequest(AuthenticatedRequest<GetAllTests.TestQuery> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");
        if(!authenticatedRequest.getUserId().equals(authenticatedRequest.getBody().getOwnerId()))
            return new Response(403, "Insufficient permissions");

        TestQuery input = authenticatedRequest.getBody();
        String title = input.getTitle().toLowerCase();
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":id", new AttributeValue().withS(input.getOwnerId()));

        DynamoDBQueryExpression<Test> query = new DynamoDBQueryExpression<Test>()
                .withKeyConditionExpression("recruiterId = :id")
                .withExpressionAttributeValues(attributeValues);

        if(!input.getTitle().isEmpty()) {
            attributeValues.put(":title", new AttributeValue().withS(title));
            query.setFilterExpression("contains(searchTitle, :title)");
        }

        List<Test> tab = getMapper().query(Test.class, query);

        return new Response(200, tab);
    }

    static class TestQuery {
        private String ownerId;
        private String title;

        String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}


