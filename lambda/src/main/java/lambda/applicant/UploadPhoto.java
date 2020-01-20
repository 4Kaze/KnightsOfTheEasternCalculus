package lambda.applicant;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;
import lambda.AuthenticatedHandler;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;
import lambda.AuthenticatedRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadPhoto extends AuthenticatedHandler<String> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        super.handleRequest(authenticatedRequest, context);
        if(isRecruiter())
            return responseOf(403, "Only replicants can upload photos");

        Applicant applicant = getMapper().load(Applicant.class, getUserId());
        AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        try {
            s3.getObjectAcl("applicant-photos", getUserId()+".png");
            if(applicant != null)
                return responseOf(409, "The photo for this replicant already exists");
        } catch(SdkClientException ignored) {

        }
        String input = getBody().replaceFirst("data:image/.*;base64,", "");
        File file = createFile(input, getUserId());
        if(file == null)
            return responseOf(500, "Error processing image");

        PutObjectRequest request = new PutObjectRequest("applicant-photos",
                getUserId()+".png",file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        request.setMetadata(metadata);
        s3.putObject(request);

        return responseOf(200, "Photo uploaded successfully");
    }
    private File createFile(String encoded, String username) {
        try(InputStream in = new ByteArrayInputStream(Base64.decode(encoded))) {
            BufferedImage image = ImageIO.read(in);
            Dimension dimensions = getScaledDimension(new Dimension(image.getWidth(), image.getHeight()), new Dimension(400, 400));
            Image img = image.getScaledInstance(dimensions.width, dimensions.height, Image.SCALE_SMOOTH);
            BufferedImage scaledImage = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();
            File output = new File("/tmp/"+username+".png");
            ImageIO.write(scaledImage, "png", output);
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {
        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        return new Dimension((int) (imageSize.width  * ratio),
                (int) (imageSize.height * ratio));
    }

}
