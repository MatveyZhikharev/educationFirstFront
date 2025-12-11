package com.educationalplatform.repository;

import com.educationalplatform.domain.model.VkAuthState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VkAuthStateRepository extends JpaRepository<VkAuthState, String> {
  Optional<VkAuthState> findByState(String state);
  void deleteByState(String state);
}
