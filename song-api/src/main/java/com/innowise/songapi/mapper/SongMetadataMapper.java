package com.innowise.songapi.mapper;

import com.innowise.songapi.dto.SpotifySongMetadataDto;
import com.innowise.songapi.model.SongMetadata;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SongMetadataMapper {
    List<SpotifySongMetadataDto> mapEntityListToDtoList(List<SongMetadata> songMetadataList);

    SpotifySongMetadataDto mapEntityToDto(SongMetadata songMetadata);
}
