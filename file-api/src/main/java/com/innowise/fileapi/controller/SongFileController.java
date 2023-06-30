package com.innowise.fileapi.controller;

import com.innowise.fileapi.service.SongFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class SongFileController {

    private final SongFileService songFileService;

    @PostMapping("/${username}")
    public ResponseEntity<String> uploadFile(
            @PathVariable("username") String username,
            @RequestParam("file") MultipartFile file) {
        String storageType = songFileService.uploadFile(username, file);
        return new ResponseEntity<>(storageType, HttpStatus.OK);
    }
}
