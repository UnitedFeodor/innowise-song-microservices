package com.innowise.fileapi.repository.impl;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class LocalSongStorageRepository implements SongStorageRepository {

    @Value("${storage.local.path}")
    private String storagePath;

    @Override
    public SongSaveResult save(MultipartFile song) {
        String songFilename = song.getOriginalFilename();
        var hashedFilename = DigestUtils.md5Digest(
                Objects
                    .requireNonNull(songFilename)
                    .getBytes(StandardCharsets.UTF_8)
        );

        var filePath = System.getenv(storagePath) + "/" + Arrays.toString(hashedFilename);
        try {
            song.transferTo(Path.of(filePath));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return SongSaveResult.builder()
                .storageType(StorageType.LOCAL)
                .storagePath(filePath)
                .build();

    }
}
