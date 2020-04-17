package io.digital.supercharger.exception;

public class FileManagementException extends RuntimeException {

    public FileManagementException(Exception e) {
        super(e);
    }

    public FileManagementException(String message) {
        super(message);
    }
}
