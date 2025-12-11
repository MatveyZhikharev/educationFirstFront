package com.educationalplatform.streamingservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.educationalplatform.streamingservice.entity.Video;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoInfoDTO {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("title")
  private String title;

  @JsonProperty("description")
  private String description;

  @JsonProperty("formattedFileSize")
  private String formattedFileSize;

  @JsonProperty("formattedDuration")
  private String formattedDuration;

  @JsonProperty("mimeType")
  private String mimeType;

  @JsonProperty("totalChunks")
  private Integer totalChunks;

  @JsonProperty("isReady")
  private Boolean isReady;

  public static VideoInfoDTO fromEntity(final Video video) {
    return VideoInfoDTO.builder()
        .id(video.getId())
        .title(video.getTitle())
        .description(video.getDescription())
        .formattedFileSize(video.getFormattedFileSize())
        .formattedDuration(video.getFormattedDuration())
        .mimeType(video.getMimeType())
        .totalChunks(video.getTotalChunks())
        .isReady(video.isReadyForStreaming())
        .build();
  }
}