package com.innowise.enricherapi.service;

import com.innowise.contractapi.dto.SongSaveResult;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
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
    private final UriComponentsBuilder uriComponentsBuilder;
    @SqsListener("songfile-queue")
    public void loadMetadataForRecievedSong(SongSaveResult songSaveResult) {
        URI fileApiUri = uriComponentsBuilder
                .scheme("http")
                .host("localhost:8081")
                .path("{songId}")
                .build(songSaveResult.getFileApiId()); //TODO take url from config
        byte[] songFile = restTemplate.getForObject(fileApiUri, byte[].class);


    }

}
