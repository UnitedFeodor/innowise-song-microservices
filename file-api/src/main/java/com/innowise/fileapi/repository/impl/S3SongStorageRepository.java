package com.innowise.fileapi.repository.impl;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3SongStorageRepository implements SongStorageRepository {

    private final S3Client s3Client;
    @Value("${storage.s3.bucket-name}")
    private String s3BucketName;
    @Override
    public SongSaveResult save(MultipartFile song) {

        String songFilename = song.getOriginalFilename();
        var hashedFilename = DigestUtils.sha256Hex(songFilename);

        try {
            InputStream songInputStream = song.getInputStream();
            RequestBody songRequestBody = RequestBody.fromInputStream(songInputStream, songInputStream.available());

            PutObjectRequest putObjectRequest = PutObjectRequest
                    .builder()
                    .bucket(s3BucketName)
                    .key(hashedFilename)
                    .build();

            s3Client.putObject(putObjectRequest, songRequestBody);
            log.info("Saved song file to s3 with hashed name {}",hashedFilename);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return SongSaveResult.builder()
                .storageType(StorageType.S3)
                .storagePath(hashedFilename)
                .hashedFilename(hashedFilename)
                .build();
    }

    @Override
    public byte[] load(SongFile songInfo) {
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(s3BucketName)
                .key(songInfo.getStoragePath())
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
        log.info("Loaded song from s3 with hashed name {}",songInfo.getHashedFilename());

        return objectBytes.asByteArray();

    }

    @Override
    public void delete(SongFile songInfo) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder()
                .bucket(s3BucketName)
                .key(songInfo.getStoragePath())
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        log.info("Deleted song from s3 with hashed name {}",songInfo.getHashedFilename());
    }
}
