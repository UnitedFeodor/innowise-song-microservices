package com.innowise.fileapi.repository.impl;

import com.innowise.contractapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import com.innowise.contractapi.model.StorageType;
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

            PutObjectRequest songPutObjectRequest = PutObjectRequest
                    .builder()
                    .bucket(s3BucketName)
                    .key(hashedFilename)
                    .build();

            s3Client.putObject(songPutObjectRequest, songRequestBody);
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
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(s3BucketName)
                .key(songInfo.getStoragePath())
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();

    }
}
