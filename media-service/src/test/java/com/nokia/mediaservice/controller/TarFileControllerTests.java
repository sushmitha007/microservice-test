package com.nokia.mediaservice.controller;

import com.nokia.mediaservice.exception.TarFileAlreadyExistsException;
import com.nokia.mediaservice.exception.TarFileNotFoundException;
import com.nokia.mediaservice.model.ResponseTarFile;
import com.nokia.mediaservice.model.TarFile;
import com.nokia.mediaservice.service.TarFileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TarFileControllerTests {
    private MockMvc mockMvc;

    @Mock
    private TarFileService tarFileService;

    @InjectMocks
    private TarFileController tarFileController;
    private TarFile tarFile;
    private ResponseTarFile responseTarFile;
    private List<ResponseTarFile> fileList;
    private Optional optional;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tarFileController).build();
        tarFile = new TarFile();
        tarFile.setFileId("file1");
        tarFile.setFileName("demo3.tar.xz");
        fileList = new ArrayList<>();
    }

    @AfterEach
    public void tearDown() {
        tarFileService.deleteAllTarFiles();
        tarFile = null;
    }

    @Test
    void givenTarFileToSaveThenShouldReturnStatusIsCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("tarFile", "demo3.tar.xz", MediaType.MULTIPART_FORM_DATA_VALUE, "demoheii".getBytes());
        when(tarFileService.saveTarFile(any())).thenReturn(responseTarFile);
        mockMvc.perform(
                multipart("/api/v1/tarFile?fileName=demo3")
                        .file(file))
                .andExpect(status().isCreated());
    }

    @Test
    void givenTarFileToSaveThenShouldReturnStatusIsConflict() throws Exception {
        MockMultipartFile file = new MockMultipartFile("tarFile", "demo3.tar.xz", MediaType.MULTIPART_FORM_DATA_VALUE, "demoheii".getBytes());
        when(tarFileService.saveTarFile(any())).thenThrow(TarFileAlreadyExistsException.class);
        mockMvc.perform(
                multipart("/api/v1/tarFile?fileName=demo3")
                        .file(file))
                .andExpect(status().isConflict());
    }

    @Test
    void givenFileNameThenShouldReturnStatusIsNotFound() throws Exception {
        when(tarFileService.getTarFile(any())).thenThrow(TarFileNotFoundException.class);
        mockMvc.perform(
                get("/api/v1/tarFile/demo4.tar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNothingWhenPerformedGetAllThenShouldReturnStatusIsOk() throws Exception {
        when(tarFileService.getAllTarFiles()).thenReturn(fileList);
        mockMvc.perform(
                get("/api/v1/tarFiles"))
                .andExpect(status().isOk());
    }


}
