package com.innowise.fileapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongFileRepository;
import com.innowise.fileapi.repository.SongStorageRepository;
import com.innowise.fileapi.repository.impl.LocalSongStorageRepository;
import com.innowise.fileapi.repository.impl.S3SongStorageRepository;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongFileService {

    private final SongFileRepository songFileRepo;
    private final S3SongStorageRepository s3SongStorageRepository;
    private final LocalSongStorageRepository localSongStorageRepository;

    private final SqsTemplate sqsTemplate;

    private final ObjectMapper objectMapper;
    @Value("${spring.cloud.aws.sqs.queue-url}")
    String queueUrl;
    @Transactional
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

        String songSaveResultMessage = null;
        try {
            songSaveResultMessage = objectMapper.writeValueAsString(songSaveResult);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        sqsTemplate.send(queueUrl, Objects.requireNonNull(songSaveResultMessage));
        log.info("Sent songSaveResult to sqs as json {}",songSaveResult);

        return songSaveResult;
    }

    @Transactional
    public byte[] downloadFile(String username, String hashedFilename) {
        SongFile songFile = songFileRepo.findFirstByUsernameAndHashedFilename(username, hashedFilename)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect song hashed filename"));
        return loadSongFromStorage(songFile);
    }

    @Transactional
    public byte[] downloadFile(Integer songId) {
        SongFile songFile = songFileRepo.findById(songId).orElseThrow(() -> new IllegalArgumentException("Incorrect song id"));
        return loadSongFromStorage(songFile);
    }

    private byte[] loadSongFromStorage(SongFile songFile) {
        StorageType storageType = songFile.getStorageType();

        SongStorageRepository songStorageRepo = getSongStorageRepoByType(storageType);
        return songStorageRepo.load(songFile);
    }

    private SongStorageRepository getSongStorageRepoByType(StorageType storageType) {
        switch (storageType) {
            case S3 -> {
                return s3SongStorageRepository;
            }
            case LOCAL -> {
                return localSongStorageRepository;
            }
            default -> {
                throw new IllegalArgumentException("Incorrect storage type");
            }
        }
    }

    public void deleteFile(Integer songId) {
        SongFile songFile = songFileRepo.findById(songId).orElseThrow(() -> new IllegalArgumentException("Incorrect song id"));
        SongStorageRepository songStorageRepo = getSongStorageRepoByType(songFile.getStorageType());

        songStorageRepo.delete(songFile);
        songFileRepo.deleteById(songId);
        log.info("Deleted file {}",songFile);
    }
}
