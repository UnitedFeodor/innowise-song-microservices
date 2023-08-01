package com.innowise.songapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SongMetadata {

    @Id
    private Integer id;

    private String spotifyId;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Album album;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Artist> artists;

    private int trackNumber;
    private int discNumber;

    private String spotifyUri;
    private int durationMs;
    private boolean explicit;


}
