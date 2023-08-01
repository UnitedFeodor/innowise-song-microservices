package com.innowise.enricherapi.dto;

import com.innowise.enricherapi.model.StorageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongSaveResult {
    private StorageType storageType;
    private String storagePath;

    private Integer fileApiId;
    private String token;

    private String hashedFilename;
}
