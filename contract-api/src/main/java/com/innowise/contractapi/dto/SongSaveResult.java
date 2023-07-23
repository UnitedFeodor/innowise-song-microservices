package com.innowise.contractapi.dto;

import com.innowise.contractapi.model.StorageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongSaveResult {
    private StorageType storageType;
    private String storagePath;

    private Integer fileApiId;

    private String hashedFilename;
}
