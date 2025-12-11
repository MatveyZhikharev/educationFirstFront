package com.educationalplatform.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class VkAuthException extends RuntimeException {

  public VkAuthException(String message) {
    super(message);
  }
}
