package lambda.tools.translator;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.test.model.Test;
import lambda.AuthenticatedRequest;
import lambda.tools.Tools;

class TranslateTest extends Handler<AuthenticatedRequest<Test>> {

    @Override
    public Response handleRequest(AuthenticatedRequest<Test> input, Context context) {
        if (!input.isRecruiter() || !input.getUserId().equals(input.getBody().getRecruiterId())) {
            return new Response(403, "Recruiter permissions required");
        }
        Test test = input.getBody();
        Tools tools = new Tools(test);
        test = tools.translateTest();
        return new Response(200, test);
    }
}
