package com.innowise.enricherapi.route;

import com.innowise.enricherapi.dto.SongSaveResult;
import com.innowise.enricherapi.model.SpotifySongMetadata;
import com.innowise.enricherapi.service.MetadataParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsRouteBuilder extends RouteBuilder {
    private static final String SQS_URI = "aws2-sqs://%s?amazonSQSClient=#sqsClient&autoCreateQueue=true";
    @Override
    public void configure() {
        from(String.format(SQS_URI, "songfile-queue"))
                .convertBodyTo(SongSaveResult.class)
                .log("Enricher received message: {body}")
                .setProperty("fileApiId", simple("${body.fileApiId}"))
                .setHeader(Exchange.HTTP_PATH, simple("/file-api/files/{body.fileApiId}"))
                .to("http://localhost:8082")
                .bean(MetadataParserService.class,"parseMetadataFromSongFile")
//                .setHeader("Authorization", () -> spotifyToken) // TODO add spotify auth
                .setHeader(Exchange.HTTP_QUERY, simple(
                        "q=remaster%2520track:${body.title}%2520artist:${body.artist}%2520album:{body.album}&type=track&limit=1"))
                .to("https://api.spotify.com/v1/search")
                .convertBodyTo(SpotifySongMetadata.class)
                .marshal().json()
                .log("Song metadata found: ${body}")
                .to(String.format(SQS_URI, "songmetadata-queue"));

    }
}
