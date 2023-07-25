package com.innowise.enricherapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.enricherapi.dto.SongSaveResult;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnricherService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @SqsListener("songfile-queue")
    public void loadMetadataForReceivedSong(String songSaveResultMessage) {
        SongSaveResult songSaveResult = objectMapper.readValue(songSaveResultMessage,SongSaveResult.class);
        log.info("Enricher received message: {}", songSaveResult);

        URI fileApiUri = UriComponentsBuilder.fromUriString("http://localhost:8082/file-api/")
                .path("{songId}")
                .build(songSaveResult.getFileApiId()); //TODO take url from config
        byte[] songFile = restTemplate.getForObject(fileApiUri, byte[].class);


    }

}
