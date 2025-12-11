package com.educationalplatform.domain.dto.request;

import com.educationalplatform.domain.model.User;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserUpdateDto {
  private String firstName;
  private String lastName;
  private String email;
  private User.Status status;
  private User.Role role;
  private LocalDateTime paymentDate;
}
