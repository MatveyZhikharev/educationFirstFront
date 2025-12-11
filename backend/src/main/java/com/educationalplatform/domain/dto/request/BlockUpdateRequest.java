package com.educationalplatform.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlockUpdateRequest {
  @NotNull
  private Long blockId;

  @NotBlank
  @Size(min = 3, max = 50)
  private String title;
}

