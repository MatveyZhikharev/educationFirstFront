package com.educationalplatform.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlockAddRequest {
  @NotBlank(message = "Title cannot be empty")
  @Size(min = 3, max = 50)
  private String title;
}
