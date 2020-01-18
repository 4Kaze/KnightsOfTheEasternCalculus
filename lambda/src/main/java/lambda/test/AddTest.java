package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import model.request.AuthenticatedRequest;
import model.test.Test;
import lambda.Response;

import java.util.Date;

public class AddTest extends Handler<AuthenticatedRequest<Test>> {

    @Override
    public Response handleRequest(AuthenticatedRequest<Test> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter())
            return new Response(403, "Recruiter permissions required");

        Test input = authenticatedRequest.getBody();
        input.setSearchTitle(input.getTitle().toLowerCase());
        input.setTestId(new Date().getTime());
        getMapper().save(input);
        return new Response(200, input);
    }
}
