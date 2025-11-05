package com.ms.musicservice.exceptions;

public class MusicNotFoundException extends RuntimeException {
  public MusicNotFoundException(String message) {
    super(message);
  }
}
