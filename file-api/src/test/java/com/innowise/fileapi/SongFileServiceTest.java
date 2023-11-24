package com.innowise.fileapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.fileapi.dto.SongSaveResult;
import com.innowise.fileapi.model.SongFile;
import com.innowise.fileapi.model.StorageType;
import com.innowise.fileapi.repository.SongFileRepository;
import com.innowise.fileapi.repository.impl.LocalSongStorageRepository;
import com.innowise.fileapi.repository.impl.S3SongStorageRepository;
import com.innowise.fileapi.service.SongFileService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class SongFileServiceTest {

    @Mock
    private SongFileRepository songFileRepo;

    @Mock
    private S3SongStorageRepository s3SongStorageRepository;

    @Mock
    private LocalSongStorageRepository localSongStorageRepository;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SqsTemplate sqsTemplate;


    @InjectMocks
    private SongFileService songFileService;


    @Test
    void uploadFile_S3_Success() throws JsonProcessingException {
        String username = "testUser";
        String queueUrl = "foo";
        ReflectionTestUtils.setField(songFileService, "queueUrl", queueUrl);
        MultipartFile file = new MockMultipartFile("testFile", "test.mp3", "test", "File content".getBytes());
        String token = "testToken";
        SongSaveResult songSaveResult = SongSaveResult.builder().build();

        when(s3SongStorageRepository.save(any(MultipartFile.class))).thenReturn(songSaveResult);
        when(songFileRepo.save(any(SongFile.class))).thenReturn(SongFile.builder().id(1).build());
        when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");
        when(sqsTemplate.send(anyString(),anyString())).thenReturn(null);
        SongSaveResult result = songFileService.uploadFile(username, file, token);

        assertNotNull(result);
        assertEquals(1, result.getFileApiId());
        assertEquals(token, result.getToken());

        verify(sqsTemplate).send(queueUrl, "jsonString");
    }

    @Test
    void uploadFile_Local_Success() throws JsonProcessingException {
        String username = "testUser";
        String queueUrl = "testUrl";
        ReflectionTestUtils.setField(songFileService, "queueUrl", queueUrl);
        MultipartFile file = new MockMultipartFile("testFile", "test.mp3", "test", "File content".getBytes());
        String token = "testToken";
        SongSaveResult songSaveResult = SongSaveResult.builder().build();

        when(s3SongStorageRepository.save(any(MultipartFile.class))).thenThrow(new RuntimeException());
        when(localSongStorageRepository.save(any(MultipartFile.class))).thenReturn(songSaveResult);
        when(songFileRepo.save(any(SongFile.class))).thenReturn(SongFile.builder().id(1).build());
        when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");

        SongSaveResult result = songFileService.uploadFile(username, file, token);

        assertNotNull(result);
        assertEquals(1, result.getFileApiId());
        assertEquals(token, result.getToken());

        verify(sqsTemplate).send(queueUrl, "jsonString");
    }

    @Test
    void downloadFileByUserHash_S3_Success() {
        String username = "user";
        String hashedFilename = "hashedFile";
        byte[] fileContent = "File Content".getBytes();

        when(songFileRepo.findFirstByUsernameAndHashedFilename(username, hashedFilename))
                .thenReturn(Optional.of(SongFile.builder().storageType(StorageType.S3).build()));
        when(s3SongStorageRepository.load(any())).thenReturn(fileContent);

        byte[] result = songFileService.downloadFile(username, hashedFilename);

        assertArrayEquals(fileContent, result);
    }

    @Test
    void downloadFileById_Local_Success() {
        Integer id = 1;
        byte[] fileContent = "File Content".getBytes();

        when(songFileRepo.findById(id))
                .thenReturn(Optional.of(SongFile.builder().storageType(StorageType.LOCAL).build()));
        when(localSongStorageRepository.load(any())).thenReturn(fileContent);

        byte[] result = songFileService.downloadFile(id);

        assertArrayEquals(fileContent, result);
    }

    @Test
    void deleteFile_S3_Success() {
        Integer songId = 1;
        when(songFileRepo.findById(anyInt()))
                .thenReturn(Optional.ofNullable(SongFile.builder().id(songId).storageType(StorageType.S3).build()));

        songFileService.deleteFile(songId);


        verify(songFileRepo).findById(songId);
        verify(s3SongStorageRepository).delete(any());
        verify(songFileRepo).deleteById(songId);
    }
}
