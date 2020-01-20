package lambda.applicant;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;

class AddApplicant extends Handler<Applicant> {

    @Override
    public Response handleRequest(Applicant input, Context context) {
        if (getMapper().load(Applicant.class, input.getId()) == null) {
            input.setLastName(input.getLastName().toLowerCase());
            getMapper().save(input);
            return responseOf(200, input);
        } else {
            return responseOf(409, "Replicant already exists");
        }
    }
}
