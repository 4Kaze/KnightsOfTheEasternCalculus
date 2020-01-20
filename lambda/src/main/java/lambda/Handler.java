package lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public abstract class Handler<I> implements RequestHandler<I, Response> {
    private final DynamoDBMapper dynamoDBMapper;

    protected Handler() {
        this.dynamoDBMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    }

    protected DynamoDBMapper getMapper() {
        return dynamoDBMapper;
    }
}
