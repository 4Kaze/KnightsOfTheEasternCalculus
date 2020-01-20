package lambda.applicant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.Base64;
import com.amazonaws.util.IOUtils;
import lambda.request.AuthenticatedRequest;

import java.io.IOException;

class GetPhoto implements RequestHandler<AuthenticatedRequest<String>, String> {
    @Override
    public String handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        if(!authenticatedRequest.isRecruiter() && !authenticatedRequest.getUserId().equals(authenticatedRequest.getBody()))
            return "Insufficient permissions";

        AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        S3Object photo = s3.getObject("applicant-photos", authenticatedRequest.getBody()+".png");
        try(S3ObjectInputStream photoStream = photo.getObjectContent()) {
            return Base64.encodeAsString(IOUtils.toByteArray(photoStream));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error during reading file";
        } finally {
            try {
                if(photo != null) photo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
