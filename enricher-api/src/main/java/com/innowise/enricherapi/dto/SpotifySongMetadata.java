package com.innowise.enricherapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpotifySongMetadata {

    private Integer id;

    private String spotifyId;
    private String name;
    private SpotifyAlbum album;
    private List<SpotifyArtist> artists;

    private int trackNumber;
    private int discNumber;

    private String spotifyUri;
    private int durationMs;
    private boolean explicit;


}
