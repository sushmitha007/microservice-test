package com.nokia.mediaservice.exception;

public class TarFileAlreadyExistsException extends Exception {
    public TarFileAlreadyExistsException(String message) {
        super(message);
    }

    public TarFileAlreadyExistsException() {
    }
}
