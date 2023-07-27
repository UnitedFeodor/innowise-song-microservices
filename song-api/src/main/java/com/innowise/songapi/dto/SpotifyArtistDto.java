package com.innowise.songapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotifyArtistDto {

    private String spotifyId;
    private String name;

    private String spotifyUri;
}
