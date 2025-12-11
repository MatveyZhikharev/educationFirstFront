package com.educationalplatform.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  public enum Status {
    REGISTERED,
    ACTIVE,
    BLOCKED
  }

  public enum Role {
    USER,
    ADMIN
  }

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "vk_id")
  private Long vkId;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Status status = Status.REGISTERED;

  @Column(name = "registration_date", nullable = false)
  @Builder.Default
  private LocalDateTime registrationDate = LocalDateTime.now();

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Role role = Role.USER;
}
