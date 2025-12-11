package com.educationalplatform.streamingservice.controller;

import com.educationalplatform.streamingservice.model.dto.ChunkResponseDTO;
import com.educationalplatform.streamingservice.model.dto.VideoDTO;
import com.educationalplatform.streamingservice.model.dto.VideoInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Video API", description = "Управление видео и потоковой передачей")
@RequestMapping("/api/v1/videos")
@RestController
public interface VideoApi {

  @Operation(summary = "Получить список всех видео")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список видео получен",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = VideoDTO.class))})
  })
  @GetMapping
  ResponseEntity<List<VideoDTO>> getAllVideos();

  @Operation(summary = "Получить информацию о видео по ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Информация о видео получена",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = VideoInfoDTO.class))}),
      @ApiResponse(responseCode = "404", description = "Видео не найдено")
  })
  @GetMapping("/{id}")
  ResponseEntity<VideoInfoDTO> getVideoInfo(@PathVariable("id") Long id);

  @Operation(summary = "Получить зашифрованный чанк видео")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Чанк видео получен",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ChunkResponseDTO.class))}),
      @ApiResponse(responseCode = "404", description = "Видео или чанк не найден")
  })
  @GetMapping("/{id}/stream/{chunkIndex}")
  ResponseEntity<ChunkResponseDTO> getVideoChunk(
      @PathVariable("id") Long id,
      @PathVariable("chunkIndex") Integer chunkIndex);

  @Operation(summary = "Получить MIME тип видео")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "MIME тип видео получен",
          content = {@Content(mediaType = "text/plain")}),
      @ApiResponse(responseCode = "404", description = "Видео не найдено")
  })
  @GetMapping("/{id}/content-type")
  ResponseEntity<String> getVideoContentType(@PathVariable("id") Long id);

  @Operation(summary = "Стриминг видео для воспроизведения")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Видео отдано для воспроизведения"),
      @ApiResponse(responseCode = "404", description = "Видео не найдено")
  })
  @GetMapping("/{id}/stream")
  ResponseEntity<Resource> streamVideo(@PathVariable("id") Long id);

  @Operation(summary = "Загрузить новое видео")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Видео успешно загружено",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = VideoDTO.class))}),
      @ApiResponse(responseCode = "400", description = "Ошибка при загрузке видео")
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<VideoDTO> uploadVideo(
      @Parameter(description = "Файл видео") @RequestParam("file") MultipartFile file,
      @Parameter(description = "Название видео") @RequestParam("title") String title,
      @Parameter(description = "Описание видео") @RequestParam(value = "description", required = false) String description
  );
}
