package com.innowise.fileapi.service;

import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongFileRepository;
import lombok.AllArgsConstructor;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class SongFileService {

    SongFileRepository songFileRepo;
    public String uploadFile(String username, MultipartFile file) {

        String storageType = StorageType.S3.toString(); // TODO remove stub

        String fileName = file.getOriginalFilename();
        SongFile songFile = SongFile.builder()
                .fileName(fileName)
                .username(username)
                .storagePath()
                .storageType(storageType)
                .build();
        songFileRepo.save(songFile);
        return storageType;
    }
}
