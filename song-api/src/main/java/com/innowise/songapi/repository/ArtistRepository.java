package com.innowise.songapi.repository;

import com.innowise.songapi.model.Artist;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ArtistRepository extends ListCrudRepository<Artist,Integer> {
    Optional<Artist> findArtistByName(String name);
}
