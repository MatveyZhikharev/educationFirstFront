package com.educationalplatform.streamingservice.service;

import com.educationalplatform.streamingservice.entity.Video;
import com.educationalplatform.streamingservice.exception.ResourceNotFoundException;
import com.educationalplatform.streamingservice.mapper.VideoMapper;
import com.educationalplatform.streamingservice.model.dto.ChunkResponseDTO;
import com.educationalplatform.streamingservice.model.dto.VideoDTO;
import com.educationalplatform.streamingservice.model.dto.VideoInfoDTO;
import com.educationalplatform.streamingservice.model.enums.VideoFormat;
import com.educationalplatform.streamingservice.model.enums.VideoStatus;
import com.educationalplatform.streamingservice.repository.VideoRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoService {

  private final VideoRepository videoRepository;
  private final VideoMapper videoMapper;
  private final VideoEncryptionService encryptionService;
  private final MinioStorageService minioStorageService;
  private final MinioClient minioClient;

  @Value("${minio.bucket-name}")
  private String bucketName;

  private static final int DEFAULT_CHUNK_SIZE = 1024 * 1024;

  public List<VideoDTO> getAllVideos() {
    return videoRepository.findAll()
        .stream()
        .map(videoMapper::toDTO)
        .collect(Collectors.toList());
  }

  public VideoInfoDTO getVideoInfo(Long id) {
    Video video = findVideoById(id);
    return videoMapper.toInfoDTO(video);
  }

  public ChunkResponseDTO getEncryptedChunk(Long id, Integer chunkIndex) {
    Video video = findVideoById(id);

    if (chunkIndex < 0 || chunkIndex >= video.getTotalChunks()) {
      throw new ResourceNotFoundException("Индекс чанка " + chunkIndex + " не найден");
    }

    try {
      long chunkSize = video.getChunkSize();
      long offset = (long) chunkIndex * chunkSize;
      long length = Math.min(chunkSize, video.getFileSize() - offset);

      try (InputStream stream = minioClient.getObject(
          GetObjectArgs.builder()
              .bucket(bucketName)
              .object(video.getFilePath())
              .offset(offset)
              .length(length)
              .build())) {

        byte[] chunkData = stream.readAllBytes();
        byte[] encryptedData = encryptionService.encrypt(chunkData, video.getEncryptionKey());

        return ChunkResponseDTO.builder()
            .chunkIndex(chunkIndex)
            .encryptedData(encryptedData)
            .build();
      }
    } catch (Exception e) {
      log.error("Не удалось получить/зашифровать чанк {} для видео {}", chunkIndex, id, e);
      throw new RuntimeException("Ошибка обработки видео чанка", e);
    }
  }

  public String getVideoContentType(Long id) {
    Video video = findVideoById(id);
    return video.getMimeType();
  }

  public ResponseEntity<Resource> streamVideo(Long id) {
    Video video = findVideoById(id);
    InputStream videoStream = minioStorageService.getVideoInputStream(video.getFilePath());

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(video.getMimeType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + video.getTitle() + "\"")
        .body(new InputStreamResource(videoStream));
  }

  @Transactional
  public VideoDTO uploadNewVideo(MultipartFile file, String title, String description) {
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    String minioFileName = "videos/" + UUID.randomUUID() + extension;

    minioStorageService.uploadVideo(file, minioFileName);

    VideoFormat format = VideoFormat.MP4;
    if (file.getContentType() != null) {
      if (file.getContentType().contains("webm")) format = VideoFormat.WEBM;
      else if (file.getContentType().contains("avi")) format = VideoFormat.AVI;
    }

    long fileSize = file.getSize();
    int totalChunks = (int) Math.ceil((double) fileSize / DEFAULT_CHUNK_SIZE);

    Video video = Video.builder()
        .title(title)
        .description(description)
        .filePath(minioFileName)
        .fileSize(fileSize)
        .mimeType(file.getContentType() != null ? file.getContentType() : "video/mp4")
        .format(format)
        .status(VideoStatus.READY)
        .chunkSize(DEFAULT_CHUNK_SIZE)
        .totalChunks(totalChunks)
        .encryptionKey(UUID.randomUUID().toString())
        .durationSeconds(0)
        .build();

    video = videoRepository.save(video);
    log.info("Видео сохранено в БД: id={}, title={}", video.getId(), video.getTitle());

    return videoMapper.toDTO(video);
  }

  private Video findVideoById(Long id) {
    return videoRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Видео с id " + id + " не найдено"));
  }
}
