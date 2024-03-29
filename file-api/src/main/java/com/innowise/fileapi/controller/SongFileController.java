package com.innowise.fileapi.controller;

import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.service.SongFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class SongFileController {

    private final SongFileService songFileService;

    @PostMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SongSaveResult> uploadFile(
            @PathVariable("username") String username,
            @RequestParam("file") MultipartFile file,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        SongSaveResult songSaveResult = songFileService.uploadFile(username, file,token);
        return new ResponseEntity<>(songSaveResult, HttpStatus.OK);
    }

    @GetMapping("/{username}/{hashedFilename}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable("username") String username,
            @PathVariable("hashedFilename") String hashedFilename) {
        byte[] songFile = songFileService.downloadFile(username, hashedFilename);
        return new ResponseEntity<>(songFile, HttpStatus.OK);
    }

    @GetMapping("/{songId}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable("songId") Integer songId) {
        byte[] songFile = songFileService.downloadFile(songId);
        return new ResponseEntity<>(songFile, HttpStatus.OK);
    }

    @DeleteMapping("/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteFile(@PathVariable("songId") Integer songId) {
        songFileService.deleteFile(songId);
        return ResponseEntity.ok().build();
    }
}
