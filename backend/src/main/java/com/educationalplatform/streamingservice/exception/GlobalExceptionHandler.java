package com.educationalplatform.streamingservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
    log.warn("Resource not found: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
    log.warn("Invalid argument: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<Object> handleValidationExceptions(Exception ex, HttpServletRequest request) {
    log.warn("Validation failed: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Некорректные входные данные", request.getRequestURI());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
    log.warn("Malformed JSON request: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Некорректный формат запроса", request.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllUncaughtException(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception occurred", ex);
    return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request.getRequestURI());
  }

  private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, String path) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("path", path);

    return new ResponseEntity<>(body, new HttpHeaders(), status);
  }
}