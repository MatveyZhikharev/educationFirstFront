package com.educationalplatform.streamingservice.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum VideoStatus {
  PENDING("Ожидает обработки"),
  PROCESSING("Обрабатывается"),
  READY("Готово к просмотру"),
  ERROR("Ошибка обработки"),
  DELETED("Удалено");

  private final String description;

  VideoStatus(final String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static Optional<VideoStatus> fromString(final String status) {
    if (status == null || status.isBlank()) {
      return Optional.empty();
    }

    return Arrays.stream(VideoStatus.values())
        .filter(s -> s.name().equalsIgnoreCase(status))
        .findFirst();
  }

  public boolean isAvailableForStreaming() {
    return this == READY;
  }

  public boolean canBeModified() {
    return this == PENDING || this == ERROR;
  }

  public boolean isFinalState() {
    return this == READY || this == ERROR || this == DELETED;
  }

  @Override
  public String toString() {
    return String.format("%s (%s)", name(), description);
  }
}