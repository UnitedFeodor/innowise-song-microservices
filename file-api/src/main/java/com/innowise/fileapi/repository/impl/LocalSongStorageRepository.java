package com.innowise.fileapi.repository.impl;

import com.innowise.contractapi.dto.SongSaveResult;
import com.innowise.contractapi.model.SongFile;
import com.innowise.contractapi.model.StorageType;
import com.innowise.fileapi.repository.SongStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class LocalSongStorageRepository implements SongStorageRepository {

    @Value("${storage.local.path}")
    private String storagePath;

    @Override
    public SongSaveResult save(MultipartFile song) {
        String songFilename = song.getOriginalFilename();
        var hashedFilename = DigestUtils.sha256Hex(songFilename);

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

        log.info(filePath.toAbsolutePath().toString());
        return SongSaveResult.builder()
                .storageType(StorageType.LOCAL)
                .storagePath(filePath.toString())
                .hashedFilename(hashedFilename)
                .build();
    }

    @Override
    public byte[] load(SongFile songFile) {
        try {
            return Files.readAllBytes(Path.of(songFile.getStoragePath()));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
