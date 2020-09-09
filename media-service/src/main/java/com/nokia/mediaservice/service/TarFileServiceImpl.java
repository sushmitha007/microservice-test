package com.nokia.mediaservice.service;

import com.nokia.mediaservice.exception.InvalidFileNameException;
import com.nokia.mediaservice.exception.TarFileAlreadyExistsException;
import com.nokia.mediaservice.exception.TarFileNotFoundException;
import com.nokia.mediaservice.model.ResponseTarFile;
import com.nokia.mediaservice.model.TarFile;
import com.nokia.mediaservice.repository.TarFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Service indicates annotated class is a service which
 * holds business logic in the Service layer
 */
@Service
public class TarFileServiceImpl implements TarFileService {
    private TarFileRepo tarFileRepo;
    private Environment environment;

    @Autowired
    public TarFileServiceImpl(TarFileRepo tarFileRepo, Environment environment) {
        this.tarFileRepo = tarFileRepo;
        this.environment = environment;
    }

    @Override
    public ResponseTarFile saveTarFile(TarFile tarFile) throws TarFileAlreadyExistsException, InvalidFileNameException {
        if (tarFile.getFileName().matches("([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.tar|.tar.xz)$")) {
            if (tarFileRepo.findByFileName(tarFile.getFileName()) == null) {
                TarFile savedFile = tarFileRepo.save(tarFile);
                ResponseTarFile responseTarFile = new ResponseTarFile();
                responseTarFile.setFileId(savedFile.getFileId());
                responseTarFile.setFileName(savedFile.getFileName());
                responseTarFile.setFileUri(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/tarFile/")
                        .path(savedFile.getFileName())
                        .toUriString());
                return responseTarFile;
            } else {
                throw new TarFileAlreadyExistsException(environment.getProperty("tarFileAlreadyExists.message"));
            }
        } else {
            throw new InvalidFileNameException(environment.getProperty("invalidFileName.message"));
        }

    }

    @Override
    public TarFile getTarFile(String fileName) throws TarFileNotFoundException {
        TarFile tarFile = tarFileRepo.findByFileName(fileName);
        if (tarFile != null) {
            return tarFile;
        } else {
            throw new TarFileNotFoundException(environment.getProperty("tarFileNotFound.message"));
        }
    }

    @Override
    public List<ResponseTarFile> getAllTarFiles() {
        List<ResponseTarFile> responseTarFiles = new ArrayList<>();
        List<TarFile> tarFiles = tarFileRepo.findAll();
        for (TarFile tarFile : tarFiles) {
            ResponseTarFile responseTarFile = new ResponseTarFile();
            responseTarFile.setFileId(tarFile.getFileId());
            responseTarFile.setFileName(tarFile.getFileName());
            responseTarFile.setFileUri(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/tarFile/")
                    .path(tarFile.getFileName())
                    .toUriString());
            responseTarFiles.add(responseTarFile);
        }
        return responseTarFiles;
    }

    @Override
    public void deleteAllTarFiles() {
        tarFileRepo.deleteAll();
    }

}

