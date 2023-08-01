package com.innowise.enricherapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotifyArtist {

    private String spotifyId;
    private String name;

    private String spotifyUri;
}
