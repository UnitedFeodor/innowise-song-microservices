package com.innowise.songapi.repository;

import com.innowise.songapi.model.Album;
import com.innowise.songapi.model.Artist;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface AlbumRepository extends ListCrudRepository<Album,Integer> {
    Optional<Album> findAlbumByName(String name);
}
