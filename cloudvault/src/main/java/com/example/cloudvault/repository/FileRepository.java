package com.example.cloudvault.repository;

import com.example.cloudvault.folder.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByFolderId(Long parentId);
    List<FileEntity> findByNameContainingIgnoreCase(String name);

}
