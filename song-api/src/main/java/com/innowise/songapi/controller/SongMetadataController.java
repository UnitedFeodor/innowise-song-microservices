package com.innowise.songapi.controller;

import com.innowise.songapi.dto.SpotifySongMetadataDto;
import com.innowise.songapi.mapper.SongMetadataMapper;
import com.innowise.songapi.model.SongMetadata;
import com.innowise.songapi.service.SongMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongMetadataController {

    private final SongMetadataMapper songMetadataMapper;
    private final SongMetadataService songMetadataService;

    @GetMapping
    public ResponseEntity<List<SpotifySongMetadataDto>> getAll() {
        List<SongMetadata> songMetadataList = songMetadataService.getAll();
        List<SpotifySongMetadataDto> songMetadataDtoList = songMetadataMapper.mapEntityListToDtoList(songMetadataList);
        return ResponseEntity.ok(songMetadataDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpotifySongMetadataDto> get(@PathVariable("id") Integer id) {
        SongMetadata songMetadata = songMetadataService.get(id);
        SpotifySongMetadataDto songMetadataDto = songMetadataMapper.mapEntityToDto(songMetadata);
        return ResponseEntity.ok(songMetadataDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        songMetadataService.delete(id, token);
        return ResponseEntity.ok().build();
    }


}
