package com.innowise.fileapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongFile {

    @Id
    Integer id;
    String fileName;

    StorageType storageType;
    String storagePath;

    String username;
}
