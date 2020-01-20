package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import lambda.test.model.TestRequest;

import java.util.Optional;

public abstract class AuthenticatedHandler<T> extends Handler<AuthenticatedRequest<T>> {
    private String userId;
    private boolean recruiter;
    private T body;

    @Override
    public Response handleRequest(AuthenticatedRequest<T> authenticatedRequest, Context context) {
        this.userId = authenticatedRequest.getUserId();
        this.recruiter = authenticatedRequest.isRecruiter();
        this.body = authenticatedRequest.getBody();
        return null;
    }

    protected Optional<Response> requireRecruiter() {
        if(!recruiter) return Optional.of(responseOf(403, "Recruiter permissions required"));
        return Optional.empty();
    }

    protected Optional<Response> requireRecruiterWithValidRequest() {
        return Optional.ofNullable(requireRecruiter().orElseGet(() -> {
            if(body instanceof TestRequest && !((TestRequest) body).getOwnerId().equals(userId))
                return responseOf(403, "Insufficient permissions");
            else return null;
        }));
    }

    public String getUserId() {
        return userId;
    }

    public boolean isRecruiter() {
        return recruiter;
    }

    public T getBody() {
        return body;
    }
}
