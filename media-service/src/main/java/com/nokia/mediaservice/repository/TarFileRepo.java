package com.nokia.mediaservice.repository;

import com.nokia.mediaservice.model.TarFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Repository marks the specific class as a Data Access Object
 */
@Repository
public interface TarFileRepo extends MongoRepository<TarFile, String> {
    /*
    This methods executes the query to retrieve the tar file for the specified fileName
     */
    TarFile findByFileName(String Filename);

}
