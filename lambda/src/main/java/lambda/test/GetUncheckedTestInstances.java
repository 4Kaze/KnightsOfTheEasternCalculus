package lambda.test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.TestInstance;
import lambda.AuthenticatedRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUncheckedTestInstances extends AuthenticatedHandler<String> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiter().isPresent())
            return requireRecruiter().get();

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":s", new AttributeValue().withN("1"));
        eav.put(":id", new AttributeValue().withS(getUserId()));

        Map<String, String> names = new HashMap<>();
        names.put("#st", "status");

        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("#st = :s AND recruiterId = :id")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(names);

        List<TestInstance> scanList = new ArrayList<>(getMapper().scan(TestInstance.class, scan));
        return responseOf(200, scanList);
    }
}
