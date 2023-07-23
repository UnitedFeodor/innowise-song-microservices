package com.innowise.fileapi.service;

import com.innowise.contractapi.dto.SongSaveResult;
import com.innowise.contractapi.model.SongFile;
import com.innowise.contractapi.model.StorageType;
import com.innowise.fileapi.repository.SongFileRepository;
import com.innowise.fileapi.repository.impl.LocalSongStorageRepository;
import com.innowise.fileapi.repository.impl.S3SongStorageRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongFileService {

    private final SongFileRepository songFileRepo;
    private final S3SongStorageRepository s3SongStorageRepository;
    private final LocalSongStorageRepository localSongStorageRepository;

    private final SqsTemplate sqsTemplate;
    @Value("${spring.cloud.cloud.sqs.queue-url}")
    String queueUrl;
    public SongSaveResult uploadFile(String username, MultipartFile file) {

        SongSaveResult songSaveResult;
        try {
            songSaveResult = s3SongStorageRepository.save(file); //TODO add retry
        } catch (Exception e) {
            songSaveResult = localSongStorageRepository.save(file);
        }

        String originalFilename = file.getOriginalFilename();
        SongFile songFile = SongFile.builder()
                .originalFilename(originalFilename)
                .hashedFilename(songSaveResult.getHashedFilename())
                .username(username)
                .storagePath(songSaveResult.getStoragePath())
                .storageType(songSaveResult.getStorageType())
                .build();
        SongFile savedFile = songFileRepo.save(songFile);
        songSaveResult.setFileApiId(savedFile.getId());

        sqsTemplate.send(queueUrl,songSaveResult);
        return songSaveResult;
    }

    public byte[] downloadFile(String username, String hashedFilename) {
        SongFile songFile = songFileRepo.findFirstByUsernameAndHashedFilename(username, hashedFilename);
        return loadSongFromStorage(songFile);

    }

    private byte[] loadSongFromStorage(SongFile songFile) {
        StorageType storageType = songFile.getStorageType();
        switch (storageType) {
            case S3 -> {
                return s3SongStorageRepository.load(songFile);
            }
            case LOCAL -> {
                return localSongStorageRepository.load(songFile);
            }
            default -> {
                throw new IllegalArgumentException("Incorrect storage type");
            }
        }
    }

    public byte[] downloadFile(Integer songId) {
        SongFile songFile = songFileRepo.findById(songId).orElseThrow(() -> new IllegalArgumentException("Incorrect song id"));
        return loadSongFromStorage(songFile);
    }
}
