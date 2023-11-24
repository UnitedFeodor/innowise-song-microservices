package com.innowise.fileapi;

import com.innowise.fileapi.controller.SongFileController;
import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.service.SongFileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongFileControllerTest {


    @Mock
    SongFileService songFileService;

    @InjectMocks
    SongFileController songFileController;

    @Test
    void uploadFile_Successful_ReturnsOk() {
        String username = "testUser";
        MultipartFile file = new MockMultipartFile("testFile", "test.mp3", "test", "File content".getBytes());
        String token = "testToken";
        SongSaveResult songSaveResult = SongSaveResult.builder().build();

        when(songFileService.uploadFile(anyString(),any(MultipartFile.class),anyString())).thenReturn(songSaveResult);

        ResponseEntity<SongSaveResult> response = songFileController.uploadFile(username,file,token);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(songSaveResult, response.getBody());
        verify(songFileService).uploadFile(username,file,token);
    }

    @Test
    void downloadFileByUserHash_Successful_ReturnsOk() {
        String username = "user";
        String hashedFilename = "hashedFile";
        byte[] fileContent = "File Content".getBytes();

        when(songFileService.downloadFile(username, hashedFilename)).thenReturn(fileContent);

        ResponseEntity<byte[]> response = songFileController.downloadFile(username, hashedFilename);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(fileContent, response.getBody());
        verify(songFileService).downloadFile(username, hashedFilename);
    }

    @Test
    void downloadFileById_Successful_ReturnsOk() {
        Integer id = 1;

        byte[] fileContent = "File Content".getBytes();

        when(songFileService.downloadFile(id)).thenReturn(fileContent);

        ResponseEntity<byte[]> response = songFileController.downloadFile(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(fileContent, response.getBody());
        verify(songFileService).downloadFile(id);
    }


    @Test
    void deleteFile_Successful_ReturnsOk() {
        Integer songId = 1;

        ResponseEntity<Object> response = songFileController.deleteFile(songId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(songFileService).deleteFile(songId);
    }



}
