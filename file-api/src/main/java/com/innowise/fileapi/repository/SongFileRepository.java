package com.innowise.fileapi.repository;


import com.innowise.fileapi.model.SongFile;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface SongFileRepository extends ListCrudRepository<SongFile,Integer> {
    Optional<SongFile> findFirstByUsernameAndHashedFilename(String username, String hashedFilename);
}
