package com.innowise.songapi.service;

import com.innowise.songapi.model.Album;
import com.innowise.songapi.model.Artist;
import com.innowise.songapi.model.SongMetadata;
import com.innowise.songapi.repository.AlbumRepository;
import com.innowise.songapi.repository.ArtistRepository;
import com.innowise.songapi.repository.SongMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongMetadataService {

    private final SongMetadataRepository songMetadataRepo;
    private final AlbumRepository albumRepo;
    private final ArtistRepository artistRepo;

    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Value("${api-gateway.uri}")
    private String apiGatewayUri;
    @Transactional
    public void save(SongMetadata songMetadata) {
        saveAlbumWithoutDuplicates(songMetadata.getAlbum());
        saveArtistsWithoutDuplicates(songMetadata.getArtists());

        songMetadataRepo.save(songMetadata);
        log.info("Saved song metadata with id {}", songMetadata.getId());
    }

    private void saveAlbumWithoutDuplicates(Album album) {
        Optional<Album> optionalAlbum = albumRepo.findAlbumByName(album.getName());

        optionalAlbum.ifPresentOrElse(
                value -> album.setId(value.getId()),
                () -> albumRepo.save(album)
        );
    }

    private void saveArtistsWithoutDuplicates(List<Artist> artistList) {
        for (Artist artist : artistList) {
            Optional<Artist> optionalArtist = artistRepo.findArtistByName(artist.getName());

            optionalArtist.ifPresentOrElse(
                    value -> artist.setId(value.getId()),
                    () -> artistRepo.save(artist)
            );
        }
    }

    @Transactional
    public List<SongMetadata> getAll() {
        List<SongMetadata> songMetadataList = songMetadataRepo.findAll();

        log.info("Acquired song metadata list of {} songs",songMetadataList.size());
        return songMetadataList;

    }

    @Transactional
    public SongMetadata get(Integer id) {
        SongMetadata songMetadata = songMetadataRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException(String.format("Song with id %d doesn't exist",id))
        );

        log.info("Acquired song metadata with id {}",songMetadata.getId());
        return songMetadata;
    }

    @Transactional
    public void delete(Integer songId, String token) {
        SongMetadata songMetadata = songMetadataRepo.findById(songId).orElseThrow(
                () -> new IllegalArgumentException(String.format("Song with id %d doesn't exist",songId))
        );

        String fileApiDeleteUri = String.format("%s/file-api/files/%d", apiGatewayUri, songId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("file-api-delete");
        circuitBreaker.run(() ->
                restTemplate.exchange(fileApiDeleteUri, HttpMethod.DELETE, httpEntity, Object.class)
        );

        songMetadataRepo.deleteById(songMetadata.getId());
        log.info("Deleted song metadata with id {}", songId);
    }
}
