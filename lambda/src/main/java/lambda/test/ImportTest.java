package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;

class ImportTest extends Handler<String> {

    @Override
    public Response handleRequest(String s, Context context) {
        return null;
    }
}
