package lambda.applicant;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;
import lambda.request.AuthenticatedRequest;
import lambda.util.Utils;

class GetApplicant extends Handler<AuthenticatedRequest<String>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter() && !authenticatedRequest.getUserId().equals(authenticatedRequest.getBody()))
            return new Response(403, "Insufficient permissions");

        Applicant applicant = getMapper().load(Applicant.class, authenticatedRequest.getBody());
        if(applicant == null)
            return new Response(404, "Applicant was not found");
        else {
            applicant.setLastName(Utils.capitalize(applicant.getLastName()));
            return new Response(200, applicant);
        }
    }
}
