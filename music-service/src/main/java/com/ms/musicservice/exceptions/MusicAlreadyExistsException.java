package com.ms.musicservice.exceptions;

public class MusicAlreadyExistsException extends RuntimeException {
    public MusicAlreadyExistsException(String message) {
        super(message);
    }
}
