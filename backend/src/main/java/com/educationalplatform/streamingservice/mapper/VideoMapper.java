package com.educationalplatform.streamingservice.mapper;

import org.springframework.stereotype.Component;
import com.educationalplatform.streamingservice.model.dto.VideoDTO;
import com.educationalplatform.streamingservice.model.dto.VideoInfoDTO;
import com.educationalplatform.streamingservice.entity.Video;

@Component
public class VideoMapper {

  public VideoDTO toDTO(Video video) {
    if (video == null) {
      return null;
    }

    return VideoDTO.builder()
        .id(video.getId())
        .title(video.getTitle())
        .formattedDuration(video.getFormattedDuration()) // или formatDuration(video.getDurationSeconds())
        .build();
  }

  public VideoInfoDTO toInfoDTO(Video video) {
    if (video == null) {
      return null;
    }

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