package com.innowise.enricherapi.service;

import com.innowise.enricherapi.exception.ParserException;
import com.innowise.enricherapi.model.Id3SongMetadata;
import com.mpatric.mp3agic.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
public class MetadataParserService {

    public Id3SongMetadata parseMetadataFromSongFile(byte[] songFile) {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile(null, ".mp3");
            Files.write(tempFile, songFile, StandardOpenOption.CREATE);
            Mp3File mp3File = new Mp3File(tempFile);

            return getId3SongMetadataFromMp3(mp3File);

        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            log.error(e.getMessage(),e);
            throw new ParserException("Error while parsing mp3 tags",e);

        } finally {
            if (tempFile != null) {
                try {
                    Files.delete(tempFile);
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private Id3SongMetadata getId3SongMetadataFromMp3(Mp3File mp3File) {
        if (mp3File.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3File.getId3v2Tag();
            return Id3SongMetadata.builder()
                    .name(id3v2Tag.getTitle())
                    .artist(id3v2Tag.getArtist())
                    .album(id3v2Tag.getAlbum())
                    .build();
        } else if (mp3File.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3File.getId3v2Tag();
            return Id3SongMetadata.builder()
                    .name(id3v1Tag.getTitle())
                    .artist(id3v1Tag.getArtist())
                    .album(id3v1Tag.getAlbum())
                    .build();
        } else {
            throw new IllegalArgumentException("Song file doesn't have any relevant metadata");
        }
    }
}
