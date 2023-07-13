package com.innowise.fileapi.repository.impl;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongStorageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class S3SongStorageRepository implements SongStorageRepository {

    private final S3Client s3Client;
    @Value("${storage.s3.bucket-name}")
    private String s3BucketName;
    @Override
    public SongSaveResult save(MultipartFile song) {

        String songFilename = song.getOriginalFilename();
        var hashedFilename = DigestUtils.md5Digest(
                Objects
                        .requireNonNull(songFilename)
                        .getBytes(StandardCharsets.UTF_8)
        );

        try {
            InputStream songInputStream = song.getInputStream();
            RequestBody songRequestBody = RequestBody.fromInputStream(songInputStream, songInputStream.available());

            PutObjectRequest songPutObjectRequest = PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(Arrays.toString(hashedFilename))
                    .build();

            s3Client.putObject(songPutObjectRequest, songRequestBody);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return SongSaveResult.builder()
                .storageType(StorageType.S3)
                .storagePath(Arrays.toString(hashedFilename))
                .build();
    }
}
