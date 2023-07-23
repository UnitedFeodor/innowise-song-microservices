package com.innowise.fileapi.repository;


import com.innowise.contractapi.model.SongFile;
import org.springframework.data.repository.ListCrudRepository;

public interface SongFileRepository extends ListCrudRepository<SongFile,Integer> {
    SongFile findFirstByUsernameAndHashedFilename(String username, String hashedFilename);
}
