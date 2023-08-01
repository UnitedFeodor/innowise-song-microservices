package com.innowise.songapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpotifyAlbumDto {

    private String spotifyId;
    private String name;

    private String releaseDate;
    private String spotifyUri;

    private List<String> genres;



}
