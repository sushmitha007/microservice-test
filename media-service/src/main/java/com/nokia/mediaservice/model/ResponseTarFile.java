package com.nokia.mediaservice.model;

import org.springframework.stereotype.Component;

@Component
public class ResponseTarFile {
    private String fileId;
    private String fileName;
    private String fileUri;

    public ResponseTarFile() {
    }

    public ResponseTarFile(String fileId, String fileName, String fileUri) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileUri = fileUri;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }
}
