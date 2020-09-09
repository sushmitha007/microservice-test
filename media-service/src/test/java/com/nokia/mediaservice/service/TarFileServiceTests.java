package com.nokia.mediaservice.service;

import com.nokia.mediaservice.exception.InvalidFileNameException;
import com.nokia.mediaservice.exception.TarFileAlreadyExistsException;
import com.nokia.mediaservice.exception.TarFileNotFoundException;
import com.nokia.mediaservice.model.ResponseTarFile;
import com.nokia.mediaservice.model.TarFile;
import com.nokia.mediaservice.repository.TarFileRepo;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarFileServiceTests {
    @Mock
    private TarFileRepo tarFileRepo;

    @Mock
    private Environment environment;

    @InjectMocks
    private TarFileServiceImpl tarFileService;
    private TarFile tarFile;
    private List<TarFile> fileList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tarFile = new TarFile();
        tarFile.setFileId("file1");
        tarFile.setFileName("demo3.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Hello World!!".getBytes()));
        fileList.add(tarFile);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @AfterEach
    public void tearDown() {
        tarFileService.deleteAllTarFiles();
        tarFile = null;
    }

    @Test
    void givenTarFileToSaveThenShouldReturnResponseTarFileObject() throws TarFileAlreadyExistsException, InvalidFileNameException {
        when(tarFileRepo.save(any())).thenReturn(tarFile);
        ResponseTarFile savedTarFile = tarFileService.saveTarFile(tarFile);
        assertEquals(tarFile.getFileId(), savedTarFile.getFileId());
        verify(tarFileRepo, times(1)).save(any());
    }

    @Test
    public void givenTarFileToSaveThenShouldThrowException() {
        when(tarFileRepo.findByFileName(tarFile.getFileName())).thenReturn(tarFile);
        Assertions.assertThrows(TarFileAlreadyExistsException.class, () ->
                tarFileService.saveTarFile(tarFile));
        verify(tarFileRepo, times(1)).findByFileName(tarFile.getFileName());
    }

    @Test
    void givenNothingWhenPerformedGetAllThenShouldReturnListOfAllTarFiles() {
        tarFileRepo.save(tarFile);
        //stubbing the mock to return specific data
        when(tarFileRepo.findAll()).thenReturn(fileList);
        List<ResponseTarFile> tarFiles = tarFileService.getAllTarFiles();
        assertEquals(fileList.get(0).getFileId(), tarFiles.get(0).getFileId());
        verify(tarFileRepo, times(1)).save(tarFile);
        verify(tarFileRepo, times(1)).findAll();
    }

    @Test
    public void givenFileNameThenShouldReturnRespectiveTarFile() throws TarFileNotFoundException {
        when(tarFileRepo.findByFileName(anyString())).thenReturn(tarFile);
        TarFile retrievedTarFile = tarFileService.getTarFile(tarFile.getFileName());
        assertEquals(tarFile.getFileId(), retrievedTarFile.getFileId());
        verify(tarFileRepo, times(1)).findByFileName(anyString());

    }

    @Test
    void givenFileNameThenShouldThrowException() {
        when(tarFileRepo.findByFileName(tarFile.getFileName())).thenReturn(null);
        Assertions.assertThrows(TarFileNotFoundException.class, () ->
                tarFileService.getTarFile("demo3.tar.xz"));
        verify(tarFileRepo, times(1)).findByFileName(tarFile.getFileName());
    }

}
