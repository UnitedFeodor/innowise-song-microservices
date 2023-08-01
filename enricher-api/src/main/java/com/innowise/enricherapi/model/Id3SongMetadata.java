package com.innowise.enricherapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Id3SongMetadata {
    private String name;

    private String album;

    private String artist;
}
