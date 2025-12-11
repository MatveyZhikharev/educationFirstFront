package com.educationalplatform.streamingservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.educationalplatform.streamingservice.entity.Video;
import com.educationalplatform.streamingservice.model.enums.VideoFormat;
import com.educationalplatform.streamingservice.model.enums.VideoStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDTO {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("title")
  private String title;

  @JsonProperty("description")
  private String description;

  @JsonProperty("fileSize")
  private Long fileSize;

  @JsonProperty("formattedFileSize")
  private String formattedFileSize;

  @JsonProperty("durationSeconds")
  private Integer durationSeconds;

  @JsonProperty("formattedDuration")
  private String formattedDuration;

  @JsonProperty("format")
  private VideoFormat format;

  @JsonProperty("status")
  private VideoStatus status;

  @JsonProperty("mimeType")
  private String mimeType;

  @JsonProperty("totalChunks")
  private Integer totalChunks;

  @JsonProperty("chunkSize")
  private Integer chunkSize;

  @JsonProperty("createdAt")
  private LocalDateTime createdAt;

  @JsonProperty("updatedAt")
  private LocalDateTime updatedAt;

  public static VideoDTO fromEntity(final Video video) {
    return VideoDTO.builder()
        .id(video.getId())
        .title(video.getTitle())
        .description(video.getDescription())
        .fileSize(video.getFileSize())
        .formattedFileSize(video.getFormattedFileSize())
        .durationSeconds(video.getDurationSeconds())
        .formattedDuration(video.getFormattedDuration())
        .format(video.getFormat())
        .status(video.getStatus())
        .mimeType(video.getMimeType())
        .totalChunks(video.getTotalChunks())
        .chunkSize(video.getChunkSize())
        .createdAt(video.getCreatedAt())
        .updatedAt(video.getUpdatedAt())
        .build();
  }
}