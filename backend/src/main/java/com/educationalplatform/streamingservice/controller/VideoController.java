package com.educationalplatform.streamingservice.controller;

import com.educationalplatform.streamingservice.model.dto.ChunkResponseDTO;
import com.educationalplatform.streamingservice.model.dto.VideoDTO;
import com.educationalplatform.streamingservice.model.dto.VideoInfoDTO;
import com.educationalplatform.streamingservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoController implements VideoApi {

  private final VideoService videoService;

  @Override
  public ResponseEntity<List<VideoDTO>> getAllVideos() {
    return ResponseEntity.ok(videoService.getAllVideos());
  }

  @Override
  public ResponseEntity<VideoInfoDTO> getVideoInfo(Long id) {
    return ResponseEntity.ok(videoService.getVideoInfo(id));
  }

  @Override
  public ResponseEntity<ChunkResponseDTO> getVideoChunk(Long id, Integer chunkIndex) {
    return ResponseEntity.ok(videoService.getEncryptedChunk(id, chunkIndex));
  }

  @Override
  public ResponseEntity<String> getVideoContentType(Long id) {
    return ResponseEntity.ok(videoService.getVideoContentType(id));
  }

  @Override
  public ResponseEntity<Resource> streamVideo(Long id) {
    return videoService.streamVideo(id);
  }

  @Override
  public ResponseEntity<VideoDTO> uploadVideo(MultipartFile file, String title, String description) {
    return ResponseEntity.ok(videoService.uploadNewVideo(file, title, description));
  }
}
