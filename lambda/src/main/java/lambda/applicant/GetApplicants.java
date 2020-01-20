package lambda.applicant;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.ApplicantListItem;
import lambda.request.AuthenticatedRequest;
import lambda.util.Utils;

import java.util.*;

class GetApplicants extends Handler<AuthenticatedRequest<String>> {

    @Override
    public Response handleRequest(AuthenticatedRequest<String> authInput, Context context) {
        if(!authInput.isRecruiter())
            return new Response(403, "Recruiter permissions required");

        DynamoDBScanExpression scanExpression;
        String input = authInput.getBody().toLowerCase();

        if(input.isEmpty()) {
            scanExpression = new DynamoDBScanExpression();
        } else {
            Map<String, AttributeValue> eav = new HashMap<>();
            eav.put(":val", new AttributeValue().withS(input));

            scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("contains(lastName, :val)")
                    .withExpressionAttributeValues(eav)
                    .withProjectionExpression("id, firstName, lastName, email, universities");
        }

        List<ApplicantListItem> queryList = new ArrayList<>(getMapper().scan(ApplicantListItem.class,scanExpression));
        queryList.sort(Comparator.comparing(ApplicantListItem::getLastName));
        queryList.forEach(replicant -> replicant.setLastName(Utils.capitalize(replicant.getLastName())));
        return new Response(200, queryList);
    }
}
