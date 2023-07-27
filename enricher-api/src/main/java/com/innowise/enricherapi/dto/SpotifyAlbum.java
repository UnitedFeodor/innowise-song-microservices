package com.innowise.enricherapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpotifyAlbum {

    private String spotifyId;
    private String name;

    private String releaseDate;
    private String spotifyUri;

    private List<String> genres;



}
