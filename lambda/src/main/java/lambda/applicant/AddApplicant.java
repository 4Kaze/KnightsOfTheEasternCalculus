package lambda.applicant;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;

public class AddApplicant extends Handler<Applicant> {

    @Override
    public Response handleRequest(Applicant input, Context context) {
        if (getMapper().load(Applicant.class, input.getId()) == null) {
            input.setLastName(input.getLastName().toLowerCase());
            getMapper().save(input);
            return new Response(200, input);
        } else {
            return new Response(409, "Replicant already exists");
        }
    }
}
