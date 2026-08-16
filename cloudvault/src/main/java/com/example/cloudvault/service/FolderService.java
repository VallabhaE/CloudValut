package com.example.cloudvault.service;

import com.example.cloudvault.dto.UserDt;
import com.example.cloudvault.folder.FileEntity;
import com.example.cloudvault.folder.FolderEntity;
import com.example.cloudvault.middleware.Middleware;
import com.example.cloudvault.repository.FileRepository;
import com.example.cloudvault.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;

    @Autowired
    private Environment environment;

    public FolderService(FolderRepository folderRepository, FileRepository fileRepository) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;

    }

    public void deleteFile(long id) {
        try {
            FileEntity file = fileRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("File not found"));
            Path filePath = Paths.get(file.getStorageKey());

            Files.deleteIfExists(filePath);
            fileRepository.deleteById(id);

        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void deleteFolder(long id){
        try {
            FolderEntity file = folderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("File not found"));
            Path filePath = Paths.get(file.getStorageKey());

            Files.deleteIfExists(filePath);
            folderRepository.deleteById(id);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public FolderEntity createFolder(String name, Long parentId,Long userId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder name is required");
        }
        String defaultPath = environment.getProperty("cloud.defaultPath");

        if (defaultPath == null || defaultPath.isBlank()) {
            throw new IllegalStateException(
                    "cloud.defaultPath is not configured"
            );
        }

        FolderEntity parent = null;
        String storageKey;

        if (parentId != null) {
            System.out.println("In");
            parent = folderRepository.findById(parentId)
                    .orElseThrow(() ->
                            new RuntimeException("Parent folder not found"));
            System.out.println("out"+parent.getStorageKey());
            storageKey = Paths.get(
                    parent.getStorageKey(),
                    name
            ).toString().replace("\\", "/");

        } else {

            storageKey = Paths.get(
                    defaultPath,
                    String.valueOf(userId),
                    name
            ).toString().replace("\\", "/");        }


        // verify user info available
        FolderEntity folder = new FolderEntity();

        folder.setName(name);
        folder.setParent(parent);
        folder.setStorageKey(storageKey);
        folder.setCreatedAt(LocalDateTime.now());
        folder.setUpdatedAt(LocalDateTime.now());
        folder.setUserId(userId);
        return folderRepository.save(folder);
    }

    public List getFolderData(long id){
        List s = folderRepository.findByParentId(id);
        s.addAll(fileRepository.findByFolderId(id));
        return s;
    }

    public boolean upload(MultipartFile file, Long folderId,String path) {
        String fileName = file.getOriginalFilename();
        String defaultPath = environment.getProperty("cloud.defaultPath");

        try{
            if (defaultPath == null || defaultPath.isBlank()) {
                throw new IllegalStateException("cloud.defaultPath is not configured");
            }
            if (fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException("File name is missing");
            }
            Path uploadPath = Paths.get(defaultPath);
            if (path != null && !path.isBlank()) {
                uploadPath = uploadPath.resolve(path);
            }

            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }catch (IOException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean saveMetaDataOnSuccess(
            MultipartFile file,
            Long folderId,
            String path
    ) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name is missing");
        }

        String storageKey;

        if (path == null || path.isBlank()) {
            storageKey = fileName;
        } else {
            storageKey = Paths.get(path, fileName)
                    .toString()
                    .replace("\\", "/");
        }

        FileEntity fileEntity = new FileEntity();

        fileEntity.setName(fileName);
        FolderEntity folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
        fileEntity.setFolder(folder);
        fileEntity.setStorageKey(storageKey);
        fileEntity.setContentType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileRepository.save(fileEntity);
        return true;
    }


    public List<Object> search(String name) {

        List<Object> result = new ArrayList<>();

        result.addAll(folderRepository.findByNameContainingIgnoreCase(name));
        result.addAll(fileRepository.findByNameContainingIgnoreCase(name));

        return result;
    }
}
