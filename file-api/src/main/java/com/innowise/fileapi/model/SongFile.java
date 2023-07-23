package com.innowise.fileapi.model;

import com.innowise.contractapi.model.StorageType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SongFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String originalFilename;
    private String hashedFilename;

    private StorageType storageType;
    private String storagePath;
    private String username;
}
