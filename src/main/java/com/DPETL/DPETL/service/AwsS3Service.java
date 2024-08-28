package com.DPETL.DPETL.service;


import com.DPETL.DPETL.exception.OurException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AwsS3Service {

    private final String bucketAppelOffresDocuments = "appeloffresdocuments";
    private final String bucketOffresDocuments = "offresdocuments";

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    public String saveAppelOffresDocumentsToS3(MultipartFile file) {
        String s3LocationFile = null;

        try {

            String s3Filename = file.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1)
                    .build();

            InputStream inputStream = file.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType()); // Dynamically set content type based on file
            metadata.setContentLength(file.getSize()); // Ensure Content-Length is set

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketAppelOffresDocuments, s3Filename, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + bucketAppelOffresDocuments + ".s3.amazonaws.com/" + s3Filename;

        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to upload file to s3 bucket" + e.getMessage());
        }

    }

    public String saveOffresDocumentsToS3(MultipartFile file) {
        String s3LocationFile = null;

        try {

            String s3Filename = file.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1)
                    .build();

            InputStream inputStream = file.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/pdf");

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketOffresDocuments, s3Filename, inputStream, metadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + bucketOffresDocuments + ".s3.amazonaws.com/" + s3Filename;

        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to upload file to s3 bucket" + e.getMessage());
        }

    }

    public void deleteAppelOffresDocumentsFromS3Bucket(String fileUrl) {
        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1)
                    .build();

            // Extract the bucket name and file key from the file URL

            String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            s3Client.deleteObject(new DeleteObjectRequest(bucketAppelOffresDocuments, fileKey));
        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to delete file from S3 bucket: " + e.getMessage());
        }
    }

    public void deleteOffresDocumentsFromS3Bucket(String fileUrl) {
        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1)
                    .build();

            // Extract the bucket name and file key from the file URL

            String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            s3Client.deleteObject(new DeleteObjectRequest(bucketOffresDocuments, fileKey));
        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to delete file from S3 bucket: " + e.getMessage());
        }
    }




}
