package pl.wolnikradoslaw.birdwatchbackend.infrastructure.storage;

import java.io.InputStream;

public interface FileStorageClient {
    void uploadFile(String bucket, String objectKey, InputStream inputStream, long size, String contentType);
    String getPresignedUrl(String bucket, String objectKey);
    void deleteFile(String bucket, String objectKey);
}