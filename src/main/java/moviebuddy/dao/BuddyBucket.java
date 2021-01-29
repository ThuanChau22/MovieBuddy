package moviebuddy.dao;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.time.LocalDateTime;

public class BuddyBucket {
    private static final String PROFILE = "moviebuddy";
    private static final String BUCKET = "moviebuddy-157a";
    private static final String POSTER_DIR = "posters/";
    private static final String TRAILER_DIR = "trailers/";

    public static String uploadPoster(String posterId, InputStream content, long size) {
        try {
            AWSCredentials credentials = new ProfileCredentialsProvider(PROFILE).getCredentials();
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_WEST_1)
                    .build();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            metadata.setContentLength(size);
            String objectKey = POSTER_DIR + posterId;
            PutObjectRequest request = new PutObjectRequest(BUCKET, objectKey, content, metadata);
            s3Client.putObject(request);
            return s3Client.getUrl(BUCKET, objectKey).toString() + "?" + LocalDateTime.now();
        } catch (AmazonServiceException ase) {
            serverExceptionMessage(ase);
        } catch (AmazonClientException ace) {
            clientExceptionMessage(ace);
        }
        return "";
    }

    public static void deletePoster(String posterId) {
        try {
            AWSCredentials credentials = new ProfileCredentialsProvider(PROFILE).getCredentials();
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_WEST_1)
                    .build();
            s3Client.deleteObject(BUCKET, POSTER_DIR + posterId);
        } catch (AmazonServiceException ase) {
            serverExceptionMessage(ase);
        } catch (AmazonClientException ace) {
            clientExceptionMessage(ace);
        }
    }

    public static String uploadTrailer(String trailerId, InputStream content, long size) {
        try {
            AWSCredentials credentials = new ProfileCredentialsProvider(PROFILE).getCredentials();
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_WEST_1)
                    .build();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("video/mp4");
            metadata.setContentLength(size);
            String objectKey = TRAILER_DIR + trailerId;
            PutObjectRequest request = new PutObjectRequest(BUCKET, objectKey, content, metadata);
            s3Client.putObject(request);
            return s3Client.getUrl(BUCKET, objectKey).toString() + "?" + LocalDateTime.now();
        } catch (AmazonServiceException ase) {
            serverExceptionMessage(ase);
        } catch (AmazonClientException ace) {
            clientExceptionMessage(ace);
        }
        return "";
    }

    public static void deleteTrailer(String trailerId) {
        try {
            AWSCredentials credentials = new ProfileCredentialsProvider(PROFILE).getCredentials();
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_WEST_1)
                    .build();
            s3Client.deleteObject(BUCKET, TRAILER_DIR + trailerId);
        } catch (AmazonServiceException ase) {
            serverExceptionMessage(ase);
        } catch (AmazonClientException ace) {
            clientExceptionMessage(ace);
        }
    }

    private static void serverExceptionMessage(AmazonServiceException ase) {
        System.out.println("Request made it to S3, but was rejected with an error response.");
        System.out.println("Error Message:    " + ase.getMessage());
        System.out.println("HTTP Status Code: " + ase.getStatusCode());
        System.out.println("AWS Error Code:   " + ase.getErrorCode());
        System.out.println("Error Type:       " + ase.getErrorType());
        System.out.println("Request ID:       " + ase.getRequestId());
    }

    private static void clientExceptionMessage(AmazonClientException ace) {
        System.out.println(
                "Client encountered a serious internal problem while trying to communicate with S3, such as not being able to access the network.");
        System.out.println("Error Message: " + ace.getMessage());
    }
}
