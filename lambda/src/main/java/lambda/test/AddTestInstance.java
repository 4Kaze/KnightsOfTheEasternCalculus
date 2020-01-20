package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.TestInstance;

public class AddTestInstance extends Handler<TestInstance> {
    @Override
    public Response handleRequest(TestInstance input, Context context) {
        if (input != null) {
            if (input.getApplicantId() != null && input.getTimestamp() != 0) {
                getMapper().save(input);
                return responseOf(200, "TestInstance added successfully");
            } else {
                return responseOf(400, input);
            }
        } else {
            return responseOf(500, "Input can't be empty");
        }
    }
}
