package lambda.applicant;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;
import lambda.Handler;
import lambda.Response;
import lambda.applicant.applicant.Applicant;
import lambda.request.AuthenticatedRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadPhoto extends Handler<AuthenticatedRequest<String>> {
    @Override
    public Response handleRequest(AuthenticatedRequest<String> authenticatedRequest, Context context) {
        if(authenticatedRequest.isRecruiter())
            return new Response(403, "Only replicants can upload photos");

        Applicant applicant = getMapper().load(Applicant.class, authenticatedRequest.getUserId());
        AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        try {
            s3.getObjectAcl("applicant-photos", authenticatedRequest.getUserId()+".png");
            if(applicant != null)
                return new Response(409, "The photo for this replicant already exists");
        } catch(SdkClientException ex) {

        }
        String input = authenticatedRequest.getBody().replaceFirst("data:image/.*;base64,", "");
        File file = createFile(input, authenticatedRequest.getUserId());
        if(file == null)
            return new Response(500, "Error processing image");

        PutObjectRequest request = new PutObjectRequest("applicant-photos",
                authenticatedRequest.getUserId()+".png",file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        request.setMetadata(metadata);
        s3.putObject(request);

        return new Response(200, "Photo uploaded successfully");
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
