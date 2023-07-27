package com.innowise.enricherapi.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.enricherapi.dto.SpotifyAlbum;
import com.innowise.enricherapi.dto.SpotifyArtist;
import com.innowise.enricherapi.dto.SpotifySongMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.camel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Converter(generateLoader = true)
@Component
@RequiredArgsConstructor
public class SpotifyJsonTrackConverter implements TypeConverter {

    private final ObjectMapper objectMapper;

    @Override
    public boolean allowNull() {
        return false;
    }

    @Override
    public <T> T convertTo(Class<T> type, Object value) throws TypeConversionException {
        try {
            JsonNode root = objectMapper.readTree((InputStream) value);
            JsonNode trackNode = root.at("/tracks/items/0");

            SpotifySongMetadata songMetadata = SpotifySongMetadata.builder()
                    .spotifyId(trackNode.get("id").asText())
                    .name(trackNode.get("name").asText())
                    .album(parseAlbum(trackNode))
                    .artists(parseArtists(trackNode))
                    .discNumber(trackNode.get("disc_number").asInt())
                    .trackNumber(trackNode.get("track_number").asInt())
                    .spotifyUri(trackNode.get("uri").asText())
                    .durationMs(trackNode.get("duration_ms").asInt())
                    .build();

            return (T) songMetadata;
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse json tree",e);
        }
    }

    private SpotifyAlbum parseAlbum(JsonNode trackNode) {

        JsonNode albumNode = trackNode.at("/album");

        SpotifyAlbum album = SpotifyAlbum.builder()
                .spotifyId(albumNode.get("id").asText())
                .name(albumNode.get("name").asText())
                .spotifyUri(albumNode.get("uri").asText())
                .releaseDate(albumNode.get("release_date").asText())
                .genres(albumNode.findValuesAsText("genres"))
                .build();

        return album;
    }

    private static List<SpotifyArtist> parseArtists(JsonNode trackNode) {

        ArrayList<SpotifyArtist> artistList = new ArrayList<>();
        for (JsonNode artistNode : trackNode.get("artists")) {
            SpotifyArtist artist = SpotifyArtist.builder()
                    .spotifyId(artistNode.get("id").asText())
                    .name(artistNode.get("name").asText())
                    .spotifyUri(artistNode.get("uri").asText())
                    .build();

            artistList.add(artist);
        }

        return artistList;
    }
    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        SpotifySongMetadata songMetadata = (SpotifySongMetadata) convertTo(type, value);
        songMetadata.setId((Integer) exchange.getProperty("fileApiId"));
        return (T) songMetadata;
    }

    @Override
    public <T> T mandatoryConvertTo(Class<T> type, Object value) throws TypeConversionException {
        return convertTo(type, value);
    }

    @Override
    public <T> T mandatoryConvertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        return convertTo(type, exchange, value);
    }

    @Override
    public <T> T tryConvertTo(Class<T> type, Object value) {
        return convertTo(type, value);
    }

    @Override
    public <T> T tryConvertTo(Class<T> type, Exchange exchange, Object value) {
        return convertTo(type, exchange, value);
    }
}
