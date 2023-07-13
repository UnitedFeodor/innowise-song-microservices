package com.innowise.fileapi.service;

import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongFileRepository;
import com.innowise.fileapi.repository.impl.LocalSongStorageRepository;
import com.innowise.fileapi.repository.impl.S3SongStorageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class SongFileService {

    SongFileRepository songFileRepo;
    S3SongStorageRepository s3SongStorageRepository;
    LocalSongStorageRepository localSongStorageRepository;

    public String uploadFile(String username, MultipartFile file) {

        var storageType = StorageType.S3;
        try {
            s3SongStorageRepository.save(file); //TODO add retry
        } catch (Exception e) {
            localSongStorageRepository.save(file);
            storageType = StorageType.LOCAL;
        }

        String fileName = file.getOriginalFilename();
        SongFile songFile = SongFile.builder()
                .fileName(fileName)
                .username(username)
                .storagePath(fileName)
                .storageType(storageType)
                .build();
        songFileRepo.save(songFile);
        return storageType.toString();
    }
}
