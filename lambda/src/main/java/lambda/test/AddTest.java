package lambda.test;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Response;
import lambda.test.model.Test;
import lambda.AuthenticatedRequest;

import java.util.Date;

class AddTest extends AuthenticatedHandler<Test> {

    @Override
    public Response handleRequest(AuthenticatedRequest<Test> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(requireRecruiter().isPresent())
            return requireRecruiter().get();

        Test input = getBody();
        input.setSearchTitle(input.getTitle().toLowerCase());
        input.setTestId(new Date().getTime());
        getMapper().save(input);
        return responseOf(200, input);
    }
}
