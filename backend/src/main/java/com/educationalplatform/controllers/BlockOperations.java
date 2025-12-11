package com.educationalplatform.controllers;

import com.educationalplatform.domain.dto.response.BlockResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Available Blocks", description = "API для работы с блоками")
@RequestMapping("/api/blocks")
public interface BlockOperations {
  @Operation(summary = "Получить список всех опубликованных блоков")
  @GetMapping
  ResponseEntity<List<BlockResponse>> getAllAvailableBlocks();

  @Operation(summary = "Получить изображение опубликованного блока")
  @GetMapping(value = "/{blockId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
  ResponseEntity<InputStreamResource> getBlockImage(@PathVariable Long blockId);
}
