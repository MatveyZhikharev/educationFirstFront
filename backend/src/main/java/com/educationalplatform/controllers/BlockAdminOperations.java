package com.educationalplatform.controllers;

import com.educationalplatform.domain.dto.request.BlockAddRequest;
import com.educationalplatform.domain.dto.request.BlockUpdateRequest;
import com.educationalplatform.domain.dto.response.BlockResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Blocks", description = "API для управления блоками")
@RequestMapping("/api/admin/blocks")
public interface BlockAdminOperations {

  @Operation(summary = "Создать блок")
  @PostMapping
  ResponseEntity<BlockResponse> add(@RequestBody BlockAddRequest request);

  @Operation(summary = "Опубликовать блок")
  @PatchMapping("/{blockId}/status")
  ResponseEntity<Void> editBlockStatus(@PathVariable Long blockId);

  @Operation(summary = "Поменять местами два блока")
  @PutMapping("/{firstBlockId}/swap/{secondBlockId}")
  ResponseEntity<Void> swapBlocks(
      @PathVariable Long firstBlockId,
      @PathVariable Long secondBlockId);

  @Operation(summary = "Обновить фото блока (заменить)")
  @PutMapping(value = "/{blockId}/image")
  ResponseEntity<Void> updatePhoto(
      @PathVariable Long blockId,
      @RequestPart("image") MultipartFile image);

  @Operation(summary = "Обновить видео блока (заменить)")
  @PutMapping(value = "/{blockId}/video")
  ResponseEntity<Void> updateVideo(
      @PathVariable Long blockId,
      @RequestPart("video") MultipartFile video);

  @Operation(summary = "Обновить текстовые данные блока")
  @PutMapping
  ResponseEntity<BlockResponse> updateBlock(@RequestBody BlockUpdateRequest request);

  @Operation(summary = "Получить все блоки")
  @GetMapping
  ResponseEntity<List<BlockResponse>> getAllBlocks();

  @Operation(summary = "Удалить блок")
  @DeleteMapping(value = "/{blockId}")
  ResponseEntity<Void> deleteBlock(@PathVariable Long blockId);
}
