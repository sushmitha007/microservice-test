package com.nokia.mediaservice.service;

import com.nokia.mediaservice.exception.InvalidFileNameException;
import com.nokia.mediaservice.exception.TarFileAlreadyExistsException;
import com.nokia.mediaservice.exception.TarFileNotFoundException;
import com.nokia.mediaservice.model.ResponseTarFile;
import com.nokia.mediaservice.model.TarFile;

import java.util.List;

public interface TarFileService {
    /**
     * AbstractMethod to save a tar file
     */
    ResponseTarFile saveTarFile(TarFile tarFile) throws TarFileAlreadyExistsException, InvalidFileNameException;

    /**
     * AbstractMethod to get a tar file
     */
    TarFile getTarFile(String fileName) throws TarFileNotFoundException;

    /**
     * AbstractMethod to get all tar files
     */
    List<ResponseTarFile> getAllTarFiles();

    /**
     * AbstractMethod to delete all tar files
     */
    void deleteAllTarFiles();

}
