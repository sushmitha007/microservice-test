package com.nokia.mediaservice.controller;

import com.nokia.mediaservice.exception.InvalidFileNameException;
import com.nokia.mediaservice.exception.TarFileAlreadyExistsException;
import com.nokia.mediaservice.exception.TarFileNotFoundException;
import com.nokia.mediaservice.model.TarFile;
import com.nokia.mediaservice.service.TarFileService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * RestController annotation is used to create
 * Restful web services using Spring MVC
 */
@RestController
@RequestMapping("api/v1/")
public class TarFileController {
    private TarFileService tarFileService;

    @Autowired
    public TarFileController(TarFileService tarFileService) {
        this.tarFileService = tarFileService;
    }

    /**
     * PostMapping Annotation for mapping HTTP POST requests onto
     * specific handler methods.
     */
    @PostMapping("tarFile")
    public ResponseEntity<?> saveTarFile(@RequestParam("tarFile") MultipartFile tarFile) {
        try {
            TarFile tarFileOne = new TarFile();
            tarFileOne.setFile(new Binary(BsonBinarySubType.BINARY, tarFile.getBytes()));
            String fileName = StringUtils.cleanPath(tarFile.getOriginalFilename());
            tarFileOne.setFileName(fileName);
            return new ResponseEntity<>(tarFileService.saveTarFile(tarFileOne), HttpStatus.CREATED);
        } catch (TarFileAlreadyExistsException tarFileAlreadyExistsException) {
            return new ResponseEntity<>(tarFileAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        } catch (InvalidFileNameException invalidFileNameException) {
            return new ResponseEntity<>(invalidFileNameException.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GetMapping Annotation for mapping HTTP GET requests
     * onto specific handler methods.
     */
    @GetMapping("tarFile/{fileName:.+}")
    public ResponseEntity<?> getTarFile(@PathVariable String fileName) {
        try {
            TarFile tarFile = tarFileService.getTarFile(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/tar+gzip"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tarFile.getFileName() + "\"")
                    .body(new ByteArrayResource(tarFile.getFile().getData()));
        } catch (TarFileNotFoundException tarFileNotFoundException) {
            return new ResponseEntity<>(tarFileNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("tarFiles")
    public ResponseEntity<?> getAllTarFiles() {
        try {
            return new ResponseEntity<>(tarFileService.getAllTarFiles(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GetMapping Annotation for mapping HTTP DELETE requests
     * onto specific handler methods.
     */
    @DeleteMapping("tarFiles")
    public ResponseEntity<?> deleteAllTarFiles() {
        try {
            tarFileService.deleteAllTarFiles();
            return new ResponseEntity<>("Deleted all the tar files from the database", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
