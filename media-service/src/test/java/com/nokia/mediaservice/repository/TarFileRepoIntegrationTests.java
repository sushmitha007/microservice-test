package com.nokia.mediaservice.repository;

import com.nokia.mediaservice.model.TarFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TarFileRepoIntegrationTests {
    @Autowired
    private TarFileRepo tarFileRepo;
    private TarFile tarFile;

    @BeforeEach
    void setUp() {
        tarFile = new TarFile();
        tarFile.setFileId("file1");
        tarFile.setFileName("demo3.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Hello World!!".getBytes()));
    }


    @AfterEach
    void tearDown() {
        this.tarFileRepo.deleteAll();
        tarFile = null;
    }

    @Test
    public void givenTarFileToSaveThenShouldReturnSavedFile() {
        tarFileRepo.save(tarFile);
        TarFile savedFile = tarFileRepo.findById(tarFile.getFileId()).get();
        assertEquals("file1", savedFile.getFileId());
    }

    @Test
    public void givenTarFilesThenShouldReturnListOfAllTarFiles() {
        TarFile tarFileOne = new TarFile();
        tarFileOne.setFileId("file2");
        tarFileOne.setFileName("demo2.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Welcome to Nokia".getBytes()));
        tarFileRepo.save(tarFile);
        tarFileRepo.save(tarFileOne);
        List<TarFile> tarFileList = tarFileRepo.findAll();
        assertEquals("file2", tarFileList.get(1).getFileId());
    }

    @Test
    public void givenFileNameThenShouldReturnRespectiveTarFile() {
        tarFileRepo.save(tarFile);
        TarFile retrievedTarFile = tarFileRepo.findByFileName(tarFile.getFileName());
        assertEquals(tarFile.getFileId(), retrievedTarFile.getFileId());
    }

}
