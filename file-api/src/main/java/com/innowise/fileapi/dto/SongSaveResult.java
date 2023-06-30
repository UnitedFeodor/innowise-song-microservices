package com.innowise.fileapi.dto;

import com.innowise.fileapi.model.StorageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class SongSaveResult {
    StorageType storageType;
    String storagePath;
}
