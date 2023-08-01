package com.innowise.fileapi.repository.impl;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class LocalSongStorageRepository implements SongStorageRepository {

    @Value("${storage.local.path}")
    private String storagePath;

    @Override
    public SongSaveResult save(MultipartFile song) {
        String songFilename = song.getOriginalFilename();
        var hashedFilename = DigestUtils.sha256Hex(songFilename + UUID.randomUUID());

        // TODO make use of env variables
        Path storageDirectory = Paths.get(storagePath);
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        Path filePath = storageDirectory.resolve(hashedFilename);
        try {
            song.transferTo(filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        log.info("Saved song file to local storage with hashed name {}",hashedFilename);
        log.info("Path to song is {}", filePath.toAbsolutePath());
        return SongSaveResult.builder()
                .storageType(StorageType.LOCAL)
                .storagePath(filePath.toString())
                .hashedFilename(hashedFilename)
                .build();
    }

    @Override
    public byte[] load(SongFile songFile) {
        try {
            byte[] songBytes = Files.readAllBytes(Path.of(songFile.getStoragePath()));
            log.info("Loaded song from local storage with hashed name {}",songFile.getHashedFilename());
            return songBytes;

        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void delete(SongFile songFile) {
        try {
            Files.delete(Path.of(songFile.getStoragePath()));
            log.info("Deleted song from local storage with hashed name {}",songFile.getHashedFilename());
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new UncheckedIOException(e);
        }
    }
}
