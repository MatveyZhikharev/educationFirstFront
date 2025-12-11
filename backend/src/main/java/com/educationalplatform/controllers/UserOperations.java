package com.educationalplatform.controllers;

import com.educationalplatform.domain.dto.request.UserUpdateDto;
import com.educationalplatform.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User Management", description = "API для админов по управлению пользователями")
@RequestMapping("/api/admin/users")
public interface UserOperations {
  @GetMapping
  @Operation(summary = "Получить пользователей с пагинацией")
  ResponseEntity<Page<User>> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size);

  @PatchMapping("/{userId}")
  @Operation(summary = "Редактировать пользователя (несколько или одно поле)")
  ResponseEntity<User> updateUser(
      @PathVariable UUID userId,
      @RequestBody UserUpdateDto dto);

}
