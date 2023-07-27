package com.innowise.songapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpotifySongMetadataDto {

    private Integer id;

    private String spotifyId;
    private String name;
    private SpotifyAlbumDto album;
    private List<SpotifyArtistDto> artists;

    private int trackNumber;
    private int discNumber;

    private String spotifyUri;
    private int durationMs;
    private boolean explicit;


}
