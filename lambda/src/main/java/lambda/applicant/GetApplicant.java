package lambda.applicant;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;
import lambda.AuthenticatedRequest;
import lambda.util.Utils;

class GetApplicant extends AuthenticatedHandler<String> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(!isRecruiter() && !getUserId().equals(getBody()))
            return responseOf(403, "Insufficient permissions");

        Applicant applicant = getMapper().load(Applicant.class, getBody());
        if(applicant == null)
            return responseOf(404, "Applicant was not found");
        else {
            applicant.setLastName(Utils.capitalize(applicant.getLastName()));
            return responseOf(200, applicant);
        }
    }
}
