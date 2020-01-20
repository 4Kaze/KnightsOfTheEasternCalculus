package lambda;

public class AuthenticatedRequest<T> {
    private String userId;
    private String group;
    private T body;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public boolean isRecruiter() {
        return this.group != null && this.group.equals("recruiter");
    }
}
