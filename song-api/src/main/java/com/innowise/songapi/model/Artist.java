package com.innowise.songapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Artist {
    @Id
    private Integer id;

    private String spotifyId;
    private String name;

    private String spotifyUri;
}
