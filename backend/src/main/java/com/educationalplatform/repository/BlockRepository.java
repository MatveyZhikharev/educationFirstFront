package com.educationalplatform.repository;

import com.educationalplatform.domain.model.Block;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlockRepository extends JpaRepository<Block, Long> {

  List<Block> findAllByIsAvailableIsTrue();

  @Query("SELECT COALESCE(MAX(b.sortOrder), 0) FROM Block b")
  int findMaxSortOrder();
}
