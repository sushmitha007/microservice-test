package com.nokia.mediaservice.controller;

import com.nokia.mediaservice.exception.InvalidFileNameException;
import com.nokia.mediaservice.exception.TarFileAlreadyExistsException;
import com.nokia.mediaservice.exception.TarFileNotFoundException;
import com.nokia.mediaservice.model.ResponseTarFile;
import com.nokia.mediaservice.model.TarFile;
import com.nokia.mediaservice.service.TarFileService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TarFileControllerIntegrationTests {

    @Autowired
    private TarFileService tarFileService;
    private TarFile tarFile;
    private List<TarFile> fileList;

    @BeforeEach
    void setUp() throws IOException {
        tarFile = new TarFile();
        tarFile.setFileId("file1");
        tarFile.setFileName("demo3.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Hello World!!".getBytes()));
        fileList = new ArrayList<>();
        fileList.add(tarFile);
    }

    @AfterEach
    void tearDown() {
        tarFile = null;
        tarFileService.deleteAllTarFiles();
    }

    @Test
    void givenTarFileToSaveThenShouldReturnTheSavedTarFile() throws TarFileAlreadyExistsException, InvalidFileNameException {
        ResponseTarFile savedTarFile = tarFileService.saveTarFile(tarFile);
        assertNotNull(savedTarFile);
        assertEquals(tarFile.getFileId(), savedTarFile.getFileId());
    }

    @Test
    void givenTarFileToSaveThenThrowException() throws TarFileAlreadyExistsException, InvalidFileNameException {
        assertNotNull(tarFileService.saveTarFile(tarFile));
        assertThrows(TarFileAlreadyExistsException.class, () -> tarFileService.saveTarFile(tarFile));
    }

    @Test
    void givenNothingWhenPerformedGetAllThenShouldReturnListOfAllTarFiles() throws TarFileAlreadyExistsException, InvalidFileNameException {
        tarFileService.saveTarFile(tarFile);
        assertNotNull(tarFileService.getAllTarFiles());
    }

    @Test
    public void givenFileNameThenShouldReturnRespectiveTarFile() throws TarFileNotFoundException, TarFileAlreadyExistsException, InvalidFileNameException {
        tarFileService.saveTarFile(tarFile);
        TarFile retrievedTarFile = tarFileService.getTarFile(tarFile.getFileName());
        assertEquals(tarFile.getFileId(), retrievedTarFile.getFileId());
    }

    @Test
    void givenFileNameThenShouldThrowException() {
        Assertions.assertThrows(TarFileNotFoundException.class, () ->
                tarFileService.getTarFile("demo3.tar.xz"));
    }
}
