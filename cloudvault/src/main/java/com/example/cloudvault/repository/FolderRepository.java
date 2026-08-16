package com.example.cloudvault.repository;

import com.example.cloudvault.folder.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    List<FolderEntity> findByParentIsNull();

    List<FolderEntity> findByParentId(Long parentId);
    List<FolderEntity> findByNameContainingIgnoreCase(String name);

}
