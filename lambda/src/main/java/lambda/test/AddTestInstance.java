package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import model.test.TestInstance;
import lambda.Response;

public class AddTestInstance extends Handler<TestInstance> {
    @Override
    public Response handleRequest(TestInstance input, Context context) {
        if (input != null) {
            if (input.getApplicantId() != null && input.getTimestamp() != 0) {
                getMapper().save(input);
                return new Response(200, "TestInstance added successfully");
            } else {
                return new Response(400, input);
            }
        } else {
            return new Response(500, "Input can't be empty");
        }
    }
}
