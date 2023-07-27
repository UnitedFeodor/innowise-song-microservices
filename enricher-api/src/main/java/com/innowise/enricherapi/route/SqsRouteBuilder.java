package com.innowise.enricherapi.route;

import com.innowise.enricherapi.dto.SongSaveResult;
import com.innowise.enricherapi.dto.SpotifySongMetadata;
import com.innowise.enricherapi.service.MetadataParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsRouteBuilder extends RouteBuilder {
    private static final String SQS_URI = "aws2-sqs://%s?amazonSQSClient=#sqsClient&autoCreateQueue=true";
    private static final String API_GATEWAY_URI = "http://localhost:8082";
    private static final String SPOTIFY_SEARCH_URI = "https://api.spotify.com/v1/search";

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String clientSecret;
    private String spotifyToken;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    public void configure() {
        from(String.format(SQS_URI, "songfile-queue"))
                .convertBodyTo(SongSaveResult.class)
                .log("Enricher received message: ${body}")
                .setProperty("fileApiId", simple("${body.fileApiId}"))
                .setHeader(Exchange.HTTP_PATH, simple("/file-api/files/${body.fileApiId}"))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .to(API_GATEWAY_URI)
                .removeHeader(Exchange.HTTP_PATH)
                .convertBodyTo(byte[].class)
                .bean(MetadataParserService.class,"parseMetadataFromSongFile")
                .setHeader("Authorization", () -> spotifyToken)
                .setHeader(Exchange.HTTP_QUERY, simple(
                        "q=remaster%2520track:${body.name}%2520artist:${body.artist}%2520album:${body.album}&type=track&limit=1"))
                .to(SPOTIFY_SEARCH_URI)
                .convertBodyTo(SpotifySongMetadata.class)
                .marshal().json()
                .log("Song metadata found: ${body}")
                .to(String.format(SQS_URI, "songmetadata-queue"));

    }

    @Scheduled(initialDelay = 0, fixedRate = 3600, timeUnit = TimeUnit.SECONDS)
    private void getSpotifyToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("spotify")
                .principal(clientSecret)
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager
                .authorize(authorizeRequest);

        String token = Objects.requireNonNull(authorizedClient)
                .getAccessToken()
                .getTokenValue();

        spotifyToken = String.format("Bearer %s", token);
        log.info("Spotify token updated {}", token);
    }

}
