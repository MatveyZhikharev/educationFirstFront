package com.educationalplatform.domain.dto.response;

import com.educationalplatform.domain.model.Block;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockResponse {
  private Long id;
  private String title;
  private Integer sortOrder;
  private Boolean isAvailable;
  private Long testId;

  public static BlockResponse fromEntity(Block block) {
    BlockResponse r = new BlockResponse();
    r.id = block.getId();
    r.title = block.getTitle();
    r.sortOrder = block.getSortOrder();
    r.isAvailable = block.getIsAvailable();
    r.testId = block.getTest() != null ? block.getTest().getId() : null;
    return r;
  }
}
