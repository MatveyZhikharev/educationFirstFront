package com.educationalplatform.controllers.impl;

import com.educationalplatform.controllers.UserOperations;
import com.educationalplatform.domain.dto.request.UserUpdateDto;
import com.educationalplatform.domain.model.User;
import com.educationalplatform.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserOperations {

  private final UserService userService;

  @Override
  public ResponseEntity<Page<User>> getUsers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<User> users = userService.getAllUsers(pageable);
    return ResponseEntity.ok(users);
  }

  @Override
  public ResponseEntity<User> updateUser(UUID userId, UserUpdateDto dto) {
    User updatedUser = userService.updateUser(userId, dto);
    return ResponseEntity.ok(updatedUser);
  }
}
