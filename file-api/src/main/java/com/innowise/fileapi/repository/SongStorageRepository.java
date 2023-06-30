package com.innowise.fileapi.repository;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import org.springframework.web.multipart.MultipartFile;

public interface SongStorageRepository {
    SongSaveResult save(MultipartFile song);
}
