package com.innowise.fileapi.repository;

import com.innowise.contractapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import org.springframework.web.multipart.MultipartFile;

public interface SongStorageRepository {
    SongSaveResult save(MultipartFile song);

    byte[] load(SongFile songInfo);

}
