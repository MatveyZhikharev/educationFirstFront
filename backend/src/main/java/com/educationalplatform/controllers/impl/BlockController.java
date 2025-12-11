package com.educationalplatform.controllers.impl;

import com.educationalplatform.controllers.BlockOperations;
import com.educationalplatform.domain.dto.response.BlockResponse;
import com.educationalplatform.service.BlockService;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlockController implements BlockOperations {

  private final BlockService blockService;

  @Override
  public ResponseEntity<List<BlockResponse>> getAllAvailableBlocks() {
    return ResponseEntity.ok(blockService.getAllAvailableBlocks());
  }

  @Override
  public ResponseEntity<InputStreamResource> getBlockImage(Long blockId) {
    InputStream imageStream = blockService.getBlockImage(blockId);

    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_JPEG)
        .body(new InputStreamResource(imageStream));
  }
}
