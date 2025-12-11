package com.educationalplatform.service;

import com.educationalplatform.domain.dto.request.UserUpdateDto;
import com.educationalplatform.domain.model.User;
import com.educationalplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public Optional<User> findById(UUID id) {
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Transactional
  public User updateUser(UUID userId, UserUpdateDto dto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));

    if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
    if (dto.getLastName() != null) user.setLastName(dto.getLastName());
    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
    if (dto.getStatus() != null) user.setStatus(dto.getStatus());
    if (dto.getRole() != null) user.setRole(dto.getRole());
    if (dto.getPaymentDate() != null) user.setPaymentDate(dto.getPaymentDate());

    return userRepository.save(user);
  }
}
