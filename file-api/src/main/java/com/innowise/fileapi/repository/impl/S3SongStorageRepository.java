package com.innowise.fileapi.repository.impl;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongStorageRepository;
import org.springframework.web.multipart.MultipartFile;

public class S3SongStorageRepository implements SongStorageRepository {
    @Override
    public SongSaveResult save(MultipartFile song) {


        return SongSaveResult.builder()
                .storageType(StorageType.S3)
                .storagePath(filePath)
                .build();
    }
}
