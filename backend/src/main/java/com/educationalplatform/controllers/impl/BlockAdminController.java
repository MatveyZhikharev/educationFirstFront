package com.educationalplatform.controllers.impl;

import com.educationalplatform.controllers.BlockAdminOperations;
import com.educationalplatform.domain.dto.request.BlockAddRequest;
import com.educationalplatform.domain.dto.request.BlockUpdateRequest;
import com.educationalplatform.domain.dto.response.BlockResponse;
import com.educationalplatform.service.BlockService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class BlockAdminController implements BlockAdminOperations {

  private final BlockService blockService;

  @Override
  public ResponseEntity<BlockResponse> add(BlockAddRequest request) {
    BlockResponse createdBlock = blockService.add(request);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdBlock.getId())
        .toUri();
    return ResponseEntity.created(location).body(createdBlock);
  }

  @Override
  public ResponseEntity<Void> editBlockStatus(Long blockId) {
    blockService.editBlockStatus(blockId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> swapBlocks(Long firstBlockId, Long secondBlockId) {
    blockService.swapBlocks(firstBlockId, secondBlockId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> updatePhoto(Long blockId, MultipartFile image) {
    blockService.updatePhoto(blockId, image);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> updateVideo(Long blockId, MultipartFile video) {
    blockService.updateVideo(blockId, video);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<BlockResponse> updateBlock(BlockUpdateRequest request) {
    return ResponseEntity.ok(blockService.updateBlock(request));
  }

  @Override
  public ResponseEntity<List<BlockResponse>> getAllBlocks() {
    return ResponseEntity.ok(blockService.getAllBlocks());
  }

  @Override
  public ResponseEntity<Void> deleteBlock(Long blockId) {
    blockService.deleteBlock(blockId);
    return ResponseEntity.noContent().build();
  }
}

