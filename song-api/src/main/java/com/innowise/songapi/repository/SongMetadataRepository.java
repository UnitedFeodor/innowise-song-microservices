package com.innowise.songapi.repository;

import com.innowise.songapi.model.SongMetadata;
import org.springframework.data.repository.ListCrudRepository;

public interface SongMetadataRepository extends ListCrudRepository<SongMetadata,Integer> {
}
