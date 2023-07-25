package com.innowise.enricherapi.service;

import com.innowise.enricherapi.dto.SongSaveResult;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class EnricherService {
    private final RestTemplate restTemplate;
    public EnricherService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @SqsListener("songfile-queue")
    public void loadMetadataForReceivedSong(SongSaveResult songSaveResult) {
        URI fileApiUri = UriComponentsBuilder.fromUriString("http://localhost:8081")
                .path("{songId}")
                .build(songSaveResult.getFileApiId()); //TODO take url from config
        byte[] songFile = restTemplate.getForObject(fileApiUri, byte[].class);


    }

}
