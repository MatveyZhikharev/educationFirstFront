package com.educationalplatform.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleResourceBadRequest(BadRequestException ex, HttpServletRequest request) {
    log.warn("Bad request: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(VkAuthException.class)
  public ResponseEntity<Object> handleResourceVkAuth(BadRequestException ex, HttpServletRequest request) {
    log.warn("Bad vk auth: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
  }

  @ExceptionHandler(BlockNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFound(BlockNotFoundException ex, HttpServletRequest request) {
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
    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Uncorrected data", request.getRequestURI());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
    log.warn("Malformed JSON request: {}", ex.getMessage());
    return buildResponseEntity(HttpStatus.BAD_REQUEST, "Некорректный формат запроса", request.getRequestURI());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Object> handleNoResourceFoundException(Exception ex, HttpServletRequest request) {
    log.error("Resource not found", ex);
    return buildResponseEntity(HttpStatus.NOT_FOUND, "Страница не найдена", request.getRequestURI());
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
