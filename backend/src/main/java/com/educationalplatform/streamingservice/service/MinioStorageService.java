package com.educationalplatform.streamingservice.service;

import com.educationalplatform.streamingservice.exception.ResourceNotFoundException;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageService {

  private final MinioClient minioClient;

  @Value("${minio.bucket-name}")
  private String bucketName;

  public String uploadVideo(MultipartFile file, String fileName) {
    try {
      createBucketIfNotExists();

      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName)
              .stream(file.getInputStream(), file.getSize(), -1)
              .contentType(file.getContentType())
              .build()
      );

      log.info("Видео успешно загружено в MinIO: {}", fileName);
      return fileName;
    } catch (Exception e) {
      log.error("Ошибка загрузки видео в MinIO: {}", fileName, e);
      throw new RuntimeException("Не удалось загрузить видео в MinIO", e);
    }
  }

  public InputStream getVideoInputStream(String fileName) {
    try {
      return minioClient.getObject(
          GetObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName)
              .build()
      );
    } catch (Exception e) {
      log.error("Ошибка получения видео из MinIO: {}", fileName, e);
      throw new ResourceNotFoundException("Видео не найдено в хранилище: " + fileName);
    }
  }

  public void deleteVideo(String fileName) {
    try {
      minioClient.removeObject(
          RemoveObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName)
              .build()
      );
      log.info("Видео удалено из MinIO: {}", fileName);
    } catch (Exception e) {
      log.error("Ошибка удаления видео из MinIO: {}", fileName, e);
      throw new RuntimeException("Не удалось удалить видео из MinIO", e);
    }
  }

  private void createBucketIfNotExists() {
    try {
      boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!found) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        log.info("Создан бакет: {}", bucketName);
      }
    } catch (Exception e) {
      log.error("Ошибка проверки/создания бакета: {}", bucketName, e);
      throw new RuntimeException("Не удалось инициализировать бакет MinIO", e);
    }
  }
}
