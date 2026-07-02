package com.ms.searchservice.exceptions;

public class MusicNotFoundException extends RuntimeException {
  public MusicNotFoundException(String message) {
    super(message);
  }
}
