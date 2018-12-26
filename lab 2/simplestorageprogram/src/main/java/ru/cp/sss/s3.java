package ru.cp.sss;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class s3 {


    public static void main(String[] args0) throws IOException {
        AWSCredentials credentials = new BasicAWSCredentials("aomKJNkuATw7UebGDxdRAv", "7iSqkKBC51NCXFrVeJtT1PsbRWVS31fDw6nZ6CRKvK56");
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocol(Protocol.HTTP);
        AmazonS3 s3Client = new AmazonS3Client(credentials, config);
        s3Client.setEndpoint("hb.bizmrg.com");
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).disableChunkedEncoding().build());

        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
        String command;
        while (!(command = reader.readLine()).equalsIgnoreCase("exit")){
            if ("file_list".equalsIgnoreCase(command)){
                printAllObjects(s3Client);
            }
            if ("get_file".equalsIgnoreCase(command)){
                System.out.print("input key in s3: ");
                String key = reader.readLine();
                System.out.println();
                System.out.println("input local file name: ");
                String localFileName = reader.readLine();
                System.out.println();
                getFileS3(s3Client, key, localFileName);
            }
            if ("set_file".equalsIgnoreCase(command)){
                System.out.println("input local file name: ");
                String localFileName = reader.readLine();
                System.out.print("input key in s3: ");
                String key = reader.readLine();
                setFileS3(s3Client, key, localFileName);
            }
        }
        System.out.println();
    }

    public static void printAllObjects(AmazonS3 client){
        ObjectListing listing = client.listObjects(new ListObjectsRequest()
                .withBucketName("bucketbucket")
                .withPrefix(""));
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        for (S3ObjectSummary objectSummary : summaries){
            System.out.println(objectSummary.getKey());
        }
    }

    public static void getFileS3(AmazonS3 client, String key,String localFileName){
        File localFile = new File(localFileName);

        ObjectMetadata s3object = client.getObject(new GetObjectRequest(
                "bucketbucket", key), localFile);
        System.out.println("download file complete");
    }

    public static void setFileS3(AmazonS3 client, String key, String localFileName){
        PutObjectRequest request = new PutObjectRequest("bucketbucket", key, new File(localFileName));
        client.putObject(request);
        System.out.println("upload file complete");
    }
}
